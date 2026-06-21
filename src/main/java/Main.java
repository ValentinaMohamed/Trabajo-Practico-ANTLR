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

    	// Extraer codigo fuente de archivos de extensión .mini
    	String[] archivos = {
    			"src/programs/programa1.mini",
    		    "src/programs/programa2.mini",
    		    "src/programs/programa3.mini"
    		};
    	
    	int contador = 0;

    		for (String archivo : archivos) {
    			
    			contador++;
    		    System.out.println("=============== Ejecutando programa " + contador + " ===============");

    		    CharStream input = CharStreams.fromFileName(archivo);
    		    
    		    // Convierte el texto en tokens
    		    MiniLangLexer lexer = new MiniLangLexer(input);
    		    // Agrupa los tokens para que el parser los consuma
    		    CommonTokenStream tokens = new CommonTokenStream(lexer);
    		    // Usa la gramática para construir el árbol sintáctico.
    		    MiniLangParser parser = new MiniLangParser(tokens);
    		    ParseTree tree = parser.programa();

    		    // Analizador semántico
    		    SymbolTable symbolTable = new SymbolTable();
    		    SemanticAnalyzer semantic = new SemanticAnalyzer(symbolTable);
    		    try {
    		        semantic.visit(tree);
    		        System.out.println("Análisis semántico completado\n");
    		    } catch (RuntimeException e) {
    		        System.out.println("Error semántico: " + e.getMessage() + "\n");
    		    }

    		    // Intérprete
    		    SymbolTable runtimeTable = new SymbolTable();
    		    Interpreter interpreter = new Interpreter(runtimeTable);
    		    try {
    		        interpreter.visit(tree);
    		        System.out.println("\nEjecución completada\n");
    		    } catch (RuntimeException e) {
    		        System.out.println("\nError en ejecución: " + e.getMessage() + "\n");
    		    }

    		    System.out.println();
    		}
        
    }
}