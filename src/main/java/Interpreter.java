public class Interpreter extends MiniLangBaseVisitor<Object> {

    private SymbolTable symbolTable;

    public Interpreter(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    // ---------- Declaración ----------
    @Override
    public Object visitDeclaracion(MiniLangParser.DeclaracionContext ctx) {
        String name = ctx.ID().getText();
        String type = ctx.tipo().getText();

        Object value = ctx.expresion() != null ? visit(ctx.expresion()) : null;

        symbolTable.declare(name, type, value);
        return null;
    }

    // ---------- Asignación ----------
    @Override
    public Object visitAsignacion(MiniLangParser.AsignacionContext ctx) {
        String name = ctx.ID().getText();
        Object value = visit(ctx.expresion());

        symbolTable.assign(name, value);
        return null;
    }

    // ---------- Print ----------
    @Override
    public Object visitImprimir(MiniLangParser.ImprimirContext ctx) {
        Object value = visit(ctx.expresion());
        System.out.println(value);
        return null;
    }

    // ---------- If ----------
    @Override
    public Object visitIfSimple(MiniLangParser.IfSimpleContext ctx) {
        Boolean condition = (Boolean) visit(ctx.expresion());

        if (condition) {
            for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
                visit(instr);
            }
        }

        return null;
    }

    // ---------- If-Else ----------
    @Override
    public Object visitIfElse(MiniLangParser.IfElseContext ctx) {
        Boolean condition = (Boolean) visit(ctx.expresion());

        if (condition) {
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

    // ---------- Do-While ----------
    @Override
    public Object visitDoWhile(MiniLangParser.DoWhileContext ctx) {
        Boolean condition;

        do {
            for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
                visit(instr);
            }

            condition = (Boolean) visit(ctx.expresion());

        } while (condition);

        return null;
    }

    // ---------- Expresiones lógicas ----------
    @Override
    public Object visitExpresionLogica(MiniLangParser.ExpresionLogicaContext ctx) {
        Object left = visit(ctx.expresionRelacional(0));

        if (ctx.expresionRelacional().size() == 1) {
            return left;
        }

        for (int i = 1; i < ctx.expresionRelacional().size(); i++) {
            Object right = visit(ctx.expresionRelacional(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (op.equals("&&")) {
                left = (Boolean) left && (Boolean) right;
            } else {
                left = (Boolean) left || (Boolean) right;
            }
        }

        return left;
    }
    
    // ---------- Expresiones relacionales ----------
    @Override
    public Object visitExpresionRelacional(MiniLangParser.ExpresionRelacionalContext ctx) {
        Object left = visit(ctx.expresionAritmetica(0));

        if (ctx.expresionAritmetica().size() == 1) {
            return left;
        }

        Object right = visit(ctx.expresionAritmetica(1));
        String op = ctx.getChild(1).getText();

        if (op.equals("==")) return left.equals(right);
        if (op.equals("!=")) return !left.equals(right);

        double l = ((Number) left).doubleValue();
        double r = ((Number) right).doubleValue();

        switch (op) {
            case "<": return l < r;
            case "<=": return l <= r;
            case ">": return l > r;
            case ">=": return l >= r;
        }

        return null;
    }

    // ---------- Expresiones aritméticas ----------
    @Override
    public Object visitExpresionAritmetica(MiniLangParser.ExpresionAritmeticaContext ctx) {
        Object result = visit(ctx.termino(0));

        for (int i = 1; i < ctx.termino().size(); i++) {
            Object right = visit(ctx.termino(i));
            String op = ctx.getChild(2 * i - 1).getText();

            boolean useDouble = result instanceof Double || right instanceof Double;

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

    // ---------- Términos ----------
    @Override
    public Object visitTermino(MiniLangParser.TerminoContext ctx) {
        Object result = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++) {
            Object right = visit(ctx.factor(i));
            String op = ctx.getChild(2 * i - 1).getText();

            boolean useDouble = result instanceof Double || right instanceof Double;

            if (useDouble) {
                double l = ((Number) result).doubleValue();
                double r = ((Number) right).doubleValue();
                result = op.equals("*") ? l * r : l / r;
            } else {
                int l = ((Number) result).intValue();
                int r = ((Number) right).intValue();
                result = op.equals("*") ? l * r : l / r;
            }
        }

        return result;
    }

    // ---------- Factor ----------
    @Override
    public Object visitFactor(MiniLangParser.FactorContext ctx) {
        if (ctx.CADENA() != null) {
            return ctx.CADENA().getText().replace("\"", "");
        }
        
        if (ctx.ENTERO() != null) {
            int val = Integer.parseInt(ctx.ENTERO().getText());
            // Si el primer hijo es "-", aplicar signo
            if (ctx.getChild(0).getText().equals("-")) return -val;
            return val;
        }

        if (ctx.REAL() != null) {
            double val = Double.parseDouble(ctx.REAL().getText());
            if (ctx.getChild(0).getText().equals("-")) return -val;
            return val;
        }
        

        if (ctx.BOOLEANO() != null) {
            return Boolean.parseBoolean(ctx.BOOLEANO().getText());
        }

        if (ctx.ID() != null) {
            return symbolTable.getValue(ctx.ID().getText());
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        }

        return null;
    }
}