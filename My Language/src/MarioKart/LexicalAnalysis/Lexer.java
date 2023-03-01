package MarioKart.LexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import MarioKart.MarioKart;
import static MarioKart.LexicalAnalysis.Types.*;

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

        keywords.put("requires", REQUIRES);
        keywords.put("summon", SUMMON);
        keywords.put("using", USING);
        keywords.put("garage", GARAGE);

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

        keywords.put("int", INT);
        keywords.put("string", STRING);
        keywords.put("boolean", BOOLEAN);

        keywords.put("(", OPEN_PARENTHESIS);
        keywords.put(")", CLOSED_PARENTHESIS);
        keywords.put("[", OPEN_BRACKET);
        keywords.put("]", CLOSED_BRACKET);
        keywords.put(";", SEMICOLON);

        keywords.put("+", PLUS);
        keywords.put("-", MINUS);
        keywords.put("*", TIMES);
        keywords.put("/", DIVIDED_BY);
        keywords.put("%", MOD);
        keywords.put("+=", PLUS_EQUALS);
        keywords.put("-=", MINUS_EQUALS);
        keywords.put("*=", TIMES_EQUALS);
        keywords.put("/=", DIVIDED_EQUALS);
        keywords.put("++", PLUS_PLUS);
        keywords.put("--", MINUS_MINUS);
        keywords.put("!", NOT);
        keywords.put("==", EQUALS_EQUALS_);
        keywords.put("!=", NOT_EQUALS);
        keywords.put(">", GREATER_THAN);
        keywords.put("<", LESS_THAN);
        keywords.put(">=", GREATER_THAN_OR_EQUAL_TO);
        keywords.put("<+=", LESS_THAN_OR_EQUAL_TO);
        keywords.put("&&", AND);
        keywords.put("||", OR);

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
            case '.' -> { return new Lexeme(lineNumber, DOT); }
            case ',' -> { return new Lexeme(lineNumber, COMMA); }
            case '!' -> { return new Lexeme(lineNumber, NOT); }
            case '%' -> { return new Lexeme(lineNumber, MOD); }
            case '"' -> { lexString(); }
            case '1' -> { lexNumber(); }
            case '2' -> { lexNumber(); }
            case '3' -> { lexNumber(); }
            case '4' -> { lexNumber(); }
            case '5' -> { lexNumber(); }
            case '6' -> { lexNumber(); }
            case '7' -> { lexNumber(); }
            case '8' -> { lexNumber(); }
            case '9' -> { lexNumber(); }
            case '0' -> { lexNumber(); }

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
            case '=' -> { return new Lexeme(lineNumber, match('=') ? EQUALS_EQUALS_ : EQUALS); }
            case '*' -> { return new Lexeme(lineNumber, match('=') ? TIMES_EQUALS : TIMES); }
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

    private Lexeme lexNumber() {
        boolean isInteger = true;
        while(isDigit(peek())) advance();

        if (peek() == '.') {
            isInteger = false;

            if(!isDigit(peek())) {
                String malformedReal = source.substring(startOfCurrentLexeme, currentPosition + 1);
                error("that isn't a number!!! silly goose. your failure: '" + malformedReal + "'");
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
        Types type = keywords.get(text);

        if (type == null) return new Lexeme(lineNumber, text, IDENTIFIER);

        else if(type == TRUE) return new Lexeme(lineNumber, true, TRUE);
        else if(type == FALSE) return new Lexeme(lineNumber, false, FALSE);

        return new Lexeme(lineNumber, type);
    }

    private Lexeme lexComments() { 
        int currentLineNumber = lineNumber;
        while(currentLineNumber == lineNumber) advance();
        return new Lexeme(lineNumber, source.substring(startOfCurrentLexeme, currentPosition), COMMENT);
    }


}
