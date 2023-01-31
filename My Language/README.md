# Welcome to GameStyle.

 Created in Spring 2023 by Andy Garcha at an unknown school in Atlanta, GA.

## Variables
### Declaration
When **creating** a new variable, initialize it using `spawn`, followed by the type afterwards. 

`spawn int enemy1;`

GameStyle supports `int`, `String`, and `boolean`. ints will declare it with a value of `0`. Strings declare to `""`, and booleans declare to `true`.

### Assignment
If you need to **assign** a new value to a variable, simply follow this syntax: 

`enemy1 = "new value";`

### Initialization
If you need to **initialize** a new variable, simply follow this syntax: 

`spawn String enemy1Name = "Todd";`

### Typing
Variables are **not** dynamic. Once something is declared ann `int`, it must remain an `int`. 

Casting and type-switching are not allowed in GameStyle.

## Functions
### Defining a Function
Defining a function in GameStyle requires an output type, a name, and optional parameters (each with their own types). Example:
```boolean collision requires int x1, int x2, int y1, int y2 {
if x1 == x2 and y1 == y2 return true;
else return false;
}
```
This function takes in four ints, and returns a boolean. The `requires` keyword is how you signify parameters in GameStyle - if your function has no parameters, you do not need this keyword whatsoever.

