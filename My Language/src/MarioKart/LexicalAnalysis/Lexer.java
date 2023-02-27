package MarioKart.LexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import MarioKart.MarioKart;

public class Lexer {
    //--------------------- Instance Variables ----------------------
    private final String source;
    private final ArrayList<Lexeme> lexemes = new ArrayList<>();
    
    private int currentPosition = 0;
    private int startOfCurrentLexeme = 0;
    private int lineNumber = 1;
    private final HashMap<String, Types> keywords;


    //--------------------- getKeywords() ---------------------------
    private HashMap<String, Types> getKeywords() {
        HashMap<String, Types> keywords = new HashMap<>();

        keywords.put("requires", Types.REQUIRES);
        keywords.put("summon", Types.SUMMON);
        keywords.put("using", Types.USING);
        keywords.put("garage", Types.GARAGE);

        keywords.put("first", Types.FIRST);
        keywords.put("second", Types.SECOND);
        keywords.put("third", Types.THIRD);
        keywords.put("fourth", Types.FOURTH);
        keywords.put("fifth", Types.FIFTH);
        keywords.put("sixth", Types.SIXTH);
        keywords.put("seventh", Types.SEVENTH);
        keywords.put("eighth", Types.EIGHTH);
        keywords.put("ninth", Types.NINTH);
        keywords.put("tenth", Types.TENTH);
        keywords.put("eleventh", Types.ELEVENTH);
        keywords.put("twelfth", Types.TWELFTH);
        keywords.put("last", Types.LAST);

        keywords.put("int", Types.INT);
        keywords.put("string", Types.STRING);
        keywords.put("boolean", Types.BOOLEAN);

        keywords.put("(", Types.OPEN_PARENTHESIS);
        keywords.put(")", Types.CLOSED_PARENTHESIS);
        keywords.put("[", Types.OPEN_BRACKET);
        keywords.put("]", Types.CLOSED_BRACKET);
        keywords.put(";", Types.SEMICOLON);

        keywords.put("+", Types.PLUS);
        keywords.put("-", Types.MINUS);
        keywords.put("*", Types.TIMES);
        keywords.put("/", Types.DIVIDED_BY);
        keywords.put("%", Types.MOD);
        keywords.put("+=", Types.PLUS_EQUALS);
        keywords.put("-=", Types.MINUS_EQUALS);
        keywords.put("*=", Types.TIMES_EQUALS);
        keywords.put("/=", Types.DIVIDED_EQUALS);
        keywords.put("++", Types.PLUS_PLUS);
        keywords.put("--", Types.MINUS_MINUS);
        keywords.put("!", Types.NOT);
        keywords.put("==", Types.EQUALS_EQUALS_);
        keywords.put("!=", Types.NOT_EQUALS);
        keywords.put(">", Types.GREATER_THAN);
        keywords.put("<", Types.LESS_THAN);
        keywords.put(">=", Types.GREATER_THAN_OR_EQUAL_TO);
        keywords.put("<+=", Types.LESS_THAN_OR_EQUAL_TO);
        keywords.put("&&", Types.AND);
        keywords.put("||", Types.OR);


        return keywords;
    }

    //---------- Constructor --------
    public Lexer(String source) {
        this.source = source;
        this.keywords = getKeywords();
        this.currentPosition = 0;
        this.startOfCurrentLexeme = 0;
        this.lineNumber = 1;
    }

    //---------------------- Helper Methods -----------------------
    private boolean isAtEnd() {
        return currentPosition >= source.length();
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(currentPosition);
    } 
    private char peekNext() {
        if(currentPosition + 1 >= source.length()) return '\0';
        return source.charAt(currentPosition + 1);
    }
    
    private boolean match(char expected) {
        if(isAtEnd() || source.charAt(currentPosition) != expected ) return false;
        currentPosition++;
        return true;
    } 
    //question for mr griest - what was that one 
    //use case for the peekTwoAhead() method?

    private char advance() {
        char currentChar = source.charAt(currentPosition);
        if(currentChar == '\n' || currentChar == '\r') lineNumber++;
        currentPosition++;
        return currentChar;
    }

    // -------------------- Supplemental Helper Methods --------------------
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') ||
               (c >= 'a' && c <= 'z') ||
               (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // --------------------- Error Reporting -----------------------
    private void error(String message) {
        MarioKart.syntaxError(message, lineNumber);
    }
}
