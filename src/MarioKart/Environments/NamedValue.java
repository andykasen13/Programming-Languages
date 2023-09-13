package MarioKart.Environments;
import MarioKart.LexicalAnalysis.Type;
import MarioKart.LexicalAnalysis.Lexeme;

public class NamedValue {
    //-------------Instance Variables-------------
    private static Lexeme name;
    private static Lexeme value;
    private static Type type;
    private static boolean isConstant;
    private static boolean isPrivate;

    //-------------Constructor-------------
    public NamedValue(Type type, Lexeme name) {
        this.type = type;
        this.name = name;
        this.value = new Lexeme(null);
    }

    //-------------Getters and Setters-------------
    public static Lexeme getName() { return name; }
    public static Lexeme getValue() { return value; }
    public static Type getType() { return type; }
    public static boolean isConstant() { return isConstant; }
    public static boolean isPrivate() { return isPrivate; }

    public void setValue(Lexeme value) { this.value = value; }

    //------------- toString -------------
    public String toString() { return name.getWord() + ": " + value.toValueOnlyString() + " (" + type + ")"; }

}
