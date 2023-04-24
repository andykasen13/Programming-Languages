package MarioKart.Environments;

import java.util.ArrayList;

import MarioKart.LexicalAnalysis.Lexeme;
import MarioKart.LexicalAnalysis.Type;
import MarioKart.MarioKart;
import static MarioKart.LexicalAnalysis.Type.*;

public class Environment {
    //------------- Instance Varables -------------
    private Environment parentEnvironment;
    private ArrayList<NamedValue> entries = new ArrayList<NamedValue>();

    //------------- Constructors -------------
    public Environment(Environment parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public Environment() {
        this(null);
    }

    //------------- Core Environment Functions -------------
    private Lexeme softLookup(Lexeme identifier) {
        for(NamedValue namedValue : entries) {
            if(namedValue.getName().equals(identifier)) {
                return namedValue.getValue();
            }
        }
        return null;
    }

    public Lexeme lookup(Lexeme identifier) {
        Lexeme value = softLookup(identifier);
        if(value == null) {
            if(parentEnvironment != null) return parentEnvironment.lookup(identifier);
            else {
                error("'" + identifier.getWord() + "' is undefined.", identifier.getLineNumber());
            }
        }
        return value;
    }

    public void add(Type type, Lexeme identifier, Lexeme value) {
        if(softLookup(identifier) != null) {
            error("A variable with the name '" + identifier.getWord() + "' is already defined and cannot be re-declared", identifier.getLineNumber());
        }
        else {
            System.out.println("entries before" + entries);
            ArrayList<Integer> a = new ArrayList<Integer>();
            a.add(1);
            a.add(2);
            a.add(5);
            System.out.println("why" + a);
            NamedValue v = new NamedValue(type, identifier);
            entries.add(v);
            System.out.println("entries after" + entries.toString());
            if(value != null) update(identifier, value);
        }
    }

    public void add(Type type, Lexeme identifier) {
        add(type, identifier, null);
    }

    public void update(Lexeme identifier, Lexeme newValue) {
        // Ensure this identifier is defined in this or some parent environment
        lookup(identifier);

        // Search this environment and update if found locally
        for(NamedValue namedValue : entries) {
            if(namedValue.getName().equals(identifier)) {

                Type declaredType = namedValue.getType();
                Type providedType = newValue.getType();

                if(providedType != declaredType) 
                    newValue = typeElevate(newValue, declaredType);
                

                if(newValue == null) 
                    error("Variable '" + identifier.getWord() + "' has been declared as type " + declaredType + " and cannot be assigned a value of type " + providedType, identifier.getLineNumber());
                

                namedValue.setValue(newValue);


                // Whether a value of valid type was provided or not,
                // quit looking once we find a match.
                return;
            }
        }
        // If no local match is found, try to update in the parent environment
        parentEnvironment.update(identifier, newValue);

    }

    public Lexeme typeElevate(Lexeme newValue, Type declaredType) {
        Type newType = newValue.getType();
        int OGLineNumber = newValue.getLineNumber();
        int newIntValue = 0;
        double newRealValue = 0.0;
        boolean newBoolValue = false;
        if(declaredType == INT && newType == REAL) {
            return new Lexeme(OGLineNumber, (double)newValue.getIntValue(), newType);
        } else if(declaredType == REAL && newType == INT) {
            return new Lexeme(OGLineNumber, (int)newValue.getRealValue(), newType);
        } else if(declaredType == BOOLEAN && newType == INT) {
            if(newValue.getBoolValue()) newIntValue = 1;
            return new Lexeme(OGLineNumber, newIntValue, newType);
        } else if(declaredType == INT && newType == BOOLEAN) {
            if(newValue.getIntValue() != 0) newBoolValue = true;
            return new Lexeme(OGLineNumber, newBoolValue, newType);
        } else if(declaredType == STRING && newType == INT) {
            return new Lexeme(OGLineNumber, newValue.getWord().length(), newType);
        } else if(declaredType == INT && newType == STRING) {
            return new Lexeme(OGLineNumber, Integer.toString(newValue.getIntValue()), newType);
        } else if(declaredType == STRING && newType == REAL) {
            return new Lexeme(OGLineNumber, (double)newValue.getWord().length(), newType);
        } else if(declaredType == REAL && newType == STRING) {
            return new Lexeme(OGLineNumber, Double.toString(newValue.getRealValue()), newType);
        } else if(declaredType == REAL && newType == BOOLEAN) {
            return new Lexeme(OGLineNumber, (newValue.getRealValue() != 0), newType);
        } else if(declaredType == BOOLEAN && newType == REAL) {
            if(newValue.getBoolValue()) newRealValue = 1.0;
            return new Lexeme(OGLineNumber, newRealValue, newType);
        } else if(declaredType == BOOLEAN && newType == STRING) {
            return new Lexeme(OGLineNumber, Boolean.toString(newValue.getBoolValue()), newType);
        } else if(declaredType == STRING && newType == BOOLEAN) {
            return new Lexeme(OGLineNumber, newValue.getWord().isEmpty(), newType);
        } else if(declaredType == CHAR && newType == INT) {
            return new Lexeme(OGLineNumber, (int)newValue.getCharValue(), newType);
        } else if(declaredType == INT && newType == CHAR) {
            return new Lexeme(OGLineNumber, (char)newValue.getIntValue(), newType);
        } else if(declaredType == CHAR && newType == REAL) {
            return new Lexeme(OGLineNumber, (double)newValue.getCharValue(), newType);
        } else if(declaredType == CHAR && newType == STRING) {
            return new Lexeme(OGLineNumber, String.valueOf(newValue.getCharValue()), newType);
        } else if(declaredType == STRING && newType == CHAR) {
            return new Lexeme(OGLineNumber, newValue.getWord().charAt(0), newType);
        } else if(declaredType == CHAR && newType == BOOLEAN) {
            return new Lexeme(OGLineNumber, newValue.getCharValue().equals(null), newType);
        }
        else error("You tried to cast something that we don't accept. Try again!", OGLineNumber);
        return new Lexeme(null);
/* int to real
 * real to int
 * bool to int
 * int to bool
 * string to int 
 * int to string
 * string to real
 * real to string
 * real to bool
 * bool to real
 * bool to string
 * string to bool
 */

 // note for myself: real --> char, bool --> char
    }
    
    //------------- toString -------------
    public String toString() {
        String soScuffed = "";

        // determine if the environment is the parent or not
        if(parentEnvironment == null) soScuffed = "This is the global environment";
        else soScuffed = "Parent: " + parentEnvironment.hashCode();

        String myString = ("------------------\n" + "Environment " + this.hashCode() + "\n\t" + soScuffed + "\n\t-----------" + "\n\tValues: ");
        for(NamedValue entry : entries) {
            myString = myString.concat("\n\t" + entry.toString());
        }

        return myString;
    }

    //------------- Error Reporting -------------
    private void error(String message, int lineNumber) {
        MarioKart.syntaxError(message, lineNumber);
    }
}