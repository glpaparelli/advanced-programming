# Haskell Constructor Classes
[[Haskell Polymorphism#Type Classes]] are predicates over types verified by any instance of the type class. 
The predicate is true if the type implementes the overloded functions defined by the type class: if the type is an instance of the type class. 
e.g: `Eq Int = True` because `Int` is an instance of `Eq`, since `Int` implements the functions `(==)` and (`/=`) defined by `Eq`. 

**Constructor Classes** are predicates over **type constructors**, which are functions from type to type ([[Haskell Polymorphism#Systematic Programming Style]])

*Constructor Classes allow to have overloading on type constructors*, 
consructor classes are the haskell way to generics.

*example 1:*
```haskell
map:: (a -> b) -> [a] -> [b]
map f [] = []
map f (x:xs) = f x : map f xs
```
In a sense `map` is a type constructor: takes in input a list `[a]` and return a list of (maybe different) type `[b]`

*example 2:*
```haskell
-- user defined parametric generic tree
data Tree a = Leaf a | Node(Tree a, Tree a) deriving Show

-- predefined type as Java Optional
data Maybe a = Nothing | Just a deriving show

mapTree :: (a -> b) -> Tree a -> Tree b
mapTree f (Leaf x) = Leaf (f x)
mapTree f (Node(l, r)) = Node (mapTree f l, mapTree f r)

mapMaybe :: (a -> b) -> Maybe a -> Maybe b
mapMaybe f Nothing = Nothing
mapMaybe f (Just x) = Just (f x)
```

## Constructor Classes
All map functions share the same structure, as shown in the previous examples:
```haskell
map:: (a -> b) -> [a] -> [b]
mapTree :: (a -> b) -> Tree a -> Tree b
mapMaybe :: (a -> b) -> Maybe a -> Maybe b
```

**They can all be written as:**
```haskell
fmap :: (a -> b) -> g a -> g b
```
**where** `g` is a *type constructor* variable: it will be binded to a type constructor, and the type constructor will be:
- `[-]` for lists
- `Tree` for trees
- `Maybe` for options

We want to have the same function `fmap`: the implementation to execute depends on the actual constructor that is intantiated with the type constructor variable g at invoation time

**side note:** a functor in math is defined as a map among categories that preserves the structure of categories.

*The shown pattern can be captured in a **constructor class Functor**:*
```haskell
class Functor g where
	fmap :: (a -> b) -> g a -> g b
```
The previous code is simply a **type class** where the predicate is over a **type constructor** rather than on a **type**. 
Since it is a type class we only define `fmap`, it will be the instances to implement it.

**Some Instances of the Functor Constructor Class: **
```haskell
class Functor f where
	fmap :: (a -> b) -> f a -> f b

instance Functor [] where
	fmap = map
	
instance Functor Tree where
	fmap = mapTree

instance Functor Maybe where
	fmap = mapMaybe
```

*Now we can use the overloaded symbol fmap to map over all three kinds of data structure:* the same name `fmap` is overloaded and it is useable on every type constructor for which we have an implementation of `Functor`
```haskell
Prelude> fmap (\x -> x + 1) [1, 2, 3]
[2, 3, 4]
it :: [Integer]

Prelude> fmap (\x -> x + 1) (Node(Leaf 1, Leaf 2))
Node (Leaf 2, Leaf 3)
it :: Tree Integer

Prelude> fmap (\x -> x + 1) (Just 1)
Just 2
it :: Maybe Integer
```

The `Functor` constructor class is part of the standard prelude for Haskell
- constructor classes next level [[Haskell Monads]]