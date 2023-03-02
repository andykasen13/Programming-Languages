package MarioKart.LexicalAnalysis;

public enum Types {
    //---------Keywords----------
    REQUIRES, RETURN, SUMMON, USING, GARAGE, END_OF_FILE, IDENTIFIER, COMMENT,

    //-----------Loop------------
    FROM, TO,

    //--------Conditional---------
    FIRST, 
    SECOND, THIRD, FOURTH, FIFTH, SIXTH,
    SEVENTH, EIGHTH, NINTH, TENTH, ELEVENTH, TWELFTH, 
    LAST,
    TRUE, FALSE, EQUALS, 

    //----------Data Types---------
    INT, STRING, BOOLEAN, REAL, ARRAY,

    //---Single-Character Tokens---
    OPEN_PARENTHESIS, CLOSED_PARENTHESIS,
    OPEN_BRACKET, CLOSED_BRACKET,
    SEMICOLON, DOT, COMMA,

    //----------Operators----------
    PLUS, MINUS, TIMES, DIVIDED_BY, MOD, 
    PLUS_EQUALS, MINUS_EQUALS, TIMES_EQUALS, DIVIDED_EQUALS,
    PLUS_PLUS, MINUS_MINUS,
    NOT, EQUALS_EQUALS_, NOT_EQUALS,
    GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO,
    AND, OR
}
