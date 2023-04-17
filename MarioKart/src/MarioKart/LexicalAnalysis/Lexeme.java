package MarioKart.LexicalAnalysis;

import java.util.ArrayList;
import java.util.Arrays;

import static MarioKart.LexicalAnalysis.Type.*;
import MarioKart.Environments.Environment;
public class Lexeme {
    //---------- Instance Variables --------------
    private final Type type;
    private Integer lineNumber;
    private ArrayList<Lexeme> children = new ArrayList<>();
    private Environment definingEnvironment;

    //------ Instance Variable Declaration ------
    private String word;
    private Integer intValue;
    private Double realValue;
    private Character charValue;
    private Boolean boolValue;
    private Object[] arrayValue;

    //---------- Helper Functions -----------
    public void addChild(Lexeme newChild){
        children.add(newChild);
    }
    public void addChildren(ArrayList<Lexeme> newChildren){
        children.addAll(newChildren);
    }
    public Lexeme getChild(int i) {
        return children.get(i);
    }
    public ArrayList<Lexeme> getChildren() {
        return children;
    }
    public int getNumOfChildren() {
        return children.size();
    }
    public void setChild(Lexeme newChild, int i) {
        children.set(i, newChild);
    }

    //-----------Constructors-------------
    public Lexeme(int lineNumber, Type myType) {
        this.lineNumber = lineNumber;
        this.type = myType;
    }

    public Lexeme(int lineNumber, String stringValue, Type myType) {
        this.lineNumber = lineNumber;
        this.word = stringValue;
        this.type = myType;
    }

    public Lexeme(int lineNumber, int intValue, Type myType) {
        this.lineNumber = lineNumber;
        this.intValue = intValue;
        this.type = myType;
    }

    public Lexeme(int lineNumber, boolean boolValue, Type myType) {
        this.lineNumber = lineNumber;
        this.boolValue = boolValue;
        this.type = myType;
    }

    public Lexeme(int lineNumber, Object[] arrayValue, Type myType) {
        this.lineNumber = lineNumber;
        this.arrayValue = arrayValue;
        this.type = myType;
    }

    public Lexeme(int lineNumber, double realValue, Type myType) {
        this.lineNumber = lineNumber;
        this.realValue = realValue;
        this.type = myType;
    }

    public Lexeme(Type type, int lineNumber) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public Lexeme(Type type) {
        this.type = type;
    }

    //---------Getters and Setters------------
    public int getIntValue() { return intValue; }
    public double getRealValue() { return realValue; }
    public boolean getBoolValue() { return boolValue; }
    public int getLineNumber() { return lineNumber; }
    public String getWord() { return word; }
    public Type getType() { return type; }
    public Object[] getArrayValue() { return arrayValue; }
    public Character getCharValue() { return charValue;}
    public Environment getDefiningEnvironment() { return definingEnvironment; }

    public void setIntValue(int intValue) { this.intValue = intValue; }
    public void setRealValue(double realValue) { this.realValue = realValue; }
    public void setBoolValue(boolean boolValue) { this.boolValue = boolValue; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    public void setWord(String word) { this.word = word; }
    public void setArrayValue(Object[] arrayValue) { this.arrayValue = arrayValue; }
    public void setCharValue(char charValue) { this.charValue = charValue; }
    public void setDefiningEnvironment(Environment env) { this.definingEnvironment = env; }

    public Object getValue(Type type) {
        return switch (type) {
            case INT -> getIntValue(); 
            case REAL -> getRealValue();
            case STRING -> getWord();
            case BOOLEAN -> getBoolValue();
            case CHAR -> getCharValue();
            case ARRAY -> getArrayValue();
            default -> null;
        };
    }

    //-------- toString -----------
    public String toString() {
        if (type == INT && intValue != null)
            return "\n (line " + lineNumber + ") Type: " + type + ", value: " + intValue;
        else if (type == REAL && realValue != null)
            return "\n (line " + lineNumber + ") Type: " + type + ", value: " + realValue;
        else if (type == STRING && word != null)
            return "\n (line " + lineNumber + ") Type: " + type + ", value: " + word;
        else if (type == BOOLEAN && boolValue != null)
            return "\n (line " + lineNumber + ") Type: " + type + ", value: " + boolValue;
        else if (type == ARRAY && arrayValue != null)
            return "\n (line " + lineNumber + ") Type: " + type + ", value: " + Arrays.toString(arrayValue);
        else if (type == IDENTIFIER && word != null) 
            return "\n(line " + lineNumber + ") Type: " + type + ", value: " + word;
        else if (type == CHAR & charValue != null)
            return "\n(line " + lineNumber + ") Type: " + type + ", value: " + charValue;
        else return "\n(line " + lineNumber + ") Type: " + type;
    }

    // --------------- Printing Lexemes as Parse Trees ---------------
    public void printAsParseTree() {
        System.out.println(getPrintableTree(this, 0));
    }

    // ------------- toValueOnlyString -------------
    public String toValueOnlyString() {
        if(type == INT) return intValue.toString();
        else if(type == REAL) return realValue.toString();
        else if(type == BOOLEAN) return boolValue.toString();
        else if(type == STRING) return word;
        else if(type == ARRAY) return arrayValue.toString();
        else if(type == IDENTIFIER) return word;
        else return null;
    }
 
 
 private static String getPrintableTree(Lexeme root, int level) {
    if (root == null) return "(Empty ParseTree)";
    StringBuilder treeString = new StringBuilder(root.toString());
 
    StringBuilder spacer = new StringBuilder("\n");
    spacer.append("\t".repeat(level));
 
    int numChildren = root.children.size();
    if (numChildren > 0) {
        treeString.append(" (with ").append(numChildren).append(numChildren == 1 ? " child):" : " children):");
        for (int i = 0; i < numChildren; i++) {
            Lexeme child = root.getChild(i);
            treeString
                    .append(spacer).append("(").append(i + 1).append(") ")
                    .append(getPrintableTree(child, level + 1));
        }
    }
 
    return treeString.toString();
 }
}
