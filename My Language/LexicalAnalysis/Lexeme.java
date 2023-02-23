package LexicalAnalysis;

public class Lexeme {
    //Instance Variables
    private final Types type;
    private int lineNumber;

    //------Instance Variable Declaration------
    private String word;
    private int intValue;
    private boolean boolValue;

    //-----------Constructors-------------
    public Lexeme(int lineNumber, Types type) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public Lexeme(int lineNumber, String stringValue, Types type) {
        this.lineNumber = lineNumber;
        this.word = stringValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, int intValue, Types type) {
        this.lineNumber = lineNumber;
        this.intValue = intValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, boolean boolValue, Types type) {
        this.lineNumber = lineNumber;
        this.boolValue = boolValue;
        this.type = type;
    }

    //---------Getters and Setters------------
    public int getIntValue() {
        return intValue;
    }
    public boolean getBoolValue() {
        return boolValue;
    }
    public int getLineNumber() {
        return lineNumber;
    }
    public String getWord() {
        return word;
    }
    public Types getType() {
        return type;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public void setType(Types type) {
        this.type = type;
    }

    //--------toString-----------
    public String toString() {
        System.out.println("Lexeme of type" + type + "and line number " + lineNumber);
        System.out.println("Boolean value: " + boolValue);
        System.out.println("Integer Value: " + intValue);
        System.out.println("String value: " + word);
    }
}
