# List Comprehension
List Comprehension is a notation for constructing new lists from old ones. 
It is a concept analogous to set comprehension in math: 
$\{x | x \in A \land x > 6\}$ 

```haskell
Prelude> myData = [1, 2, 3, 4, 5, 6, 7]

-- exploits myData to build another list
Prelude> twiceData = [2 * x | x <- myData]
Prelude> twiceData
[2, 4, 6, 8, 10, 12, 14]

-- more predicates, in and 
Prelude> twiceEvenData = [2 * x | x <- myDara, x `mod` 2 == 0]
Prelude> twiceEvenData
[4, 8, 12]

{-
we can have multiple iterators: for x 
each  we iterate on every y
-}
Prelude> [x * y | x <- [2, 5, 10], y <- [8, 10, 11]]
[16, 20, 22, 40, 50, 55, 80, 100, 110]

{-
we can exploit list comprehension with functions: for each
element in the list xs (we don't care _ what this element is) 
we put 1 in a list, then we make the sum of the list
-}
length xs = sum [1 | _ <- xs]

-- remember that strings are list of chars
removeNonUppercase st = [c | c <- st, c `elem` ['A'..'Z']]
```