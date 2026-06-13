import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) throws Exception {

    	// Simula un archivo fuente con el código
        CharStream input = CharStreams.fromString(
        	    "print(\"Hola\");\n" +
        	    "print(123);\n" +
        	    "print(98.3);\n" +
        	    "print(true);\n"
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