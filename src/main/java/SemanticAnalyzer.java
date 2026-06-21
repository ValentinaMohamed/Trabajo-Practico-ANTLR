public class SemanticAnalyzer extends MiniLangBaseVisitor<String> {

    private SymbolTable symbolTable;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    // ---------- Declaración ----------
    @Override
    public String visitDeclaracion(MiniLangParser.DeclaracionContext ctx) {
        String name = ctx.ID().getText();
        String declaredType = ctx.tipo().getText();

        if (symbolTable.exists(name)) {
            throw new RuntimeException("Variable redeclarada: " + name);
        }

        if (ctx.expresion() != null) {
            String exprType = visit(ctx.expresion());

            if (!isCompatible(declaredType, exprType)) {
                throw new RuntimeException(
                    "Tipo incompatible en declaración: " + name +
                    " es " + declaredType + " pero recibe " + exprType
                );
            }
        }

        symbolTable.declare(name, declaredType, null);
        return null;
    }

    // ---------- Asignación ----------
    @Override
    public String visitAsignacion(MiniLangParser.AsignacionContext ctx) {
        String name = ctx.ID().getText();

        if (!symbolTable.exists(name)) {
            throw new RuntimeException("Variable no declarada: " + name);
        }

        String declaredType = symbolTable.getType(name);
        String exprType = visit(ctx.expresion());

        if (!isCompatible(declaredType, exprType)) {
            throw new RuntimeException(
                "Asignación inválida: " + name +
                " es " + declaredType + " pero recibe " + exprType
            );
        }

        return null;
    }

    // ---------- Print ----------
    @Override
    public String visitImprimir(MiniLangParser.ImprimirContext ctx) {
        visit(ctx.expresion());
        return null;
    }

    // ---------- If ----------
    @Override
    public String visitIfSimple(MiniLangParser.IfSimpleContext ctx) {
        String conditionType = visit(ctx.expresion());

        if (!conditionType.equals("bool")) {
            throw new RuntimeException("La condición del if debe ser booleana");
        }

        for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
            visit(instr); // se revisan todas las instrucciones, no se ejecutan
        }

        return null;
    }

    // ---------- If-Else ----------
    @Override
    public String visitIfElse(MiniLangParser.IfElseContext ctx) {
        String conditionType = visit(ctx.expresion());

        if (!conditionType.equals("bool")) {
            throw new RuntimeException("La condición del if debe ser booleana");
        }

        for (MiniLangParser.InstruccionContext instr : ctx.ifBlock) {
            visit(instr);
        }

        for (MiniLangParser.InstruccionContext instr : ctx.elseBlock) {
            visit(instr);
        }

        return null;
    }

    // ---------- Do-While ----------
    @Override
    public String visitDoWhile(MiniLangParser.DoWhileContext ctx) {
        for (MiniLangParser.InstruccionContext instr : ctx.instruccion()) {
            visit(instr);
        }

        String conditionType = visit(ctx.expresion());

        if (!conditionType.equals("bool")) {
            throw new RuntimeException("La condición del do-while debe ser booleana");
        }

        return null;
    }

    // ---------- Expresiones lógicas ----------
    @Override
    public String visitExpresionLogica(MiniLangParser.ExpresionLogicaContext ctx) {
        String leftType = visit(ctx.expresionRelacional(0));

        if (ctx.expresionRelacional().size() == 1) {
            return leftType;
        }

        for (int i = 1; i < ctx.expresionRelacional().size(); i++) {
            String rightType = visit(ctx.expresionRelacional(i));

            if (!leftType.equals("bool") || !rightType.equals("bool")) {
                throw new RuntimeException("Los operadores lógicos requieren booleanos");
            }

            leftType = "bool";
        }

        return "bool";
    }

    // ---------- Expresiones relacionales ----------
    @Override
    public String visitExpresionRelacional(MiniLangParser.ExpresionRelacionalContext ctx) {
        String leftType = visit(ctx.expresionAritmetica(0));

        if (ctx.expresionAritmetica().size() == 1) {
            return leftType;
        }

        String rightType = visit(ctx.expresionAritmetica(1));
        String op = ctx.getChild(1).getText();
        
        if (op.equals("==") || op.equals("!=")) {
            return "bool";
        }

        if (!isNumeric(leftType, rightType)) {
            throw new RuntimeException(
                "Operación relacional inválida entre " + leftType + " y " + rightType
            );
        }

        return "bool";
    }

    // ---------- Expresiones aritméticas ----------
    @Override
    public String visitExpresionAritmetica(MiniLangParser.ExpresionAritmeticaContext ctx) {
        String resultType = visit(ctx.termino(0));

        for (int i = 1; i < ctx.termino().size(); i++) {
            String rightType = visit(ctx.termino(i));

            if (!isNumeric(resultType) || !isNumeric(rightType)) {
                throw new RuntimeException(
                    "Operación aritmética inválida entre " + resultType + " y " + rightType
                );
            }

            if (resultType.equals("real") || rightType.equals("real")) {
                resultType = "real";
            } else {
                resultType = "int";
            }
        }

        return resultType;
    }

    // ---------- Términos ----------
    @Override
    public String visitTermino(MiniLangParser.TerminoContext ctx) {
        String resultType = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++) {
            String rightType = visit(ctx.factor(i));
            String op = ctx.getChild(2 * i - 1).getText();

            if (!isNumeric(resultType) || !isNumeric(rightType)) {
                throw new RuntimeException(
                    "Operación aritmética inválida entre " + resultType + " y " + rightType
                );
            }
            
            // Validación de división por cero
            if (op.equals("/")) {
                // Si el factor derecho es un literal entero o real con valor 0
                if (ctx.factor(i).ENTERO() != null && ctx.factor(i).getText().equals("0")) {
                    throw new RuntimeException("No se puede dividir por 0");
                }
                if (ctx.factor(i).REAL() != null && ctx.factor(i).getText().equals("0.0")) {
                	throw new RuntimeException("No se puede dividir por 0");
                }
            }

            if (resultType.equals("real") || rightType.equals("real")) {
                resultType = "real";
            } else {
                resultType = "int";
            }
        }

        return resultType;
    }

    // ---------- Factor ----------
    @Override
    public String visitFactor(MiniLangParser.FactorContext ctx) {
        if (ctx.CADENA() != null) return "string";
        if (ctx.ENTERO() != null) return "int";
        if (ctx.REAL() != null) return "real";
        if (ctx.BOOLEANO() != null) return "bool";

        if (ctx.ID() != null) {
            String name = ctx.ID().getText();

            if (!symbolTable.exists(name)) {
                throw new RuntimeException("Variable no declarada: " + name);
            }

            return symbolTable.getType(name);
        }

        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        }

        return null;
    }

    // ---------- Auxiliares ----------
    private boolean isCompatible(String declaredType, String exprType) {
        if (declaredType.equals(exprType)) return true;

        // Permitir asignar int a real
        if (declaredType.equals("real") && exprType.equals("int")) return true;

        return false;
    }

    private boolean isNumeric(String type) {
        return type.equals("int") || type.equals("real");
    }

    private boolean isNumeric(String left, String right) {
        return isNumeric(left) && isNumeric(right);
    }
}