# Haskell Polymorphism
Haskell way to [[Polymorphism]]

## Overloading
Some function are **fully polymorphic**
```haskell
length :: [w] -> Int
```

Many useeful functions are less polymorphic
```haskell
member :: [w] -> w -> Bool
```
Membership only works for types that support equalities (to see if an element is in a list it have to be possible to check if the element is equal to some element in the list). 
For example it can't be possible to check the membership of a function to a list of function because the equality of function is undefined.

```
sort :: [w] -> [w]
```
Sorting works only for types that support ordering. More strict than membership since it is not enough to check equality, it has to be possible to determine the order of elements. 

### Overloading Arithmetic, Take 1
What if we allow functions to contain overloaded symbols?
```
square x = x * x 
```
Since `*` is overloaded `square` has two possible types: `Int -> Int` and `Float -> Float` 

The previous example makes sense but consider: 
``` 
squares (x, y, z) = (square x, square y, square z)
```
Now each parameter (`x`, `y`, `z`) have two types, which means that `squares` have $2^8$ types. 

This approach is not widely used because of the exponenial growth in number of versions. With type inference the compiler would have to infer all the possible types!

### Overloading Arithmetic, Take 2
Basic operations such as `+` and `*` can be overloaded **but not functions defined from them**
```Ocaml
3 * 3 -- legal
3.14 * 2.72 -- legal

square x = x * x
square :: Int -> Int

square 3 -- legal
square 3.4 -- illegal
```
Standard ML uses this approach. 
**This is ugly**: programmer cannot define functions that implementation might support
- the user can't choose to use `*` for floats in the user defined function because of type checking, not because of the semantics!

 ### Overloading Equality, Take 1
 Equality defined only for types that admit equality: types non containing function of abstract types
 - equality among functions is undefined 
 - abstract data types are conceptually abstract, not allowed to open them and check equalities

Overloaded equality is like arithmetic ops `+` and `*` in Standard ML and the default type was `(==) : Int -> Int`. 
With this approach we have the same problems: can't override `(==)` inside user defined functions
```OCaml
2 == 3 -- False, legal
'a' == 'a' -- True, legal

member [] y = False
member (x:xs) y = (x == y) || member xs y

member [1, 2, 3] 3 -- ok if defualt is Int
member "Haskell" 'k' -- illegal
```

### Overloading Equality, Take 2
Make type of equality fully polymorphic: 
```
(==) :: a -> a -> Bool
```

As a result, for example, the membership is fully polymorphic
```
member :: [a] -> a -> Bool
```
- the type checker accepts equality even between functions

This approach is adopted by the language Miranda
- equality applied to functions yields a runtime error
- equality applied to an abstract type compares the underlying representation, which violates abstraction principle. 

This is *not optimal*
- at static time we know that the equality between functions will result in a runtime error, but we still allow it
- we violate the semantics of abstract types by looking at the underlying representation

### Overaloading Equality, Take 3
Make equality polymorphic **in a limited way**: 
```
(==) :: a(==) -> a(==) -> Bool
```
where `a(==)` is type variable restricted to **types with equality**

Now the membership has the following type:
```
member :: a(==) -> [a(==)] -> Bool
```

This is the approach used in Standard ML toady, where the type `a(==)` is called **eqtype variable** and is written `"a` (while the normal type variables are written `'a'`)

## Type Classes
Type Classes solve all the problems we have seen, the idea is to generalize the ML's eqtypes to arbitrary types.

**Type Classes:** 
- provide concise types to describe overloaded functions, so no exponential blow-up
- allow users to define functions using overloaded operations
- allow users to declare new collections of overloaded functions: equality and arithmetic operators are not privileged built-ins
- fit within the type inference framework 

### Intuition
To a function that sort lists can be passed as an argument a comparison operator:
```haskell
qsort :: (a -> a -> Bool) -> [a] -> [a]
qsort cmp [] = []
qsort cmp (x:xs) = 
	-- recursevly call on elements smaller than the pivot
	qsort cmp (filter (cmp x) xs) 
	-- put the pivot in the right place
	++ [x] ++
	-- recursevly call on elements bigger that the pivot
	qsort cmp (filter (not cmp x) xs)
```
In this way `qsort` is fully polymorphic and generic: this code is usable for every type if it is possible to define the comparator for that type. 
*We can build on this: type classes have a similar idea*

Consider the following "overloaded" parabola function
```haskell
parabola x = (x * x) + x
```

We can rewrite the function to take the operators it contains as arguments:
```haskell
parabola' (plus, times) x = plus (times x x) x
```
- the extra parameter `(plus, times)` is a *dictionary* that provides implementations for the overloaded operators. 
- we have to rewrite all calls to pass appropriate implementations for `plus` and `times`
```haskell
y = parabola'(intPlus, intTimes) 10
```

### Systematic Programming Style
Type Classes actualize the intuition of the previous section

```haskell
-- dictionary type
data MathDict a = MkMathDict (a -> a -> a) (a -> a -> a)
```
We define a new type of data `MathDict` that recieve a data type `a` as a parameter. 
`MkMathDict` is a *type constructor* that recieve 2 functions as parameters: the first function will be `plus` and the second function will be `times` (of the right type of `a`)

**Type Constructors** are functions from type to type: they take a type and they generates a new type starting from the passed type. 

```haskell
-- accessor functions: retrieve the 
-- functions from the dictionary
get_plus :: MathDict a -> (a -> a -> a)
get_plus (MkMathDict p t) = p

get_times :: MathDict a -> (a -> a -> a)
get_times (MkMathDict p t) = t
```

*Dictionary-Passing Style*
```haskell
parabola :: MathDict a -> a -> a
parabola dict x = 
	let plus = get_plus dict
		times = get_times dict
	in plus (times x x) x
```

Once we have defined `MathDict` we can construct dictionaries very easly
```haskell
intDict = MkMathDict intPlus intTimes
floatDict = MkMathDIct floatPlus floatTimes
```

And now we can pass dictionaries
```haskell
y = parabola intDict 10
z = parabola floatDict 3.14
```
In reality *the programmer do not pass the instance of the dictionary* because the compiler, from the type of the second parameter of `parabola` insert the right type of dictionary at compile time!

### Type Class Declarations
Type class define a set of possible operations on the parameter of the passed type. 
*Type classes are conceptually similar to java interfaces: they specify a type and operations that have to be implemented.* 
When we define a type, using the keyword `class` we automatically generate the accessors to the dictionary and the type of the dictionary, based on the passed type. 

*example:* the (predefined) Type Class `Eq a`, that define the operators 
- `(==) :: a -> a -> Bool`
- `(\=) :: a -> a -> Bool` 
Two "vars" can be confronted with equality if they "implement" the class `Eq` (meaning they specialize, override the body of `(==)` and `(\=)`)

**An example of Type Class Declaration is the following:**
```haskell
class Num a where
	(+) :: a -> a -> a
	(*) :: a -> a -> a
	negate :: a -> a
	...
```

### Type Class Instance Declarations
An instance specify the implementations for a particular type.
When we instance a type class it is required to implement the operations declared in the dictionary. 

Haskell Type Classes allow [[Polymorphism#Ad Hoc Polymorphism]] because this is overloading: the user can define multiple implementations of the function `(==)` which are distinguished not by the number of parameters but by the type of the parameters

For example, for the instance `Int` the `(==)` is defined to be the integer equality

**An example of Type Class Instance Declaration**
```haskell
instance Num Int where
	a + b = intPlus a b
	a * b = intTimes a b
	negate a = intNeg a
	...
```
In this case we define `Int` which is an instance of the type class `Num`. `Int` provides the imlementation for the operator `+` and `*` on integers. 

*An instance declaration for a type T says how the operations are implemented*

### Qualified Types
Concisely express the operations required on otherwise polymorphic types.
*for all types `w` that support the `Eq` operations*:
```haskell
member :: Eq w => w -> [w] -> Bool
```
- `w` is a qualified type: a generic type under a constrain, *the type `w` is qualified by the constraint*

### Compiling Type Classes
When the developer writes
```haskell
square :: Num n => n -> n
square x = x * x
```
The compiler actually generates the following (conceptual) code
```haskell
square :: Num n => n -> n
square d x = (*) d x x
```

When a function has constraints the function is compiled to recieve an additional parameter: the dictionary that contains the actual implementation of the overloaded symbol. 
The body of the function is modified with `(*)` defined in the type class. 

The class declaration:
```haskell
class Num n where 
	(+) :: n -> n -> n
	(*) :: n -> n -> n
	negate :: n -> n
	...
```
is translated (compiled) to: 
```haskell
-- data type declaration
data Num n = 
	MkNum -- constructor
		(n -> n -> n) -- (+)
		(n -> n -> n) -- (*)
		(n -> n) -- negate
		....
```
```haskell 
-- selector function for each class operation
(*) :: Num n => n -> n -> n
(*) :: (MkNum _ m _ ...) = m
```
*A value of type `(Num n)` is a dictionary of the `Num` operations for the type n *

### Compiling Instance Declarations
The type class instance:
```haskell
instance Num Int where
	a + b = intPlus a b
	a * b = intTimes a b
	negate a = intNeg a
	...
```
is compiled as follow:
```haskell
dNumInt :: Num Int
dNumInt = MkNum intPlus intTimes intNeg ...
```
it is simply called the type constructor `MkNum` passing as operations the implementations as parameters (obv in the right order)

### Summary so Far
- each overloaded symbol has to be introduced in at least one type class (if it appear in more that one the developer has to explitly clarify the ambiguity)
- the compiler translates each function that uses an overloaded symbol into a function with an extra parameter: the dictionary
- refences to overloaded symbols are rewritten by the compiler to lookup the symbol in the dictionary
- *the compiler converts each type class declaration into a dictionary type declaration and a set of selector functions*
- *the compiler converts each instance declaration into a dictionary of the appropriate type* 
- *the compiler rewrites calls to overloaded functions to pass a dictionary. It uses the static qualified type of the function to select the right dictionary*

### Compositionality
We can build compound instances from simpler ones
```haskell
class Eq a where
	(==) :: a -> a -> Bool

instance Eq Int where
	(==) = intEq -- intEq is the primitive equality

{-
haskell has composit types (pair, triples, lists), the instance of Eq can be implemented from the less complex instances. 

If the element of a pair is an instance of Eq than we can define an instance of Eq (a, b) 
-}
instance (Eq a, Eq b) => Eq(a, b)
	(u, v) == (x, y) = (u == x) && (v == y)

-- writing Eq a => we tell the compiler that we use the implementation of (==) of Eq a in the current implementation
instance Eq a => Eq [a] where
	(==) [] [] = True
	(==) (x:xs) (y:ys) = x == y && xs = ys
	(==) _ _ = False
```

#### Compound Translation
The following code:
```haskell
class Eq a where
	(==) :: a -> a -> Bool

instance Eq a => Eq [a] where
	(==) [] [] = True
	(==) (x:xs) (y:ys) = x == y && xs = ys
	(==) _ _ = False
```
is translated to:
```haskell
-- dictionary type
data Eq = MkEq (a -> a -> Bool)
-- selector function
(==) (MkEq eq) = eq

-- list dictionary
dEqList :: Eq a -> Eq [a]
dEqList d = MkEq eql
	where
		eql [] [] = True
		eql (x:xs) (y:ys) = (==) d x y && eql xs ys
		eql _ _ = False
```

There are many type classes that can be used to define compound instances (`Eq`, `Ord`, `Num`, `Show`, ...)

#### Subclasses
We could treat the `Eq` and `Num` type classes separately
```haskell
-- memsq is member squared
memsq :: (Eq a, Num a) => a -> [a] -> Bool 
memsq x xs = member (square x) xs
```
but we expect that any type supporting `Num` also supports `Eq`. 

A *subclass declaration* express this relationship:
```haskell
class Eq a => Num a where
	(+) :: a -> a -> a
	(*) :: a -> a -> a
```
`Num` is surely a subclass of `Eq` because it implements `(\=)` and `(==)`

With the previous declaration we can simplify the type of the function
```haskell
memsq :: Num a => a -> [a] -> Bool
memsq x xs = member (square x) xs
```
as qualified type we keep the more strict one, in this way we also need just one dictionary (of `Num`) because it also contains the implementation of `(==)` and `(\=)` imposed by `Eq`

#### Deriving
For type classes `Read`, `Show`, `Bounded`, `Enum`, `Eq` and `Ord` the compiler can generate instance declarations automatically: it can automatically provide default implementations for the operations of the type classes

*example:*
```haskell
	data Color = Red | Green | Blue
		deriving (Show, Read, Eq, Ord)
```

**ad-hoc**: derivations apply only to types where derivation code works