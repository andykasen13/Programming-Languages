package MarioKart.Recognizer;

import MarioKart.LexicalAnalysis.Lexeme;
import MarioKart.LexicalAnalysis.Types;
import MarioKart.MarioKart;

import java.util.ArrayList;

import static MarioKart.LexicalAnalysis.Types.*;

public class Recognizer {
    private static final boolean printDebugMessages = true;
    // ------------------- Instance Variables --------------------
    private final ArrayList<Lexeme> lexemes;
    private Lexeme currentLexeme;
    private int nextLexemeIndex;

    // ------------------- Core Support Methods ---------------------
    //get the current lexeme's type
    private Types peek() { return currentLexeme.getType(); }

    //get the next lexeme's type
    private Types peekNext() {
        if(nextLexemeIndex >= lexemes.size()) return null;
        else return lexemes.get(nextLexemeIndex).getType();
    }

    //check the current lexeme's type against a parameter
    private boolean check(Types type) { return currentLexeme.getType() == type; }

    //check the next lexeme's type against a parameter
    private boolean checkNext(Types type) {
        if(nextLexemeIndex > lexemes.size()) return false;
        return lexemes.get(nextLexemeIndex).getType() == type;
    }

    //move forward a lexeme
    private Lexeme consume(Types expectedType) {
        if(check(expectedType)) return advance();

        error("Expected Type " + expectedType + " but found " + currentLexeme + ".");
        return new Lexeme(currentLexeme.getLineNumber(), ERROR);
    }

    private Lexeme advance() {
        Lexeme toReturn = currentLexeme;
        currentLexeme = lexemes.get(nextLexemeIndex);
        nextLexemeIndex++;
        return toReturn;
    }

    // ------------------- Constructor --------------------
    public Recognizer(ArrayList<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.nextLexemeIndex = 0;
        advance();
    }

    // ------------------- Consumption Functions --------------------
    // ------------------- Pending Functions --------------------
    // ------------------- Grouped Type-Enumeration --------------------
    // ------------------- Error Reporting --------------------
    private Lexeme error(String message) {
        MarioKart.syntaxError(message, currentLexeme);
        return new Lexeme(currentLexeme.getLineNumber(), message, ERROR);
    }

    // ------------------- Debugging  --------------------
}
