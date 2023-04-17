package MarioKart.Evaluator;

import MarioKart.MarioKart;
import MarioKart.LexicalAnalysis.Lexeme;
import static MarioKart.LexicalAnalysis.Type.*;
import MarioKart.Environments.Environment;

public class Evaluator {
    
    //--------------------- Error Reporting ---------------------
    private Lexeme error(String message, Lexeme lexeme) {
        MarioKart.runtimeError(message, lexeme);
        return new Lexeme(lexeme.getLineNumber(), message, ERROR);
    }

    private Lexeme error(String message, int lineNumber) {
        MarioKart.runtimeError(message, lineNumber);
        return new Lexeme(lineNumber, message, ERROR);
    }

    //--------------------- Evaluation --------------------- (pray for me)
    public Lexeme eval(Lexeme tree, Environment environment) {
        return switch (tree.getType()) {
            // Programs and blocks alike are rooted in STATEMENT_LIST Lexemes
            case STATEMENT_LIST -> evalStatementList(tree, environment);

            // Self-Evaluating Types
            case INT, BOOLEAN, CHAR, STRING, ARRAY, REAL -> tree;

            // Identifiers can simply be looked up
            case IDENTIFIER -> environment.lookup(tree);

            case FUNCTION_DEFINITION -> evalFunctionDefinition(tree, environment);
            case FUNCTION_CALL -> evalFunctionCall(tree, environment);

            case INITIALIZATION -> evalInitialization(tree, environment);
            case ASSIGNMENT -> evalAssignment(tree, environment);

            default -> error("Cannor evaluate " + tree, tree.getLineNumber());
        };
    }

    private Lexeme evalStatementList(Lexeme tree, Environment environment) {
        Lexeme result = new Lexeme(NULL);
        for(Lexeme statement : tree.getChildren()) {
            result = eval(statement, environment);
        }
        return result;
    }

    private Lexeme evalFunctionDefinition(Lexeme tree, Environment environment) {
        tree.setDefiningEnvironment(environment);
        Lexeme functionName = tree.getChild(0);
        environment.add(FUNCTION, functionName, tree);
        return functionName;
    }

    private Lexeme evalFunctionCall(Lexeme tree, Environment environment) {
        Lexeme functionName = tree.getChild(0);
        Lexeme functionDefinitionTree = environment.lookup(functionName);
        if(functionDefinitionTree.getType() != FUNCTION_DEFINITION) error("You tried to call something that wasn't a function. Dumbass.", tree.getLineNumber());
        Lexeme parameterList = functionDefinitionTree.getChild(1);
        Lexeme argumentList = tree.getChild(1);
        Lexeme evaluatedArgumentList = evalArgList(argumentList, environment);
        if(parameterList.getNumOfChildren() != evaluatedArgumentList.getNumOfChildren()) {
            error("your parameter count and argument count are not the same.", tree.getLineNumber());
        }
        Environment callEnvironment = new Environment(environment);
        for(int i = 0; i < parameterList.getNumOfChildren(); i++) {;
            callEnvironment.add(parameterList.getChild(i).getChild(0).getType(),  parameterList.getChild(i).getChild(1),  evaluatedArgumentList.getChild(i));
        }
        Lexeme functionBody = functionDefinitionTree.getChild(3);
        return eval(functionBody, callEnvironment);
    }

    private Lexeme evalArgList(Lexeme tree, Environment environment) {
        for(int i = 0; i < tree.getNumOfChildren(); i++) {
            tree.setChild(eval(tree.getChild(i), environment),i);
        }
        return tree;
    }

    private Lexeme evalInitialization(Lexeme tree, Environment environment) {
        Lexeme ID = tree.getChild(1);
        Lexeme value = eval(tree.getChild(2),environment);
        if(tree.getChild(0).getType() != value.getType()) {
            error("your types in your initialization do not match buster.", tree.getLineNumber());
        }
        environment.add(value.getType(), ID, value);
        return ID;
    }

    private Lexeme evalAssignment(Lexeme tree, Environment environment) {
        Lexeme ID = tree.getChild(0);
        Lexeme value = eval(tree.getChild(1),environment);
        environment.update(ID, value);
        return null;
    }

}
    
