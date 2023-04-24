# Welcome to MarioKart.

 Created in Spring 2023 by Andy Garcha at an unknown school in Atlanta, GA.

## Variables
### Declaration
When **creating** a new variable, initialize it using the data type followed by the identifier.

`int enemy1;`

MarioKart supports `int`, `String`, `char`, `real`, and `boolean`. ints will declare it with a value of `0`. Strings declare to `""`, chars to `''`, reals to `0.0`, and booleans to `true`.

### Assignment
If you need to **assign** a new value to a variable, simply follow this syntax: 

`enemy1 tiesWith 3;`

### Initialization
If you need to **initialize** a new variable, simply follow this syntax: 

`String enemy1Name tiesWith "Todd";`

### Typing
Variables are **not** dynamic. Once something is declared an `int`, it must remain an `int`. 

Casting and type-switching are not allowed in MarioKart.

## Functions
### Defining a Function
Defining a function in MarioKart requires an output type, a name, and optional parameters (each with their own types). Example:
```
boolean collision requires int x1, int x2, int y1, int y2 {
if x1 == x2 and y1 == y2 return true;
else return false;
}
```
This function takes in four ints, and returns a boolean. The `requires` keyword is how you signify parameters in MarioKart - if your function has no parameters, you do not need this keyword whatsoever.

### Calling Functions
The `summon` keyword initiates a function call. Example:
```
summon collision using enemy1x, enemy2x, enemy1y, enemy2y;
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

## Operators and Comparators
| Java Operator | MK Operator    |
| -- |----------------|
| `=` | `tiesWith`    |
| `+` | `boost`        |
| `-` | `slip`         |
| `*` | `superStar`   |
| `/` | `blueShell`   |
| `%` | `%`            |
| `+=` | `multiMushroom` |
| `-=` | `redShell`      |
| `*=` | `goldenMushroom` |
| `/=` | `fallOffTheMap` |
| `++` | `overtake`       |
| `--` | `getPassed`     |
| `!` | `not`            |
| `==` | `isTiedWith`   |
| `!=` | `isNotTiesWith` |
| `>` | `isFurtherThan` |
| `>=` | `isBetterThan` |
| `<` | `isBehind`      |
| `<=` | `isWorseThan`  |
| `&&` | `and`            |
| `or` | `or` |

## Collections
### Creation
MarioKart uses `garages` to store variables. `garage`s are not restricted to a type.
`garage enemy1Stats[7];`

### Modification
To modify a specific space in a `garage`, use the following syntax:
```enemy1Stats[3] = "defeated";```

### Access and Deletion
To access a specific space in a `garage`, use the following syntax:
```enemy1Stats[3];```

## Built-in Functions
### rob
To delete a specific space in a `garage`, use the following command:

```rob enemy1Stats[3];```

This initializes a `garage` of length `7`.

### setAblaze
To clear an entire `garage`, use the following command:

```setAblaze enemy1Stats;```

### ???

`blueshell enemy1Stats;`

### print
To print, simply use:

```print "Hello World!";```

Keywords
| keyword | java alternative |
| ------- | ---------------- |
| spawn | new |
| garage | array |
| time trial | for / while |
| summon | func |
| requires | () |
| first | if |
| second | else if |
| last | else |

### Google Sheet
https://docs.google.com/spreadsheets/d/1sydhecTJOR_gCx7WWyMLkqbHQ5fe810liVykVgWQfjI/edit?usp=sharing
