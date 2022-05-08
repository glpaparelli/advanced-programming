# Haskell Introduction
Haskell is a functional programming language, based on the model of computing called *lambda calculus*. 

## Intro to Functional Programming
*The key idea is to do everything by composing functions*
- **no mutable state**: variables can't be modified
- **no side effects**: no envoiroment interactions (no prints, ...)

*Functional programming is based on:*
- **1-st class and high-order functions**: functions can be denoted and passed as arguments to functions and they can be returned as result of functions
- **recursion**: the recursion takes place of iteration (no mutable state, hence we can't have control variables to check the guards of the loops)
- **powerful list facilities**: the functional programming go along very well with the logic definition of lists and we can have functionalities that with imperative programming are not possible (eg. infinite lists)
- **polymorphism**: typically universal parametric implicit (implicit since we have type inference)
- **fully general aggregates**:
	- wide use of tuples and records
	- data structures cannot be modified, have to be recreated
- **structured function returns**: there are no side effects thus the only way for functions to pass information to the caller (no side effect, if we want to return multiple informations we have to use an aggregate, prev. point)
- **garbage collection**:
	- in case of static scoping, unlimited extent for
		- locally allocated data structures
		- locally defined functions 
	- they cannot be allocated on the stack (if we return a function from a function and that function was allocated on the stack at the return we would loose the function definition)

## Haskell's Overview
The **interactive interpreter** is the **G**lasgow-**H**askell **C**ompiler **I**nterpreter **ghci**: read-eval-print loop

Actually the ghci infers type before compiling or executing, the loop is read -> infer -> compile -> eval -> print -> restart

The Haskell type system do not allow casts or similar things, because of how Haskell handles overloading

***Indentation is important to Haskell, it takes the place of braces***

### Types
***Haskell determine types with Type Inference: the types are infered based on how they are used in the body of the fucntion***

#### Booleans
```haskell
True, False :: Bool

not :: Bool -> Bool

-- and, or could recieve a foldable type as a list
and, or :: Foldable t => t Bool -> Bool

-- the type of the two branches have to mach
if ... then ... else ...
```

#### Chars and Strings
```haskell
-- char between ''
'a', 'b', ';', '2', ... :: Char

-- strings are lists of chars, between ""
"Ron Weasley" :: [Char] 
```

#### Numbers
```haskell
-- type class Num to disambiguate
0, 1, 2, ... :: Num p => p 

1.0, 3.14, ... :: Fractional a => a

-- infix operators become prefix operators
-- eg: 2 + 3 is (+) 2 3
-- to use infix notation: 2 `+` 3
+, *, -, ... :: Num a => a -> a -> a


-- no castings allawoed consequences: 
-- decimal division
/ :: Fractional a => a -> a -> a
-- integer division
div, mod :: Integral a => a -> a -> a

-- power
^ :: (Num a, Integral b) => a -> b -> a

```

#### Compound Types
##### Tuples
```haskell
-- pair
("Advanced Programming", 2021) :: Num b => ([Char], b)

-- projection, only for pairs: first and second element
fst :: (a, b) -> a 
snd :: (a, b) -> b

-- tuple
('4', True, "AP") :: (Char, Bool, [Char])
```

##### Records
```haskell 
-- declaration
data Person = Person {firstName :: String, lastName :: String}

--usage
hp = Person {firstName = "Harry", lastName = "Potter"}
```

##### Lists
```haskell
-- NIL, polymorphic type
[] :: [a]  

-- infix cons notation, head instertion
1 : [2, 3, 4] :: Num a => [a]

-- infix head concatenation
[1, 2] ++ [3, 4]

-- get the first element of the list
head :: [a] -> a
-- get the rest of the list (list without first element)
tail :: [a] -> [a] 
```

###### List Constructors
```haskell
-- range
ghci> [1..20]
[1, 2, 3, 4, 5, 6, 7, ..., 20]
ghci > ['a'..'z']
"abcdefghi...z"

-- range with step
ghci> [3,6..20]
[3, 6, 9, 12, 15, 18]
ghci> [7,6..1]
[7, 6, 5, 4, 3, 2, 1]

-- infinite list
[1..]

-- it works! (more on that later)
ghci> take 10 [1..]
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

ghci> take 10 (cycle [1,2])
[1,2,1,2,1,2,1,2,1,2,1,2]

ghci> take 10 (repeat 5)
[5,5,5,5,5,5,5,5,5,5]
```

In Haskell (and in functional programming) lists are more powerful than imperative programming: [[Haskell List Comprehension]]
### Binding Variables 
Variables (names) are bound to expressions, without evaluating them: main concept *lazy evaluation*. 

In haskell there are not variables, we just give names to expressions and we do not evaluate the expression until we use the name. 

The scope of the binding is the rest of the session, a successive binding can hide the upper one:
```haskell
-- the let is optional
Prelude> let a = 5
Prelude> let a = 3

Prelude> a
3
```

The behaviour can be "surprising"
```haskell
Prelude> a = 6

-- no result
Prelude> a = a + 1

-- broken loop
Prelude> a 
^CInterrupted. 

```
- `a = a + 1`  do not give a result since `a + 1` is not evaluated (lazy evaluation) 
- when we try to evaluate `a` we substitute `a` with its binding, which is `a + 1`, then we substitute `a` with its binding, ...

In eager languages (Java, OCaml, ...) the previous code gives the correct result 7

### Patterns and Declarations
Patterns can be used in place of variables
`<pat> ::= <var> | <tuple> | <cons> | <record> ...`
More in general we have `<pat> = <exp>`

*examples*
```haskell
myTuple = ("Foo", "Bar")
myList = [1, 2, 3, 4]

-- pattern assignment, x = "Foo", y = "Bar"
-- mind that neither x or y are evalutated yet, this is just a 
-- binding: lazy evaluation
(x, y) = myTuple

-- z = 1, zs = [2, 3, 4]
z:zs = myList 
```

Mind that we can also do **local declarations**
```haskell
let (x, y) = (2, "FooBar") in x * 4
```
In this case the names `x` and `y` (which are binded to 2, "FooBar") can be used inside the expression in the `in`

### Anonymous Functions
They are the haskell way to represent the lambdas abstractions.
In Haskell evaluated functions are values, hence we can write expression that evaluate to a fucntion: this enable greatly the higher ordering ([[Haskell HO Functions]]) concept. 

**To define anonymous functions**
```haskell
-- sintax of a function that gives the successor
\x -> x + 1 

-- function application:
Perlude> (\x -> x + 1) 5 
6

-- bind a function to an identifier (no longer anonymous)
Perlude> f = \x -> x + 1
Perlude> :t f
f :: Num a => a -> a
-- application of the function
Perlude> f 7
8
```

**To define anonymous function using Patterns**
```haskell
Prelude> h = \(x, y) -> x + y
Prelude> :t h
h :: Num a => (a, a) -> a

Prelude> h (3, 4)
7

{- watch out: this don't work because the function expect 
a pair while we are passing two parameters 
Haskell is left associative with the pars: it is trying to
evaluate (h 3) but it can't since h is not carried -} 
Prelude> h 3 4
error

-- we can do pattern matching on the parameters of the function
Prelude> k = \(z:zs) -> length zs
Prelude> :t k
k :: [a] -> Int
Prelude> k "hello"
4
```

### Function Declarations
It takes the form of:
- name $pat_1$ = $exp_1$
- name $pat_2$ = $exp_2$
- ...

The declaration relies heavly to pattern matching of the paramerters, in fact $pat_i$ stands for i-th path: when the parameter match the pattern that "case" of the function is executed

*examples*
```haskell
-- the arguments have to mach the patter (x, y)
f (x, y) = x + y

-- pattern matching on the parameters of the function length
-- if this pattern is matched means that the list is emtpy
length [] = 0
-- pattern for non-empty lists
length (x:s) = 1 + length(s)

-- we always have to define exhaustive patterns
Prelude> len (z:zs) = length zs
len :: [a] -> Int
Prelude> len [1, 2, 3] 
2
Prelude> len []
*** Exception: non exhaustive patterns in function len
```

#### Intro to Functions on Lists
**Reverse a list**
```haskell
reverse [] = reverse
reverse (x:xs) = (reverse xs) ++ [x]
```
The previous code is higly inefficient (quadratic) since the operator `++` build a new list (in which are added all the elements of the first list) for every recursion  

**Reverse a list with Tail Recursion**
```haskell
reverse xs = 
	-- definition of support function with pattern matching
	let rev ([], accum) = accum 
	    rev (y:ys, accum) = rev (ys, y:accum)
	in rev (xs, [])
```
The previous code is obviously way more efficient and elegant (linear time, use of an accumulator, tail recursion not heavy on the memory)

### Function Types 
In Haskell `f :: A -> B` means that for every x $\in$ A
$$
f(x) = 
\begin{cases}
y = f(x) \in B, \\
run forever
\end{cases}
$$
In words: *if f(x) terminates then f(x) $\in$ B*

#### Type of Predefined Functions
```haskell
Prelude> :t not
not :: Bool -> Bool

Prelude> :t (+)
(+) :: Num a => a -> a -> a

Prelude> :t (:)
(:) :: a -> [a] -> [a]

Prelude> :t elem
-- Eq a tells us that 'a' is a generic type that support 
-- equalities, more on that later on
elem :: Eq a => a -> [a] -> Bool
```

### Lazyness
As said Haskell is a lazy language, functions and data constructors (also user-defined ones) don't evaluate their arguments until they need them. 

#### Call By Need
*Normal Order Evaluation* aka *Lazy Evaluation*: 
- functions are evaluated first, arguments are evaluated if and when they are need
- sort of parameter passing by name
- some evaluation may be repeated (overhead)

**Haskell realizes lazy evaluation by using call by need parameter passing**; an expression passed as argument is bound to the formal parameter, but it is evaluated only if its value is need. 
Mind that the argument (if it is) is evalyated only the first time, Haskell uses a memorization technique: the result of an evaluation is saved and further uses of the argument are simply substituited when needed. 

Call by need, combined with *lazy data constructors* (more to come), allows to construct potentially infinite data structures and to call infinitely resursive functions without necessarily causing non-termination. 

*Lazy evaluation works fine with purely functional languages:* the absence of side effects is key. 
Side effects require programmers to think about the order in which things happen, which is not predictable with lazy languages.

### From Loops to Recursion
`for` and `while` loops are replaced by using **recursion**: subroutines call themeself directly or indirectly (mutual recursion)

```haskell
myLength [] = 0
myLength (x:s) = 1 + myLength(s)
```

We can also define recursive functions using **guards** and **pattern matching**
```haskell
-- explicitly define the type, usefull 
-- for type inference and debugging
myTake :: (Num i, Ord i) => i -> [a] -> [a]

{-
a case of pattern matching that also uses guards. 
the semantic is: we don't care of the second argument (_) if 
the first argument (the number of elements that we have to take)
is <= 0. In this case we always return the empty list
Guards are boolean expressions
-}
myTake n _ 
    | n <= 0 = []
myTake _ [] = []
myTake n (x:xs)) x : myTake (n-1) xs
```