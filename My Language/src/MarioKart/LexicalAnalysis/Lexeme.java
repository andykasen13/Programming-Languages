package MarioKart.LexicalAnalysis;

import java.util.Arrays;

import static MarioKart.LexicalAnalysis.Types.*;
public class Lexeme {
    //Instance Variables
    private final Types type;
    private int lineNumber;

    //------Instance Variable Declaration------
    private String word;
    private Integer intValue;
    private Double realValue;
    private Boolean boolValue;
    private Object[] arrayValue;

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

    public Lexeme(int lineNumber, Object[] arrayValue, Types type) {
        this.lineNumber = lineNumber;
        this.arrayValue = arrayValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, double realValue, Types type) {
        this.lineNumber = lineNumber;
        this.realValue = realValue;
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
    public Types getType() {
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
        else return "\n(line " + lineNumber + ") Type: " + type;
    }
}
