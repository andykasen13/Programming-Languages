package MarioKart.LexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import MarioKart.MarioKart;
import static MarioKart.LexicalAnalysis.Type.*;

public class Lexer {
    //--------------------- Instance Variables ----------------------
    private final String source;
    private final ArrayList<Lexeme> lexemes = new ArrayList<>();
    
    private int currentPosition = 0;
    private int startOfCurrentLexeme = 0;
    private int lineNumber = 1;
    private final HashMap<String, Type> keywords;


    //--------------------- getKeywords() ---------------------------
    private HashMap<String, Type> getKeywords() {
        HashMap<String, Type> keywords = new HashMap<>();

        keywords.put("requires", REQUIRES);
        keywords.put("summon", SUMMON);
        keywords.put("using", USING);
        keywords.put("garage", GARAGE);
        keywords.put("from", FROM);
        keywords.put("to", TO);
        keywords.put("return", RETURN);
        keywords.put("timeTrial", TIMETRIAL);

        keywords.put("first", FIRST);
        keywords.put("second", SECOND);
        keywords.put("third", THIRD);
        keywords.put("fourth", FOURTH);
        keywords.put("fifth", FIFTH);
        keywords.put("sixth", SIXTH);
        keywords.put("seventh", SEVENTH);
        keywords.put("eighth", EIGHTH);
        keywords.put("ninth", NINTH);
        keywords.put("tenth", TENTH);
        keywords.put("eleventh", ELEVENTH);
        keywords.put("twelfth", TWELFTH);
        keywords.put("last", LAST);

        keywords.put("tiesWith", EQUALS);
        keywords.put("boost", PLUS);
        keywords.put("slip", MINUS);
        keywords.put("superStar", TIMES);
        keywords.put("blueShell", DIVIDED_BY);
        keywords.put("multiMushroom", PLUS_EQUALS);
        keywords.put("redShell", MINUS_EQUALS);
        keywords.put("goldenMushroom", TIMES_EQUALS);
        keywords.put("fallOffTheMap", DIVIDED_EQUALS);
        keywords.put("overtake", PLUS_PLUS);
        keywords.put("getPassed", MINUS_MINUS);
        keywords.put("not", NOT);
        keywords.put("isTiedWith", EQUALS_EQUALS);
        keywords.put("isNotTiedWith", NOT_EQUALS);
        keywords.put("isFurtherThan", GREATER_THAN);
        keywords.put("isBetterThan", GREATER_THAN_OR_EQUAL_TO);
        keywords.put("isBehind", LESS_THAN);
        keywords.put("isWorseThan", LESS_THAN_OR_EQUAL_TO);
        keywords.put("and", AND);
        keywords.put("or", OR);

        keywords.put("int", INT);
        keywords.put("real", REAL);
        keywords.put("boolean", BOOLEAN);
        keywords.put("string", STRING);
        keywords.put("array", ARRAY);

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

    //----------------------- lex() function ----------------------------
    public ArrayList<Lexeme> lex() {
        while(!isAtEnd()) {
            startOfCurrentLexeme = currentPosition;
            Lexeme nextLexeme = getNextLexeme();
            if(nextLexeme != null) lexemes.add(nextLexeme);
        }
        
        lexemes.add(new Lexeme(lineNumber, END_OF_FILE));
        return lexemes;
    }

    // --------------------- lex Helper Functions ---------------------------
    private Lexeme getNextLexeme() {
        char c = advance();
        switch(c) {
            //Ignore whitespace
            case ' ', '\t', '\n', '\r' -> {
                return null;
            }
            
            //one digit
            case '[' -> { return new Lexeme(lineNumber, OPEN_BRACKET); }
            case ']' -> { return new Lexeme(lineNumber, CLOSED_BRACKET); }
            case '(' -> { return new Lexeme(lineNumber, OPEN_PARENTHESIS); }
            case ')' -> { return new Lexeme(lineNumber, CLOSED_PARENTHESIS); }
            case '{' -> { return new Lexeme(lineNumber, OPEN_CURLY); }
            case '}' -> { return new Lexeme(lineNumber, CLOSED_CURLY); }
            case ';' -> { return new Lexeme(lineNumber, SEMICOLON); }
            case '.' -> { return new Lexeme(lineNumber, DOT); }
            case ',' -> { return new Lexeme(lineNumber, COMMA); }
            case '!' -> { return new Lexeme(lineNumber, NOT); }
            case '%' -> { return new Lexeme(lineNumber, MOD); }
            case '"' -> { return lexString(); }
            case '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> { return lexNumber(); }

            //two digits
            case '&' -> { if(match('&')) return new Lexeme(lineNumber, AND); }
            case '|' -> { if(match('|')) return new Lexeme(lineNumber, OR); }

            //one OR two digits :(
            case '+' -> {
                if(match('+')) return new Lexeme(lineNumber, PLUS_PLUS);
                else if(match('=')) return new Lexeme(lineNumber, PLUS_EQUALS);
                else return new Lexeme(lineNumber, PLUS);
            }
            case '-' -> {
                if(match('-')) return new Lexeme(lineNumber, MINUS_MINUS);
                else if(match('=')) return new Lexeme(lineNumber, PLUS_EQUALS);
                else return new Lexeme(lineNumber, MINUS);
            }
            case '/' -> {
                if(match('/')) lexComments();
                else if(match('=')) return new Lexeme(lineNumber, DIVIDED_EQUALS);
                else return new Lexeme(lineNumber, DIVIDED_BY);
            }

            //only two options
            case '>' -> { return new Lexeme(lineNumber, match('=') ? GREATER_THAN_OR_EQUAL_TO : GREATER_THAN); }
            case '<' -> { return new Lexeme(lineNumber, match('=') ? LESS_THAN_OR_EQUAL_TO : LESS_THAN); }
            case '=' -> { return new Lexeme(lineNumber, match('=') ? EQUALS_EQUALS : EQUALS); }
            case '*' -> { return new Lexeme(lineNumber, match('=') ? TIMES_EQUALS : TIMES); }

            //put NOTHING beneath these cases pls
            default -> {
                if(isDigit(c)) return lexNumber();
                else if(isAlpha(c)) return lexIdentifierOrKeyword();
                else {
                    error("bro what is '" + c + "'. i do not recognize it. but then again i dont recognize half of what goes on in my calc class sooooooo");
                    return null;
                }
            }
        }
        return null;
    }

    private Lexeme lexString() {
        while( !(isAtEnd() || peek() == '"')) advance();

        String str = source.substring(startOfCurrentLexeme + 1, currentPosition);

        if(isAtEnd()) error("you didn't close your string :(. String: '" + str + "'");
        else advance();

        return new Lexeme(lineNumber, str, STRING);
    }

    private void lexComments() {
        while( !(isAtEnd() || peek() == '\n')) advance();

    }

    private Lexeme lexNumber() {
        boolean isInteger = true;
        while(isDigit(peek())) advance();

        //checking if int or real
        if (peek() == '.') {
            isInteger = false;
            if(!isDigit(peekNext())) {
                String malformedReal = source.substring(startOfCurrentLexeme, currentPosition + 2);
                error("that isn't a number!!! silly goose. your failure: '" + malformedReal + "'");
                return null;
            }
            advance();
            while(isDigit(peek())) advance();
        }

        String numberString = source.substring(startOfCurrentLexeme, currentPosition);
        if(isInteger) { 
            int intValue = Integer.parseInt(numberString);
            return new Lexeme(lineNumber, intValue, INT);
        } else {
            double realValue = Double.parseDouble(numberString);
            return new Lexeme(lineNumber, realValue, REAL);
        }

    }

    private Lexeme lexIdentifierOrKeyword() {
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(startOfCurrentLexeme, currentPosition);
        Type type = keywords.get(text);

        if (type == null) return new Lexeme(lineNumber, text, IDENTIFIER);

        else if(type == TRUE) return new Lexeme(lineNumber, true, TRUE);
        else if(type == FALSE) return new Lexeme(lineNumber, false, FALSE);

        return new Lexeme(lineNumber, type);
    }
}
