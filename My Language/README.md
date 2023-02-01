# Welcome to MarioKart.

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

### Calling Functions
The `summon` keyword initiates a function call. Example:
```summon collision using enemy1x, enemy2x, enemy1y, enemy2y {
 if x1 == x2 and y1 == y2 return true;
 else return false;
}
```
Again, the `using` keyword is only used if the function has parameters.
This function would return a value of `true` or `false` based on its parameters.

## Control Flow
### Looping
In terms of `for` loops, MarioKart runs like the following:
```
time trial from lap 0 to 18 by 2 {
 ...
}
```
In this example, a for loop is running from 0 to 18 (inclusive), while incrementing by 2.

In terms of `while` loops, MarioKart runs like the following:
```
time trial to lap 18 {
 ...
 lap increase 1
}
```
This loop just goes iterates by 1 until it gets to 18 (inclusive).

### Conditionals
Conditionals are written like this:
```
first "x1 = 15" {
 return "correct!";
}
second "x1 > 15" {
 return "too high!";
}
last {
 return "too low!";
}
```

For MarioKart, `first` acts as `if`, `second`, `third`, `fourth`, and so on acts as `else if`, and `last` acts as `else`.

## Operators
| Java Operator | MK Operator |
| ----------- | ----------- |
| = | ties with |
| + | boost |
| - | slip |
| * | super star |
| / | blue shell |
| % | % |
| += | multi mushroom |
| -= | red shell |
| **= | golden mushroom |
| /= | fall off the map |
| ++ | overtake |
| -- | get passed |
| ! | not |
| == | is tied with |
| != | is not tied with |
| > | is further than |
| >= | is better than |
| < | is behind |
| <= | is worse than |
| && | and |
| || | or 

