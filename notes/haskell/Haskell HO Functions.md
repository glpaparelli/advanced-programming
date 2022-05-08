# Higher-Order Functions
Higher Order functions are functions that take other functions as arguments or return a function as a result. 

*example:* function as argument
```haskell
applyTo5 :: Num t1 => (t1 -> t2) -> t2
applyTo5 f = f 5

Prelude> applyTo5 succ 
6

{-
(7 +) is a function that has type Num t1 => t1 -> t1 because the first argument of the function (+) is fixed as 7
-}
Prelude> applyTo5 (7 +)
12
```

*example*: function as argument and as result
```haskell
applyTwice :: (a -> a) -> a -> a
applyTwice f x = f (f x)

Prelude> applyTwice (+ 3) 10
16
Prelude> applyTwice (++ "AO") "AO"
"AO AO AO"
```

Higher order functions can be used to support alternative syntax, 
*example*: from functional to stream-like
```haskell
(|>) :: t1 -> (t1 -> t2) -> t2
-- (|>) takes an argument and a function and apply 
-- the function to the argument
(|>) a f = f a

Prelude> length (tail (reverse [1, 2, 3]))
2

Prelude> [1, 2, 3] |> reverse |> tail |> length
2
```

Mind that *any carried function with more than one argument is higher order*: if we apply the function to just one argument it return a function
```haskell
-- f is the partial function obtained by the function (+)
-- applied to only one parameter
Prelude> let f = (+) 5
Prelude> :t f 
Num a => a -> a
```

## Notable Functions
### Map
The function map applies the argument function to each element in a collection 
```haskell
map :: (a -> b) -> [a] -> [b]
map _ [] = []
map f (x:xs) = f x : map f xs
```

### Filter
The function filter takes a collection and a boolean predicate and returns a collection of the elements that satisfy the predicate
```haskell
filter :: (a -> Bool) -> [a] -> [a]
filter _ []
filter p (x:xs)
	| p x = x : filter p xs
	| otherwise = filter p xs
```

### Reduce 
`foldl`, `foldr`: takes a collection, an initial value and a function and combines the elements in the collection according to the function. 
Reduce because it reduce a whole list to a single value. 

#### foldl
```haskell
-- folds values from beginning to end of list
foldl :: Foldable t => (b -> a -> b) -> b -> t a -> b
foldl f z [] = z
foldl f z (x:xs) = foldl f (f z x) xs
```

*example*: reverse a list
```haskell
myReverse :: [a] -> [a]
myReverse = foldl (\acc x -> x : acc) []
```

#### foldr
```haskell
-- folds values from end to beginning of list
foldr :: Foldable t => (a -> b -> b) -> b -> t a -> b
foldr f z [] = z
foldr f z (x:xs) = f x (foldr f z xs)
```

*example*: filter a list
```haskell
myFilter :: (a -> Bool) -> [a] -> [a]
myFilter p = foldr (\x acc > if p then x : acc else acc) []
```
