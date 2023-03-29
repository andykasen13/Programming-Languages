package MarioKart.Parser;

import MarioKart.LexicalAnalysis.Lexeme;
import MarioKart.LexicalAnalysis.Type;
import MarioKart.MarioKart;

import java.util.ArrayList;

import static MarioKart.LexicalAnalysis.Type.*;

public class Parser {
    private static final boolean printDebugMessages = true;
    // ------------------- Instance Variables --------------------
    private final ArrayList<Lexeme> lexemes;
    private Lexeme currentLexeme;
    private int nextLexemeIndex;

    // ------------------- Core Support Methods ---------------------
    //get the current lexeme's type
    private Type peek() { return currentLexeme.getType(); }

    //get the next lexeme's type
    private Type peekNext() {
        if(nextLexemeIndex >= lexemes.size()) return null;
        else return lexemes.get(nextLexemeIndex).getType();
    }

    //check the current lexeme's type against a parameter
    private boolean check(Type type) { return currentLexeme.getType() == type; }

    //check the next lexeme's type against a parameter
    private boolean checkNext(Type type) {
        if(nextLexemeIndex > lexemes.size()) return false;
        return lexemes.get(nextLexemeIndex).getType() == type;
    }

    //check the following lexeme's type against a parameter
    private boolean checkTwoNext(Type type) {
        if(nextLexemeIndex + 1 > lexemes.size()) return false;
        else return lexemes.get(nextLexemeIndex + 1).getType() == type;
    }

    //move forward a lexeme
    private Lexeme consume(Type expectedType) {
        if(check(expectedType)) return advance();
        System.out.println(currentLexeme);
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
    public Parser(ArrayList<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.nextLexemeIndex = 0;
        advance();
        program();
    }

    // ------------------- Consumption Functions --------------------
    public Lexeme program() {
        log("program");
        if(statementListPending()) return statementList();
        else return(new Lexeme(EMPTY_PROGRAM));
    }

    private Lexeme statementList() { //complete
        log("statement_list");
        Lexeme statementList = new Lexeme(STATEMENT_LIST, -1); //declare a new lexeme, but give it a fake type and a fake line number, just for now as i dont know what to do in this situation
        while(statementPending()) statementList.addChild(statement());
        return statementList;
    }
    private boolean statementListPending() { //complete
        return statementPending();
    }

    private Lexeme block() { //complete
        log("block");
        Lexeme block = consume(OPEN_BRACKET);
        block.addChild(statementList());
        consume(CLOSED_BRACKET);
        return block;
    }

    private Lexeme assignment() { //complete
        log("assignment");
        Lexeme assignment = consume(IDENTIFIER);
        consume(EQUALS);
        assignment.addChild(expression());
        consume(SEMICOLON);
        
        return assignment;
    }
    private boolean assignmentPending() {
        return check(IDENTIFIER);
    }

    private Lexeme expression() { //complete
        log("expression");
        Lexeme expression;
        if(binaryExpressionPending()) expression = (binaryExpression());
        else if(primaryPending()) expression = (primary());
        else if(unaryExpressionPending()) expression = (unaryExpression());
        else {
            error("this is not an expression! you gave me '" + currentLexeme + "' and idk what this is lol :)");
            expression = null;
        }
        return expression;
    }
    private boolean expressionPending() {
        return binaryExpressionPending() || primaryPending() || unaryExpressionPending();
    }

    private Lexeme binaryExpression() { //complete
        log("binary expression");
        Lexeme binaryExpression = new Lexeme(BINARY_EXPRESSION);
        binaryExpression.addChild(primary());
        binaryExpression.addChild(binaryOperator());
        binaryExpression.addChild(primary());
        return binaryExpression;
    }
    private boolean binaryExpressionPending() {
        return primaryPending();
    }

    private Lexeme functionDefinition() { //INCOMPLEET WHY WHAT DO I DO HELP
        log("function_definition");
        Lexeme functionDefinition = new Lexeme(FUNCTION_DEFINITION);
        functionDefinition.addChild(dataType());
        functionDefinition.addChild(consume(IDENTIFIER));
        consume(REQUIRES);
        functionDefinition.addChild(parameterList());
        functionDefinition.addChild(block());
        return functionDefinition;
    }
    private boolean functionDefinitionPending() {
        return dataTypePending() && checkNext(IDENTIFIER) && checkTwoNext(REQUIRES);
    }

    private Lexeme parameterList() { //INCOPLETE HELP WHY WHAT DO I DO HERE
        log("parameter_list");
        Lexeme parameterList = new Lexeme(PARAMETER_LIST);
        if(parameterListPending()) parameterList.addChild(parameter());
        return parameterList;
    }
    private boolean parameterListPending() {
        return parameterPending();
    }


    private Lexeme statement() { //complete
        log("statement");
        Lexeme statement = null;
        if(functionCallPending()) statement = functionCall();
        else if(assignmentPending()) statement = assignment();
        else if(conditionalBlockPending()) statement = conditionalBlock();
        else if(loopStatementPending()) statement = loopStatement();
        else if(functionDefinitionPending()) statement = functionDefinition();
        else if(initializationPending()) statement = initialization();
        else if(variableDeclarationPending()) statement = variableDeclaration();
        else { error("Malformed statement. Error at: '" + currentLexeme + "'."); }

        return statement;
    }
    private boolean statementPending() {
        return functionCallPending() || assignmentPending() || conditionalBlockPending() || loopStatementPending() || functionDefinitionPending() || initializationPending() || variableDeclarationPending();
    }

    private Lexeme variableDeclaration() { //complete
        log("variable declaration");
        Lexeme variableDeclaration = new Lexeme(VARIABLE_DECLARATION);
        variableDeclaration.addChild(dataType());
        variableDeclaration.addChild(consume(IDENTIFIER));
        consume(SEMICOLON);
        return variableDeclaration;
    }
    private boolean variableDeclarationPending() {
        return dataTypePending() && checkNext(IDENTIFIER);
    }

    private Lexeme initialization() { //complete
        log("initialization");
        Lexeme initialization = new Lexeme(INITIALIZATION);
        initialization.addChild(dataType());
        initialization.addChild(consume(IDENTIFIER));
        consume(EQUALS);
        initialization.addChild(expression());
        consume(SEMICOLON);
        return initialization;
    }
    private boolean initializationPending() {
        return dataTypePending();
    }

    private Lexeme loopStatement() {
        log("loop statement");
        Lexeme loopStatement = new Lexeme(FOR_STATEMENT);
        consume(TIMETRIAL);
        if(forStatementPending()) loopStatement.addChild(forStatement());
        else if(whileStatementPending()) loopStatement.addChild(whileStatement());
        else { error("Malformed loop. Error at: '" + currentLexeme + "'."); }
        return loopStatement;
    }
    private boolean loopStatementPending() {
        return forStatementPending() || whileStatementPending();
    }

    private Lexeme forStatement() {
        log("for statement");
        Lexeme forStatement = new Lexeme(FOR_STATEMENT);
        consume(FROM);
        consume(LAP);
        forStatement.addChild(consume(INT));
        consume(TO);
        forStatement.addChild(consume(INT));
        consume(BY);
        forStatement.addChild(consume(INT));
        forStatement.addChild(block());
        return forStatement;
    }
    private boolean forStatementPending() {
        return check(FROM);
    }

    private Lexeme whileStatement() {
        log("while statement");
        Lexeme whileStatement = new Lexeme(WHILE_STATEMENT);
        consume(TO);
        consume(LAP);
        whileStatement.addChild(consume(INT));
        whileStatement.addChild(block());
        return whileStatement;
    }
    private boolean whileStatementPending() {
        return check(TO);
    }

    private Lexeme conditionalBlock() {
        log("conditional block");
        Lexeme conditionalBlock = new Lexeme(CONDITIONAL_BLOCK);
        conditionalBlock.addChild(ifStatement());
        while(elseIfStatementPending()) conditionalBlock.addChild(elseIfStatement());
        if(elseIfStatementPending()) conditionalBlock.addChild(elseStatement());
        return conditionalBlock;
    }
    private boolean conditionalBlockPending() {
        return ifStatementPending();
    }

    private Lexeme ifStatement() {
        log("if statement");
        Lexeme ifStatement = new Lexeme(IF_STATEMENT);
        consume(FIRST);
        consume(OPEN_PARENTHESIS);
        ifStatement.addChild(expression());
        consume(CLOSED_PARENTHESIS);
        ifStatement.addChild(block());
        return ifStatement;
    }
    private boolean ifStatementPending() {
        return check(FIRST);
    }

    private Lexeme elseIfStatement() { //i am not doing this rn but reminder to come back to this
        log("else if statement");
        Lexeme elseIfStatement = new Lexeme(ELSE_IF_STATEMENT);
        if(check(SECOND)) {
            consume(SECOND);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(THIRD)) {
            consume(THIRD);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(FOURTH)) {
            consume(FOURTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(FIFTH)) {
            consume(FIFTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(SIXTH)) {
            consume(SIXTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(SEVENTH)) {
            consume(SEVENTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(EIGHTH)) {
            consume(EIGHTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(NINTH)) {
            consume(NINTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else if(check(TENTH)) {
            consume(TENTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }

        //Test
        else if(check(ELEVENTH)) {
            consume(ELEVENTH);
            consume(OPEN_PARENTHESIS);
            elseIfStatement.addChild(expression());
            consume(CLOSED_PARENTHESIS);
            elseIfStatement.addChild(block());
        }
        else { error("Malformed else-if statement. Error at: '" + currentLexeme + "'."); }
        return elseIfStatement;
    }
    private boolean elseIfStatementPending() {
        return check(SECOND) || check(THIRD) || check(FOURTH) || check(FIFTH) || check(SIXTH) || check(SEVENTH) || check(EIGHTH) || check(NINTH) || check(TENTH) || check(ELEVENTH);
    }

    private Lexeme elseStatement() {
        log("else statement");
        Lexeme elseStatement = new Lexeme(ELSE_STATEMENT);
        if(check(TWELFTH)) {
            consume(TWELFTH);
            elseStatement.addChild(block());
        }
        else if(check(LAST)) {
            consume(LAST);
            elseStatement.addChild(block());
        }
        else {
            error("Malformed else statement. Error at: '" + currentLexeme + "'.");
            elseStatement = null;
        }
        return elseStatement;
    }
    private boolean elseStatementPending() {
        return check(TWELFTH) || check(LAST);
    }


    private Lexeme primary() {
        log("primary");
        Lexeme primary;
        if(check(INT)) primary = consume(INT);
        else if(check(REAL)) primary = consume(REAL);
        else if(check(STRING)) primary = consume(STRING);
        else if(check(IDENTIFIER)) primary = consume(IDENTIFIER);
        else if(booleanLiteralPending()) primary = booleanLiteral();
        else if(functionCallPending()) primary = functionCall();
        else if(parenthesizedExpressionPending()) primary = parenthesizedExpression();
        else { 
            error("Malformed primary. Error at: '" + currentLexeme + "'."); 
            primary = null;
        }
        return primary;
    }
    private boolean primaryPending()  {
        return check(INT) || check(REAL) || check(STRING) || check(IDENTIFIER) || booleanLiteralPending() || functionCallPending() || parenthesizedExpressionPending();
    }

    private Lexeme functionCall() {
        log("function call");
        Lexeme functionCall = new Lexeme(FUNCTION_CALL);
        functionCall.addChild(consume(IDENTIFIER));
        consume(OPEN_PARENTHESIS);
        while(parameterPending()) functionCall.addChild(parameter());
        consume(CLOSED_PARENTHESIS);
        return functionCall;
    }
    private boolean functionCallPending() {
        return check(IDENTIFIER);
    }

    private Lexeme parameter() {
        log("parameter");
        Lexeme parameter = new Lexeme(PARAMETER);
        parameter.addChild(dataType());
        parameter.addChild(consume(IDENTIFIER));
        if(check(COMMA)) consume(COMMA);
        return parameter;
    }
    private boolean parameterPending() {
        return dataTypePending();
    }

    private Lexeme dataType() {
        log("data type");
        Lexeme dataType;
        if(check(STRING)) dataType = consume(STRING);
        else if(check(INT)) dataType = consume(INT);
        else if(check(REAL)) dataType = consume(REAL);
        else if(check(BOOLEAN)) dataType = consume(BOOLEAN);
        else if(check(CHAR)) dataType = consume(CHAR);
        else { 
            error("Malformed data type. Error at: '" + currentLexeme + "'."); 
            dataType = null;
        }
        return dataType;
    }
    private boolean dataTypePending() {
        return check(STRING) || check(INT) || check(REAL) || check(BOOLEAN) || check(CHAR);
    }

    private Lexeme parenthesizedExpression() {
        log("parenthesized expression");
        Lexeme parenthesizedExpression = consume(OPEN_PARENTHESIS);
        while(expressionPending()) parenthesizedExpression.addChild(expression());
        parenthesizedExpression.addChild(consume(CLOSED_PARENTHESIS));
        return parenthesizedExpression;
    }
    private boolean parenthesizedExpressionPending() {
        return check(OPEN_PARENTHESIS);
    }

    private Lexeme booleanLiteral() {
        log("boolean literal");
        Lexeme booleanLiteral;
        if(check(FALSE)) booleanLiteral = consume(FALSE);
        else if(check(TRUE)) booleanLiteral = consume(TRUE);
        else { 
            error("Malformed boolean. Error at: '" + currentLexeme + "'."); 
            booleanLiteral = null;
        }
        return booleanLiteral;
    }
    private boolean booleanLiteralPending() {
        return check(TRUE) || check(FALSE);
    }

    private Lexeme unaryExpression() {
        log("unary expression");
        Lexeme unaryExpression;
        if(primaryPending()) {
            unaryExpression = primary();
            unaryExpression.addChild(unaryPostOperator());
        }
        else if(unaryPreOperatorPending()) {
            unaryExpression = unaryPreOperator();
            unaryExpression.addChild(primary());
        }
        else { 
            error("Malformed expression. Error at: '" + currentLexeme + "'."); 
            unaryExpression = null;
        }
        return unaryExpression;
    }
    private boolean unaryExpressionPending() {
        return primaryPending() || unaryPreOperatorPending();
    }

    private Lexeme unaryPostOperator() {
        log("unary post operator");
        Lexeme unaryPostOperator;
        if(check(PLUS_PLUS)) unaryPostOperator = consume(PLUS_PLUS);
        else if(check(MINUS_MINUS)) unaryPostOperator = consume(MINUS_MINUS);
        else { 
            error("Malformed Unary Post Operator. Error at: '" + currentLexeme + "'."); 
            unaryPostOperator = null;
        }
        return unaryPostOperator;
    }
    private boolean unaryPostOperatorPending() {
        return check(PLUS_PLUS) || check(MINUS_MINUS);
    }

    private Lexeme unaryPreOperator() {
        log("unary pre operator");
        Lexeme unaryPreOperator = null;
        if(check(NOT)) unaryPreOperator = consume(NOT);
        else if(check(MINUS)) unaryPreOperator = consume(MINUS);
        else { error("Malformed Unary Pre Operator. Error at: '" + currentLexeme + "'."); }
        return unaryPreOperator;
    }
    private boolean unaryPreOperatorPending() {
        return check(NOT) || check(MINUS);
    }

    private Lexeme binaryOperator() {
        log("binary operator");
        Lexeme binaryOperator = null;
        if(simpleMathOperatorPending()) binaryOperator = simpleMathOperator();
        else if(comparatorPending()) binaryOperator = comparator();
        else if(booleanOperatorsPending()) binaryOperator = booleanOperators();
        else { error("Malformed binary operator. Error at: '" + currentLexeme + "'."); }
        return binaryOperator;
    }
    private boolean binaryOperatorPending() {
        return simpleMathOperatorPending() || comparatorPending() || booleanOperatorsPending();
    }

    private Lexeme booleanOperators() {
        log("boolean operator");
        Lexeme booleanOperators = null;
        if(check(AND)) booleanOperators = consume(AND);
        else if(check(OR)) booleanOperators = consume(OR);
        else { error("Malformed boolean operator. Error at: '" + currentLexeme + "'."); }
        return booleanOperators;
    }
    private boolean booleanOperatorsPending() {
        return check(AND) || check(OR);
    }

    private Lexeme simpleMathOperator() {
        log("simple math operator");
        Lexeme simpleMathOperator = null;
        if(check(PLUS)) simpleMathOperator = consume(PLUS);
        else if(check(MINUS)) simpleMathOperator = consume(MINUS);
        else if(check(TIMES)) simpleMathOperator = consume(TIMES);
        else if(check(DIVIDED_BY)) simpleMathOperator = consume(DIVIDED_BY);
        else if(check(MOD)) simpleMathOperator = consume(MOD);
        else { error("Malformed simple math operator. Error at: '" + currentLexeme + "'."); }
        return simpleMathOperator;
    }
    private boolean simpleMathOperatorPending() {
        return check(PLUS) || check(MINUS) || check(TIMES) || check(DIVIDED_BY) || check(MOD);
    }

    private Lexeme comparator() {
        log("comparator");
        Lexeme comparator = null;
        if(check(GREATER_THAN)) comparator = consume(GREATER_THAN);
        else if(check(LESS_THAN)) comparator = consume(LESS_THAN);
        else if(check(GREATER_THAN_OR_EQUAL_TO)) comparator = consume(GREATER_THAN_OR_EQUAL_TO);
        else if(check(LESS_THAN_OR_EQUAL_TO)) comparator = consume(LESS_THAN_OR_EQUAL_TO);
        else if(check(EQUALS_EQUALS)) comparator = consume(EQUALS_EQUALS);
        else if(check(NOT_EQUALS)) comparator = consume(NOT_EQUALS);
        else { error("Malformed comparator. Error at: '" + currentLexeme + "'."); }
        return comparator;
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