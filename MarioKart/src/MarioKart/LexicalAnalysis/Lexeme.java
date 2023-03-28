package MarioKart.LexicalAnalysis;

import java.util.ArrayList;
import java.util.Arrays;

import static MarioKart.LexicalAnalysis.Type.*;
public class Lexeme {
    //Instance Variables
    private final Type type;
    private Integer lineNumber;
    private ArrayList<Lexeme> children;

    //------Instance Variable Declaration------
    private String word;
    private Integer intValue;
    private Double realValue;
    private Boolean boolValue;
    private Object[] arrayValue;

    //---------Helper Functions-----------
    public void addChild(Lexeme newChild){
        children.add(newChild);
    }
    public void addChildren(ArrayList<Lexeme> newChildren){
        children.addAll(newChildren);
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
    public int getIntValue() {
        return intValue;
    }
    public double getRealValue() {
        return realValue;
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
    public Type getType() {
        return type;
    }
    public Object[] getArrayValue() {
        return arrayValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    public void setRealValue(double realValue) {
        this.realValue = realValue;
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
    public void setArrayValue(Object[] arrayValue) {
        this.arrayValue = arrayValue;
    }

    //--------toString-----------
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
        else if (type == IDENTIFIER && word != null) {
            return "\n(line " + lineNumber + ") Type: " + type + ", value: " + word;
       }
        else return "\n(line " + lineNumber + ") Type: " + type;
    }
}
