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
        program();
    }

    // ------------------- Consumption Functions --------------------
    private void program() {
        statementList();
    }

    private void statementList() {
        while(statementPending()) statement();
    }

    private void block() {
        consume(OPEN_BRACKET);
        statementList();
        consume(CLOSED_BRACKET);
    }

    private void assignment() {
        consume(IDENTIFIER);
        consume(EQUALS);
        expression();
        consume(SEMICOLON);
    }
    private boolean assignmentPending() {
        return check(IDENTIFIER);
    }

    private void expression() {
        if(binaryExpressionPending()) binaryExpression();
        else if(primaryPending()) primary();
        else if(unaryExpressionPending()) unaryExpression();
        else error("this is not an expression! you gave me '" + currentLexeme + "' and idk what this is lol :)");
    }
    private boolean expressionPending() {
        return binaryExpressionPending() || primaryPending() || unaryExpressionPending();
    }

    private void binaryExpression() {
        primary();
        binaryOperator();
        primary();
    }
    private boolean binaryExpressionPending() {
        return primaryPending();
    }

    private void functionDefinition() {
        dataType();
        consume(IDENTIFIER);
        consume(OPEN_PARENTHESIS);
        while(dataTypePending()) {
            dataType();
            consume(IDENTIFIER);
        }
        consume(CLOSED_PARENTHESIS);
        block();
    }
    private boolean functionDefinitionPending() {
        return dataTypePending();
    }

    private void statement() {
        if(functionCallPending()) functionCall();
        else if(assignmentPending()) assignment();
        else if(conditionalBlockPending()) conditionalBlock();
        else if(loopStatementPending()) loopStatement();
        else if(functionDefinitionPending()) functionDefinition();
        else if(initializationPending()) initialization();
        else if(variableDeclarationPending()) variableDeclaration();
        else { error("Malformed statement. Error at: '" + currentLexeme + "'."); }
    }
    private boolean statementPending() {
        return functionCallPending() || assignmentPending() || conditionalBlockPending() || loopStatementPending() || functionDefinitionPending() || initializationPending() || variableDeclarationPending();
    }

    private void variableDeclaration() {
        dataType();
        consume(IDENTIFIER);
        consume(SEMICOLON);
    }
    private boolean variableDeclarationPending() {
        return dataTypePending() && checkNext(IDENTIFIER);
    }

    private void initialization() {
        dataType();
        consume(IDENTIFIER);
        consume(EQUALS);
        primary();
        consume(SEMICOLON);
    }
    private boolean initializationPending() {
        return dataTypePending();
    }

    private void loopStatement() {
        consume(TIMETRIAL);
        if(forStatementPending()) forStatement();
        else if(whileStatementPending()) whileStatement();
        else { error("Malformed loop. Error at: '" + currentLexeme + "'."); }
    }
    private boolean loopStatementPending() {
        return forStatementPending() || whileStatementPending();
    }

    private void forStatement() {
        consume(FROM);
        consume(LAP);
        consume(INT);
        consume(TO);
        consume(INT);
        consume(BY);
        consume(INT);
        block();
    }
    private boolean forStatementPending() {
        return check(FROM);
    }

    private void whileStatement() {
        consume(TO);
        consume(LAP);
        consume(INT);
        block();
    }
    private boolean whileStatementPending() {
        return check(TO);
    }

    private void conditionalBlock() {
        ifStatement();
        while(elseIfStatementPending()) elseIfStatement();
        if(elseIfStatementPending()) elseStatement();
    }
    private boolean conditionalBlockPending() {
        return ifStatementPending();
    }

    private void ifStatement() {
        consume(FIRST);
        consume(OPEN_PARENTHESIS);
        expression();
        consume(CLOSED_PARENTHESIS);
        block();
    }
    private boolean ifStatementPending() {
        return check(FIRST);
    }

    private void elseIfStatement() {
        if(check(SECOND)) {
            consume(SECOND);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(THIRD)) {
            consume(THIRD);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(FOURTH)) {
            consume(FOURTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(FIFTH)) {
            consume(FIFTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(SIXTH)) {
            consume(SIXTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(SEVENTH)) {
            consume(SEVENTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(EIGHTH)) {
            consume(EIGHTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(NINTH)) {
            consume(NINTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else if(check(TENTH)) {
            consume(TENTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }

        //Test
        else if(check(ELEVENTH)) {
            consume(ELEVENTH);
            consume(OPEN_PARENTHESIS);
            expression();
            consume(CLOSED_PARENTHESIS);
            block();
        }
        else { error("Malformed else-if statement. Error at: '" + currentLexeme + "'."); }
    }
    private boolean elseIfStatementPending() {
        return check(SECOND) || check(THIRD) || check(FOURTH) || check(FIFTH) || check(SIXTH) || check(SEVENTH) || check(EIGHTH) || check(NINTH) || check(TENTH) || check(ELEVENTH);
    }

    private void elseStatement() {
        if(check(TWELFTH)) {
            consume(TWELFTH);
            block();
        }
        else if(check(LAST)) {
            consume(LAST);
            block();
        }
    }
    private boolean elseStatementPending() {
        return check(TWELFTH) || check(LAST);
    }


    private void primary() {
        if(check(INT)) consume(INT);
        else if(check(REAL)) consume(REAL);
        else if(check(STRING)) consume(STRING);
        else if(check(IDENTIFIER)) consume(IDENTIFIER);
        else if(booleanLiteralPending()) booleanLiteral();
        else if(functionCallPending()) functionCall();
        else if(parenthesizedExpressionPending()) parenthesizedExpression();
        else { error("Malformed primary. Error at: '" + currentLexeme + "'."); }
    }
    private boolean primaryPending()  {
        return check(INT) || check(REAL) || check(STRING) || check(IDENTIFIER) || booleanLiteralPending() || functionCallPending() || parenthesizedExpressionPending();
    }

    private void functionCall() {
        consume(IDENTIFIER);
        consume(OPEN_PARENTHESIS);
        while(parameterPending()) parameter();
        consume(CLOSED_PARENTHESIS);
        consume(SEMICOLON);
    }
    private boolean functionCallPending() {
        return check(IDENTIFIER);
    }

    private void parameter() {
        while(dataTypePending()) {
            dataType();
            consume(IDENTIFIER);
        }
    }
    private boolean parameterPending() {
        return dataTypePending();
    }

    private void dataType() {
        if(check(STRING)) consume(STRING);
        else if(check(INT)) consume(INT);
        else if(check(REAL)) consume(REAL);
        else if(check(BOOLEAN)) consume(BOOLEAN);
        else if(check(CHAR)) consume(CHAR);
        else { error("Malformed data type. Error at: '" + currentLexeme + "'."); }
    }
    private boolean dataTypePending() {
        return check(STRING) || check(INT) || check(REAL) || check(BOOLEAN) || check(CHAR);
    }

    private void parenthesizedExpression() {
        consume(OPEN_PARENTHESIS);
        while(expressionPending()) expression();
        consume(CLOSED_PARENTHESIS);
        consume(SEMICOLON);
    }
    private boolean parenthesizedExpressionPending() {
        return check(OPEN_PARENTHESIS);
    }

    private void booleanLiteral() {
        if(check(FALSE)) consume(FALSE);
        else if(check(TRUE)) consume(TRUE);
        else { error("Malformed boolean. Error at: '" + currentLexeme + "'."); }
    }
    private boolean booleanLiteralPending() {
        return check(TRUE) || check(FALSE);
    }

    private void unaryExpression() {
        if(primaryPending()) {
            primary();
            unaryPostOperator();
            consume(SEMICOLON);
        }
        else if(unaryPreOperatorPending()) {
            unaryPreOperator();
            primary();
            consume(SEMICOLON);
        }
        else { error("Malformed expression. Error at: '" + currentLexeme + "'."); }
    }
    private boolean unaryExpressionPending() {
        return primaryPending() || unaryPreOperatorPending();
    }

    private void unaryPostOperator() {
        if(check(PLUS_PLUS)) consume(PLUS_PLUS);
        else if(check(MINUS_MINUS)) consume(MINUS_MINUS);
        else { error("Malformed Unary Post Operator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean unaryPostOperatorPending() {
        return check(PLUS_PLUS) || check(MINUS_MINUS);
    }

    private void unaryPreOperator() {
        if(check(NOT)) consume(NOT);
        else if(check(MINUS)) consume(MINUS);
        else { error("Malformed Unary Pre Operator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean unaryPreOperatorPending() {
        return check(NOT) || check(MINUS);
    }

    private void binaryOperator() {
        if(simpleMathOperatorPending()) simpleMathOperator();
        else if(comparatorPending()) comparator();
        else if(booleanOperatorsPending()) booleanOperators();
        else { error("Malformed binary operator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean binaryOperatorPending() {
        return simpleMathOperatorPending() || comparatorPending() || booleanOperatorsPending();
    }

    private void booleanOperators() {
        if(check(AND)) consume(AND);
        else if(check(OR)) consume(OR);
        else { error("Malformed boolean operator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean booleanOperatorsPending() {
        return check(AND) || check(OR);
    }

    private void simpleMathOperator() {
        if(check(PLUS)) consume(PLUS);
        else if(check(MINUS)) consume(MINUS);
        else if(check(TIMES)) consume(TIMES);
        else if(check(DIVIDED_BY)) consume(DIVIDED_BY);
        else if(check(MOD)) consume(MOD);
        else { error("Malformed simple math operator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean simpleMathOperatorPending() {
        return check(PLUS) || check(MINUS) || check(TIMES) || check(DIVIDED_BY) || check(MOD);
    }

    private void comparator() {
        if(check(GREATER_THAN)) consume(GREATER_THAN);
        else if(check(LESS_THAN)) consume(LESS_THAN);
        else if(check(GREATER_THAN_OR_EQUAL_TO)) consume(GREATER_THAN_OR_EQUAL_TO);
        else if(check(LESS_THAN_OR_EQUAL_TO)) consume(LESS_THAN_OR_EQUAL_TO);
        else if(check(EQUALS_EQUALS)) consume(EQUALS_EQUALS);
        else if(check(NOT_EQUALS)) consume(NOT_EQUALS);
        else { error("Malformed comparator. Error at: '" + currentLexeme + "'."); }
    }
    private boolean comparatorPending() {
        return check(GREATER_THAN_OR_EQUAL_TO) || check(LESS_THAN_OR_EQUAL_TO) || check(GREATER_THAN) || check(LESS_THAN) || check(EQUALS_EQUALS) || check(NOT_EQUALS);
    }


    // ------------------- Pending Functions --------------------
    // ------------------- Grouped Type-Enumeration --------------------
    // ------------------- Error Reporting --------------------
    private Lexeme error(String message) {
        MarioKart.syntaxError(message, currentLexeme);
        return new Lexeme(currentLexeme.getLineNumber(), message, ERROR);
    }

    // ------------------- Debugging  --------------------
    private static void log(String message) {
        if(printDebugMessages) System.out.println(message);
    }

    private static void logHeading(String heading) {
        if(printDebugMessages) System.out.println("--------------" + heading + "------------");
    }
}
