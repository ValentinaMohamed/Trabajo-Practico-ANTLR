import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

	// Imprime el árbol por renglones
	public static void printTree(ParseTree tree, Parser parser, int indent) {
	    String indentStr = " ".repeat(indent);
	    String nodeText = Trees.getNodeText(tree, parser);
	    System.out.println(indentStr + nodeText);

	    for (int i = 0; i < tree.getChildCount(); i++) {
	        printTree(tree.getChild(i), parser, indent + 2);
	    }
	}
	
    public static void main(String[] args) throws Exception {

    	// Simula un archivo fuente con el código
    	CharStream input = CharStreams.fromString(
    			
    			"// Declaración de variables\n" +
    			
    		    "var saludo : string = \"Hola\";\n" +
    		    "var numero : int = 1234;\n" +
    		    "var numero_real : real = 98.3;\n" +
    		    "var boolean : bool = true;\n" +
    		    
    		    
                "// Expresiones\n" +
                
                "var suma : int = numero + 10;\n" +
                "var resta : int = numero - 10;\n" +
                "var multiplicacion : real = numero_real * 2;\n" +
                "var division : real = numero_real / 2;\n" +
                "var comparacion : bool = numero > 100;\n" +
                "var logico : bool = comparacion && true;\n" +
    		    
                
                "/* Impresión de variables */\n" +

    		    "print(saludo);\n" +
    		    "print(numero);\n" +
    		    "print(numero_real);\n" +
    		    "print(boolean);\n" +
    		    "print(suma);\n" +
    		    "print(resta);\n" +
    		    "print(multiplicacion);\n" +
    		    "print(division);\n" +
    		    "print(comparacion);\n" +
    		    "print(logico);\n" +
    		    
    		    
                "// Cambio de valor\n" +
    		    
    		    "saludo = \"Chau\";\n" +
    		    "print(saludo);\n" +
    		    
				
				"// Condicional if-else\n" +

				"var nota : int = 8;\n" +
				"if (nota >= 6) {\n" +
				"   print(\"Aprobado\");\n" +
				"} else {\n" +
				"   print(\"Desaprobado\");\n" +
				"}\n" +
				
    		    
    		    "// Do while\n" +
    		    
    		    "var i : int = 0;\n" +
    		    "do {\n" +
    		    "   print(i);\n" +
    		    "   i = i + 1;\n" +
    		    "} while (i < 3);\n"
    		);

        // Convierte el texto en tokens
        MiniLangLexer lexer = new MiniLangLexer(input);

        // Agrupa los tokens para que el parser los consuma.
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Usa la gramática para construir el árbol sintáctico.
        MiniLangParser parser = new MiniLangParser(tokens);
        ParseTree tree = parser.programa();

        // Salida
        // System.out.println(tree.toStringTree(parser));
        
        // Salida árbol 
        printTree(tree, parser, 0);
    }
}