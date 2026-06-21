public class SemanticAnalyzer extends MiniLangBaseVisitor<Object> {
    private SymbolTable symbolTable;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    
    // ---------------- Declaración ----------------
    @Override
    public Object visitDeclaracion(MiniLangParser.DeclaracionContext ctx) {
        String name = ctx.ID().getText();
        String type = ctx.tipo().getText();

        if (symbolTable.exists(name)) {
            throw new RuntimeException("Variable redeclarada: " + name);
        }

        Object value = ctx.expresion() != null ? visit(ctx.expresion()) : null;

        // Validar que el valor inicial coincida con el tipo declarado
        if (value != null && !isCompatible(type, value)) {
            throw new RuntimeException("Tipo incompatible en declaración: " + name +
                                       " es " + type + " pero se asigna " +
                                       value.getClass().getSimpleName());
        }

        symbolTable.declare(name, type, value);
        return null;
    }


    // ---------------- Asignación ----------------
    @Override
    public Object visitAsignacion(MiniLangParser.AsignacionContext ctx) {
        String name = ctx.ID().getText();

        if (!symbolTable.exists(name)) {
            throw new RuntimeException("Variable no declarada: " + name);
        }

        Object value = visit(ctx.expresion());
        String declaredType = symbolTable.getType(name);

        // Validar compatibilidad
        if (!isCompatible(declaredType, value)) {
            throw new RuntimeException("Asignación inválida: " + name + " es " + declaredType +
                                       " pero se intenta asignar " + value.getClass().getSimpleName());
        }

        symbolTable.assign(name, value);
        return null;
    }


    // ---------------- Print ----------------
    @Override
    public Object visitImprimir(MiniLangParser.ImprimirContext ctx) {
        Object value = visit(ctx.expresion());
        System.out.println(value);
        return null;
    }
    
    // ---------------- If ----------------
    @Override
    public Object visitIfSimple(MiniLangParser.IfSimpleContext ctx) {
        Object condition = visit(ctx.expresion());

        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("La condición del if debe ser booleana");
        }

        if ((Boolean) condition) {
            for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
                visit(instr);
            }
        }

        return null;
    }
    
    // ---------------- If-Else ----------------
    @Override
    public Object visitIfElse(MiniLangParser.IfElseContext ctx) {
        Object condition = visit(ctx.expresion());
        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("La condición del if debe ser booleana");
        }

        if ((Boolean) condition) {
            for (MiniLangParser.InstruccionContext instr : ctx.ifBlock) {
                visit(instr);
            }
        } else {
            for (MiniLangParser.InstruccionContext instr : ctx.elseBlock) {
                visit(instr);
            }
        }
        return null;
    }

    
    // ---------------- Do-While ----------------
    @Override
    public Object visitDoWhile(MiniLangParser.DoWhileContext ctx) {
        Object condition;
        do {
            for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
                visit(instr);
            }
            condition = visit(ctx.expresion());
            if (!(condition instanceof Boolean)) {
                throw new RuntimeException("La condición del do-while debe ser booleana");
            }
        } while ((Boolean) condition);
        return null;
    }

    
    // ---------------- Expresiones ----------------
    @Override
    public Object visitExpresionLogica(MiniLangParser.ExpresionLogicaContext ctx) {
        Object left = visit(ctx.expresionRelacional(0));
        if (ctx.expresionRelacional().size() == 1) return left;

        for (int i = 1; i < ctx.expresionRelacional().size(); i++) {
            Object right = visit(ctx.expresionRelacional(i));
            String op = ctx.getChild(2 * i - 1).getText();
            if (!(left instanceof Boolean) || !(right instanceof Boolean)) {
                throw new RuntimeException("Operador lógico requiere booleanos");
            }
            left = op.equals("&&") ? (Boolean) left && (Boolean) right
                                   : (Boolean) left || (Boolean) right;
        }
        return left;
    }

    
    @Override
    public Object visitExpresionRelacional(MiniLangParser.ExpresionRelacionalContext ctx) {
        Object left = visit(ctx.expresionAritmetica(0));
        if (ctx.expresionAritmetica().size() == 1) return left;

        Object right = visit(ctx.expresionAritmetica(1));
        String op = ctx.getChild(1).getText();

        // Comparaciones de igualdad funcionan con cualquier tipo
        if (op.equals("==")) {
            return left.equals(right);
        }
        if (op.equals("!=")) {
            return !left.equals(right);
        }

        // Números para <, <=, >, >= 
        if (!(left instanceof Number) || !(right instanceof Number)) {
            throw new RuntimeException("Operación relacional inválida: " +
                                       left.getClass().getSimpleName() + " " + op + " " +
                                       right.getClass().getSimpleName());
        }

        double l = ((Number) left).doubleValue();
        double r = ((Number) right).doubleValue();

        switch (op) {
            case "<":  return l < r;
            case "<=": return l <= r;
            case ">":  return l > r;
            case ">=": return l >= r;
        }
        return null;
    }

    
    @Override
    public Object visitExpresionAritmetica(MiniLangParser.ExpresionAritmeticaContext ctx) {
        Object result = visit(ctx.termino(0));

        for (int i = 1; i < ctx.termino().size(); i++) {
            Object right = visit(ctx.termino(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (!(result instanceof Number) || !(right instanceof Number)) {
                throw new RuntimeException("Operación aritmética inválida: " +
                                           result.getClass().getSimpleName() + " " + op + " " +
                                           right.getClass().getSimpleName());
            }

            // Si alguno es Double
            boolean useDouble = (result instanceof Double) || (right instanceof Double);

            if (useDouble) {
                double l = ((Number) result).doubleValue();
                double r = ((Number) right).doubleValue();
                result = op.equals("+") ? l + r : l - r;
            } else {
                int l = ((Number) result).intValue();
                int r = ((Number) right).intValue();
                result = op.equals("+") ? l + r : l - r;
            }
        }
        return result;
    }


    @Override
    public Object visitTermino(MiniLangParser.TerminoContext ctx) {
        Object result = visit(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++) {
            Object right = visit(ctx.factor(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (!(result instanceof Number) || !(right instanceof Number)) {
                throw new RuntimeException("Operación aritmética requiere números");
            }

            boolean useDouble = (result instanceof Double) || (right instanceof Double);

            if (useDouble) {
                double l = ((Number) result).doubleValue();
                double r = ((Number) right).doubleValue();
                if (op.equals("/") && r == 0.0) {
                    throw new RuntimeException("No se puede realizar una división por cero");
                }
                result = op.equals("*") ? l * r : l / r;
            } else {
                int l = ((Number) result).intValue();
                int r = ((Number) right).intValue();
                if (op.equals("/") && r == 0) {
                    throw new RuntimeException("No se puede realizar una división por cero");
                }
                result = op.equals("*") ? l * r : l / r;
            }
        }
        return result;
    }

    
    @Override
    public Object visitFactor(MiniLangParser.FactorContext ctx) {
        if (ctx.CADENA() != null) return ctx.CADENA().getText().replace("\"", "");
        if (ctx.ENTERO() != null) return Integer.parseInt(ctx.ENTERO().getText());
        if (ctx.REAL() != null) return Double.parseDouble(ctx.REAL().getText());
        if (ctx.BOOLEANO() != null) return Boolean.parseBoolean(ctx.BOOLEANO().getText());
        if (ctx.ID() != null) {
            if (!symbolTable.exists(ctx.ID().getText())) {
                throw new RuntimeException("Variable no declarada: " + ctx.ID().getText());
            }
            return symbolTable.getValue(ctx.ID().getText());
        }
        if (ctx.expresion() != null) return visit(ctx.expresion());
        return null;
    }


    // ---------------- Auxiliares ----------------
    private boolean isCompatible(String type, Object value) {
        switch (type) {
            case "int":    return value instanceof Integer;
            case "real":   return value instanceof Double || value instanceof Integer;
            case "string": return value instanceof String;
            case "bool":   return value instanceof Boolean;
            default:       return false;
        }
    }

}
