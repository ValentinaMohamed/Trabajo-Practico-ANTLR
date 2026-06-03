import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) throws Exception {

        CharStream input = CharStreams.fromString("print(\"Hola\");");

        MiniLangLexer lexer = new MiniLangLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        MiniLangParser parser = new MiniLangParser(tokens);

        ParseTree tree = parser.programa();

        System.out.println(tree.toStringTree(parser));
    }
}