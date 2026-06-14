import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) throws Exception {

    	// Simula un archivo fuente con el código
    	CharStream input = CharStreams.fromString(
    			
    			"// Declaración de variables\n" +
    			
    		    "var saludo : string = \"Hola\";\n" +
    		    "var numero : int = 1234;\n" +
    		    "var numero_real : real = 98.3;\n" +
    		    "var boolean : bool = true;\n" +
    		    
                "/* Impresión de variables */\n" +

    		    "print(saludo);\n" +
    		    "print(numero);\n" +
    		    "print(numero_real);\n" +
    		    "print(boolean);\n" +
    		    
                "// Cambio de valor\n" +
    		    
    		    "saludo = \"Chau\";\n" +
    		    "print(saludo);\n"
    		);

        // Convierte el texto en tokens
        MiniLangLexer lexer = new MiniLangLexer(input);

        // Agrupa los tokens para que el parser los consuma.
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Usa la gramática para construir el árbol sintáctico.
        MiniLangParser parser = new MiniLangParser(tokens);
        ParseTree tree = parser.programa();

        // Salida
        System.out.println(tree.toStringTree(parser));
    }
}