# Haskell Monads
Often type construcotrs ([[Haskell Polymorphism#Systematic Programming Style]]) can be thought of as defining boxes for values. 

Functors with fmap allow to apply functions inside "boxes" [[Haskell Constructor Classes]]

*Monads are constructors classes introducing operations for:*
- **return**: putting a value into a box
- **bind**: compose functions that return boxed values

## Towards Monads: The Maybe Type Constructor
**Type Constructor**: a generic type with one or more **type variables**
```haskell
data Maybe a = Nothing | Just a
```
- a value of type `Maybe a` is a possibly undefined value of type `a`
- a function `f:: a -> Maybe b` is a partial function from `a` to `b`

*Maybe is used to represent partial functions* (haskell usually handles partial functions with the divergence scenario)
```haskell
max :: Ord a => [a] -> Maybe a
max [] = Nothing
max (x:xs) = Just (foldr (\y z -> if y > z then y else z) x xs)
```

## Composing Partial Functions
```haskell
-- partial functions: lookup in a DB 
father :: Person -> Maybe Person
mother :: Person -> Maybe Person

maternalGrandfather Person -> Maybe Person
maternalGrandfather p = 
	-- mother p can return a person or nothing
	-- we have to "unbox it"
	case mother p of
		Nothing -> Nothing 
		Just mom -> father mom -- nothing or a person
```

*The previous code can easly explode:*
```haskell
getGrandfahters :: Person -> Maybe (Person, Person)
getGrandfathers p = 
	case father p of 
		Nothing -> Nothing
		Just dad -> 
			case father dad of
				Nothing -> Nothing
				Just gf1 -> 
					case mother p of
						Nothing -> Nothing
						Just mom -> 
							case father mom of
								Nothing -> Nothing
								Just gf2 -> Just (gf1, gf2)
```
The code is unreadable: the dumb thing is that we have a lot of cases where we get `Nothing`, in particular if we get a `Nothing` we have to propagate it and we retun an useful result only if everything is defined: *nothing propagation*

**Now we introduce a higher order operator to compose partial functions in order to "propagate" undefinedness automatically:** *the bind operator*
```haskell
-- y "bind" g
(>>=) :: Maybe a -> (a -> Maybe b) -> Maybe b
y >>= g = 
	case y of
		Nothing -> Nothing
		Just x -> g x
```

*bind example:*
```haskell
-- partial functions: lookup in a DB 
father :: Person -> Maybe Person
mother :: Person -> Maybe Person

getGrandfathers :: Person p -> Maybe(Person, Person)
getGrandfathers p = 
	father p >>= 
		(\dad -> father dad >>=
			(\gf1 -> mother p >>=
				(\mom -> father mom >>=
					(\gf2 -> return (gf1, gf2))))
```
- with the bind operator now every line of code is relevant to achieve the result, if something is undefined `Nothing` the bind operator will pass it on 
- if `father p` is `Nothing` the first `>>=` fill return `Nothing` and it will be the parameter to 
  `father dad`, which will return `Nothing`, which will be the paramter of `mother p`, ...

## The Monad Constructor Class and the Maybe Monad
```haskell
class Monad m where
	return :: a -> m a
	(>>=) :: m a -> (a -> m b) -> m b
	-- ... something more
```
where:
- **m** is a type constructor
- **m a** is the type of monadic values, monadic value because `m` is an instance, a value of the `Monad` class

`Monad` is a [[Haskell Constructor Classes]] where `m` is any type constructor (a box, not necessary `Maybe`) and 
- `return` calls the constructor: `m a = Box(a)`
- `(>>=)` behaves as expected

```haskell
instance Monad Maybe where
	return :: a -> Maybe a
	return x = Just x

	(>>=) :: Maybe a -> (a -> Maybe b) -> Maybe b
	y >>= g = case y of
		Nothing -> Nothing
		Just x -> g x
```

In Haskell a monad is represented as a type constructor (call it `m`), a function that builds values of that type (`a -> m a`), and a function that combines values of that type with computations that produce values of that type to produce a new computation for values of that type (`m a -> (a -> m b) -> m b`).

Roughly speaking, the monad type constructor defines a type of computation, the `return` function creates primitive values of that computation type and `>>=` combines computations of that type together to make more complex computations of that type.

## Alternative Syntax
Monadic use makes the syntax a bit heavy and harder to understand, so Haskell has introduces the `do` construct:
```haskell
-- classic syntax
getGrandfathers :: Person p -> Maybe(Person, Person)
getGrandfathers p = 
	father p >>= 
		(\dad -> father dad >>=
			(\gf1 -> mother p >>=
				(\mom -> father mom >>=
					(\gf2 -> return (gf1, gf2))))

-- imperative-style do syntax
getGrandfathers :: Person p -> Maybe(Person, Person)
getGrandfathers p = do
	dad <- father p
	gf1 <- father dad
	mom <- mother p
	gf2 <- father mom
	return (gf1, gf2)
```
*do is syntax sugar for the bind operator*

## Monads as Containers 
```haskell
class Monad m where
	return :: a -> m a
	(>>=) :: m a -> (a -> m b) -> m b
	-- ... something more
```

*The monadic constructor can be seen as a container:* let's see for lists. 
Getting bind for more basic operations
```haskell
-- the "fmap" for functors
map :: (a -> b) -> [a] -> [b]

-- container with a single element
return :: a -> [a] 
return x = [x]

-- flattens two-level containers: [[1,2], [], [4]] = [1, 2, 4]
concat :: [[a]] -> [a]

-- (>>=) applies a non deterministic function fo a list
-- of values of type `a` and it collect all the possible result
-- in a list of type `b`
(>>=) :: [a] -> (a -> [b]) -> [b]
xs >> f = concat(map f xs)
```

## Monads as Computations
```haskell
class Monad m where
	return :: a -> m a
	(>>=) :: m a -> (a -> m b) -> m b
	-- operator "then"
	(>>) :: m a -> m b -> m b
	-- ... something more
```
There is also the operator "then" `(>>)`
- `(>>=)`: arrives `m a`, extract if possible the type valye `a` and obtain the result of type `m b` by applying the function `a -> m b` to the just extracted value
- `(>>)`: return the value of the second monadic value of type `m b` discarding the value of the first monadic value of type `m a`

- a value of type `m a` is a computation returning a value of type `a`: interpretation of a monadic valye `m a` as a computation that produce a result of type `a`
- for any value there is a computation which "does nothing" and produces that result. This is given by the function `return`: given a value `a` the `return` do nothing than produce the monadic value `m a`: this is a computation that do nothing and always terminate
- given two computations `x` and `y` one can form the computation `x >> y` which intuitively "runs" `x`, throws away the result and then runs `y` returning its result
- given computation `x` we can use its result to desice what to do next. Given `f : a -> m b`, the comoutation `x >>= f` runs `x` then applies `f` to its result and runs the resulting computation

- `return`, `bind` and `then` define basic ways to compose computations
- they are used in haskell librareis to define more complex operators and control structures
- if a type constructor defining a libraris of computations in monadic, one get automatically benefit of such libraries

*example:* `Maybe`
- `f: a -> Maybe b` is a partial function 
- `bind` applies a partial function to a possibly undefined value, propagating undefinedness

*example:* Lists
- `f: a -> [b]` is a non-deterministic function
- `bind` applies a non-deterministic function to a list of values, collection all possible results