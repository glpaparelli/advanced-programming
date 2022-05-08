# Datatypes
## Datatypes Declarations
How to declare a new type: the general syntax for datatype declaration is
```
data <name> = <clause> | ... | <clause>
<clause> :: = <constructor> | <constructor> <type>
```
- `<type>` and `<constructor>` must be capitalized
- `data` is a keyword similar to `class`, it tells the interpreter that we are defining a new type `<type>` with its own constructors

*example 1*
```haskell
data Color = Red | Yellow | Blue
```
In this specific case `Color` is exactly like an enum in Java where the possibility are `Red`, `Yellow` and `Blue`. 
`Red`, `Yellow`, `Blue` have type `Color`

*example 2*
```haskell
data Atom = Atom String | Number Int
```
The type `Atom` has two constructors, one with a parameter `String` and the other with a parameter `Int`. 
Elements are `Atom "A"`, `Atom "B"`, ..., `Number 0`, `Number 1`, ... and they all are of type `Atom`

### Recursevly Defined Data Structure
*example 1*
```haskell
data List = Nil | Cons(Atom, List)
```
A `List` is either `Nil` or an `Atom` inserted (`Cons` aka`:`) in a `List`. 
Elements are `Nil`, `Cons(Atom "A", Nil`, ... 
`Cons(Number 2, Cons(Atom "Bill", Nil))`, ... and they all have type `List`

*example 2*
```haskell
data Tree = Leaf Int | Node (Int, Tree, Tree)
```
We can now represent a tree:
`Node(4, Node(3, Leaf 1, Leaf 2), Node(5, Leaf 6, Leaf 7))`
Now we can see *why `Node` and `Leaf`* are called constructor: thet return a type `Tree`, which is required in the definiton of `Tree`

## Datatypes and Pattern Matching
Constructors can be used in Pattern Matching 
- pattern matching can also be done on user defined datatypes
- pattern matching can be done on constructors of datatypes

*example*
```haskell
data Tree = Leaf Int | Node (Int, Tree, Tree)

sum (Leaf n) = n
sum (Node(n, left, right)) = n + sum(t1) + sum(t2)
```

## Case Expression 
```haskell
-- datatype 
data Exp = Var Int | Const Int | Plus (Exp, Exp)

-- case expression
case e of 
	Var n -> ...
	Const n -> ...
	Plus(e1, e2) -> ...
```