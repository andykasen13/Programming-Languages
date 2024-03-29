package MarioKart.Evaluator;

import MarioKart.MarioKart;
import MarioKart.LexicalAnalysis.Lexeme;
import static MarioKart.LexicalAnalysis.Type.*;

import java.util.HashMap;

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
        switch (tree.getType()) {
            // Programs and blocks alike are rooted in STATEMENT_LIST Lexemes
            case STATEMENT_LIST: return evalStatementList(tree, environment);

            // Self-Evaluating Types
            case INT, BOOLEAN, CHAR, STRING, ARRAY, REAL: return tree;

            // Identifiers can simply be looked up
            case IDENTIFIER: return environment.lookup(tree);

            case FUNCTION_DEFINITION: return evalFunctionDefinition(tree, environment);
            case FUNCTION_CALL: return evalFunctionCall(tree, environment);

            case INITIALIZATION: return evalInitialization(tree, environment);
            case ASSIGNMENT: return evalAssignment(tree, environment);

            case BINARY_EXPRESSION: return evalBinaryExpression(tree, environment);

            case PRINT: return evalPrint(tree, environment);

            default: {
                error("Cannor evaluate " + tree, tree);
                return null;
            }
        }
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

    private Lexeme evalBinaryExpression(Lexeme tree, Environment environment) {
        Lexeme num1 = tree.getChild(0);
        Lexeme num2 = tree.getChild(2);
        Lexeme binaryOperator = tree.getChild(1);

        //create an alphabet
        HashMap<Character, Integer> letterMap = new HashMap<>();
        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for(int i = 0; i < alphabet.length; i++) {
            letterMap.put(alphabet[i], i);
        }

        //TODO: reminder to check that both line numbers line up 

        if(binaryOperator.getType() == PLUS) return (add(num1, num2));

        else return null;
    }

    private Lexeme evalPrint(Lexeme tree, Environment environment) {
        Lexeme expression = eval(tree.getChild(0), environment);
        if(expression == null) error("You tried to print something that does not exist.", tree.getLineNumber());
        if(expression.getType() == IDENTIFIER) expression = environment.lookup(expression);
        System.out.println(expression.toValueOnlyString());
        return expression;
    }




    /* ----------------------------------------------- Expression Evaluation  ----------------------------------------------- */


    private Lexeme add(Lexeme num1, Lexeme num2) {
        int lineNum = num1.getLineNumber();

        // INT INT INT INT INT INT INT 
        if(num1.isInt()) { 
            int num1Val = num1.getIntValue();

            // int + int --> just add them
            if(num2.isInt())  return new Lexeme(lineNum, num1Val + num2.getIntValue(), INT);

            // int + double --> truncate the double
            else if(num2.isReal()) return new Lexeme(lineNum, num1Val + (int)num2.getRealValue(), INT);

            // int + string --> turn the int into a string
            else if(num2.isString()) return new Lexeme(lineNum, Integer.toString(num1Val) + num2.getWord(), STRING);

            // int + boolean --> false = 0, true = 1
            else if(num2.isBool()) return new Lexeme(lineNum, num1Val + (num2.getBoolValue() ? 1 : 0), INT);

            // int + char --> alphabet
            else if(num2.isChar()) return new Lexeme(lineNum, num1Val + (int)num2.getCharValue(), INT);
        }

        // REAL REAL REAL REAL REAL REAL REAL REAL
        else if(num1.isReal()) {
            double num1Val = num1.getRealValue();

            // real + int --> turn int into real
            if(num2.isInt()) return new Lexeme(lineNum, num1Val + Double.valueOf(num2.getIntValue()), REAL);

            // real + real --> just add them
            else if(num2.isReal()) return new Lexeme(lineNum, num1Val + num2.getRealValue(), REAL);

            // real + string --> turn real into string
            else if(num2.isString()) return new Lexeme(lineNum, num1Val + num2.getWord(), STRING);

            // real + boolean --> false = 0, true = 1
            else if(num2.isBool()) return new Lexeme(lineNum, num1Val + (num2.getBoolValue() ? 0.0 : 1.0), REAL);

            // real + char --> alphabet
            else if(num2.isChar()) return new Lexeme(lineNum, num1Val + Double.valueOf((int)num2.getCharValue()), REAL);
        }

        //STRING STRING STRING STRING STRING STRING STRING STRING
        else if(num1.isString()) {
            String num1Val = num1.getWord();

            // string + int --> int to string
            if(num2.isInt()) return new Lexeme(lineNum, num1Val + Integer.toString(num2.getIntValue()), STRING);

            // string + double --> double to string
            else if(num2.isReal()) return new Lexeme(lineNum, num1Val + num2.getRealValue(), STRING);

            // string + string --> just add normally
            else if(num2.isString()) return new Lexeme(lineNum, num1Val + num2.getWord(), STRING);

            // string + boolean --> boolean to string
            else if(num2.isBool()) return new Lexeme(lineNum, num1Val + num2.getBoolValue(), STRING);

            // string + char --> char to string
            else if(num2.isChar()) return new Lexeme(lineNum, num1Val + Character.toString(num2.getCharValue()), STRING);
        }

        // BOOL BOOL BOOL BOOL BOOL BOOL BOOL BOOL
        else if(num1.isBool()) {
            boolean num1Val = num1.getBoolValue();

            // bool + int --> odd = false, even = true
            if(num2.isInt()) {
                int x = (num1Val ? 2 : 1);
                x += num1.getIntValue();
                if(x % 2 == 0) return new Lexeme(lineNum, true, BOOLEAN);
                else return new Lexeme(lineNum, false, BOOLEAN);
            }

            // bool + real --> odd = false, even = true
            else if(num2.isReal()) {
                int x = (num1Val ? 2 : 1);
                x+= (int)num1.getRealValue();
                if(x % 2 == 0) return new Lexeme(lineNum, true, BOOLEAN);
                else return new Lexeme(lineNum, false, BOOLEAN);
            }

            // bool + string --> convert bool to string
            else if(num2.isString()) return new Lexeme(lineNum, num1Val + num2.getWord(), STRING);

            // bool + bool --> or operator
            else if(num2.isBool()) return new Lexeme(lineNum, num1Val || num2.getBoolValue(), BOOLEAN);

            // bool + char --> error
            else if(num2.isChar()) {
                error("you cannot add a boolean and a char. why? because i am lazy", lineNum); 
                return null;
            }
        }

        // CHAR CHAR CHAR CHAR CHAR CHAR CHAR CHAR
        else if(num1.isChar()) {
            char num1Val = num1.getCharValue();

            // char + int --> convert char to int, add, convert back to char
            if(num2.isInt()) return new Lexeme(lineNum, (char)((num2.getIntValue()) + (num1Val - 0)), CHAR);

            // char + real --> convert real to int, char to int, add, convert back to char
            else if(num2.isReal()) return new Lexeme(lineNum, (char)((int)(num2.getRealValue()) + (num1Val - 0)), CHAR);

            // char + string --> char to string, add
            else if(num2.isString())  return new Lexeme(lineNum, (num1Val + num2.getWord()), STRING); 

            // char + bool --> false = no change, true = increase by 1
            else if(num2.isBool()) return new Lexeme(lineNum, (char)((num1Val + 0) + (num2.getBoolValue() ? 1 : 0)), CHAR);

            // char + char --> regular add
            else if(num2.isChar()) return new Lexeme(lineNum, num1Val + num2.getCharValue(), CHAR);
        }

        else {
            error("You have tried to add two things together that i do not understand. please try again", lineNum);
            return null;
        }
        return null;
    }

    public Lexeme subtract(Lexeme num1, Lexeme num2) {
        int lineNum = num1.getLineNumber();

        if(num1.isInt()) {
            int num1Val = num1.getIntValue();

            // int - int --> regular subtraction
            if(num2.isInt()) return new Lexeme(lineNum, num1Val - num2.getIntValue(), INT);

            // int - real --> convert int to real, subtract, truncate
            else if(num2.isReal()) return new Lexeme(lineNum, (int)((double)num1Val - num2.getRealValue()), INT);

            // int - string --> error
            else if(num2.isString()) {
                error("You tried to subtract an int from a string and i don't want to code something for that so fix it, now!", lineNum);
                return null;
            }

            // int - boolean --> error
            else if(num2.isBool()) {
                error("You tried to subtract an int from a boolean and i am a lazy bitch so fix that so i don't have to code logic for that", lineNum);
                return null;
            }

            // int - char --> error
            else if(num2.isChar()) {
                error("You tried to subtract a char from an int and that is not supported.", lineNum);
            }
        }

        else if(num1.isReal()) {
            if(num2.isInt()) return new Lexeme(lineNum, (int)num1.getRealValue() - num2.getIntValue(), INT);
            else if(num2.isReal()) return new Lexeme(lineNum, num1.getRealValue() - num2.getRealValue(), REAL);
            else if(num2.isString()) error("You tried to subtract a STRING from a REAL. MarioKart does not support this.", lineNum);
            else if(num2.isBool()) error("You tried to subtract a BOOLEAN from a REAL. MarioKart does not support this.", lineNum);
            else if(num2.isChar()) error("You tried to subtract a CHAR from a REAL. MarioKart does not support this.", lineNum);
        } else if(num1.isString()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getWord().substring(0, num1.getWord().length() - num2.getIntValue()), STRING);
            else if(num2.isReal()) error("You tried to subtract a REAL from a STRING. MarioKart does not support this.", lineNum);
            else if(num2.isString()) {
                String num1Value = num1.getWord();
                String num2Value = num2.getWord();
                if(num1Value.contains((CharSequence) num2)) return new Lexeme(lineNum, num1Value.replaceAll(num2Value, ""), STRING); 
                else return new Lexeme(lineNum, num1Value, STRING);
            }
            else if(num2.isBool()) {
                if(!num2.getBoolValue()) return new Lexeme(lineNum, "", STRING);
                else return num1;
            }
            else if(num2.isChar()) return new Lexeme(lineNum, num1.getWord().replaceAll(num2.getCharValue() + "", ""), STRING);
        } else if(num1.isBool()) {
            if(num2.isInt()) return new Lexeme(lineNum, (num2.getIntValue() % 2 == 0) ? num1.getBoolValue() : !num1.getBoolValue(), BOOLEAN);
            else if(num2.isReal()) return new Lexeme(lineNum, ((int)num2.getRealValue() % 2 == 0) ? num1.getBoolValue() : !num1.getBoolValue(), BOOLEAN);
        } else if(num1.isChar()) {
            if(num2.isChar()) return new Lexeme(lineNum, num1.getCharValue() - num2.getCharValue(), CHAR);
        }
        error("You tried to subtract a type " + num2.getType() + " from a type " + num1.getType() + ". MarioKart does not support this.", lineNum);
        return null;
    }

    public Lexeme multiply(Lexeme num1, Lexeme num2) {
        int lineNum = num1.getLineNumber();

        if(num1.isInt()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getIntValue() * num2.getIntValue(), INT);
            else if(num2.isReal()) return new Lexeme(lineNum, num1.getIntValue() * (int)num2.getRealValue(), INT);
            else if(num2.isString()) return new Lexeme(lineNum, num2.getWord().repeat(num1.getIntValue()), STRING);
        } else if(num1.isReal()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getIntValue() * (int)num2.getRealValue(), INT);
            else if(num2.isReal()) return new Lexeme(lineNum, num1.getRealValue() * num2.getRealValue(), REAL);
        } else if(num1.isString()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getWord().repeat(num2.getIntValue()), STRING);
            else if(num2.isString()) return new Lexeme(lineNum, num1.getWord().substring(0, num1.getWord().length()/2) + num2.getWord() + num1.getWord().substring(num1.getWord().length()/2, num1.getWord().length()), STRING);
        } else if(num1.isBool()) {
            if(num2.isBool()) return new Lexeme(lineNum, num1.getBoolValue() && num2.getBoolValue(), BOOLEAN);
        }

        error("You tried to multiply a type " + num1.getType() + " and a type " + num2.getType() + ". MarioKart does not support this.", lineNum);
        return null;
    }

    public Lexeme divide(Lexeme num1, Lexeme num2) {
        int lineNum = num1.getLineNumber();

        if(num1.isInt()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getIntValue() / num2.getIntValue(), INT);
            else if(num2.isReal()) return new Lexeme(lineNum, (double)num1.getIntValue() / num2.getRealValue(), REAL);
            else if(num2.isString()) return new Lexeme(lineNum, (num1.getIntValue() <= num2.getWord().length()) ? num2.getWord().substring(0,num1.getIntValue()) : num2.getWord().repeat(num1.getIntValue()/num2.getWord().length()) + num2.getWord().substring(0,num1.getIntValue() % num2.getWord().length()),STRING);
        } else if(num1.isReal()) {
            if(num2.isInt()) return new Lexeme(lineNum, num1.getRealValue() / (double)num2.getIntValue(), REAL);
            else if(num2.isReal()) return new Lexeme(lineNum, num1.getRealValue() / num2.getRealValue(), REAL);
            else if(num2.isString()) return new Lexeme(lineNum, ((int)num1.getRealValue() <= num2.getWord().length()) ? num2.getWord().substring(0,(int)num1.getRealValue()) : num2.getWord().repeat((int)num1.getRealValue()/num2.getWord().length()) + num2.getWord().substring(0,(int)num1.getRealValue() % num2.getWord().length()),STRING);
        } else if(num1.isString()) {
            if(num2.isString()) return new Lexeme(lineNum, (num1.getWord().length() >= num2.getWord().length()) ? num1.getWord().substring(0, num2.getWord().length()) : num2.getWord().repeat(num2.getWord().length() - num1.getWord().length()), STRING);
        } else if(num1.isBool()) {
            if(num2.isBool()) return new Lexeme(lineNum, (num1.getBoolValue() == num2.getBoolValue()) ? 1 : 0, INT);
        }

        error("You tried to divide a type " + num2.getType() + " by a type " + num1.getType() + ". MarioKart does not support this.", lineNum);
        return null;
    }

    public Lexeme modulus(Lexeme num1, Lexeme num2) {
        int lineNum = num1.getLineNumber();

        if(num1.isInt() && num2.isInt()) return new Lexeme(lineNum, num1.getIntValue() % num2.getIntValue(), INT);
        else if(num1.isReal() && num2.isReal()) return new Lexeme(lineNum, num1.getRealValue() % num2.getRealValue(), REAL);
        else if(num1.isString() && num2.isString()) return new Lexeme(lineNum, num1.getWord().length() - num2.getWord().length(), INT);
        else if(num1.isBool() && num2.isBool()) return new Lexeme(lineNum, (num1.getBoolValue() == num2.getBoolValue()) ? 0 : 1, INT);

        error("You tried to carry out an expression of type " + num1.getType() + " mod type" + num2.getType() + ". MarioKart does not support this.", lineNum);
        return null;
    }



}
    
