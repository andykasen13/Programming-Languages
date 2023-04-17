package MarioKart.Environments;
import static MarioKart.LexicalAnalysis.Type.*;
import MarioKart.LexicalAnalysis.Lexeme;

public class EnvironmentTest {
    public static void main(String[] args) {
        Environment globalEnvironment = new Environment();
        Environment env1 = new Environment(globalEnvironment); 

        Lexeme lex1 = new Lexeme(5, 8, INT);
        env1.add(INT, new Lexeme(5, "x", IDENTIFIER), new Lexeme(lex1.getLineNumber(), lex1.getIntValue(), lex1.getType()));
        System.out.println(env1);
        System.out.println(globalEnvironment);
    }
}
