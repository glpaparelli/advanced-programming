{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use newtype instead of data" #-}
module Dictionary(
    Dictionary,
    Dictionary.empty,
    Dictionary.values,
    Dictionary.keys,
    Dictionary.insert,
    Dictionary.merge,
    Dictionary.equals,
    Dictionary.lookup,
    Dictionary.insertSingleElement
) where

{-
    check if two dictionaries are equals
    - two dictionaries are equals have exactly the same keys?
        (the two lists of keys are equals if we dont look at the order?)
    - then for each key the associated values are equals? 
        (the lists associated to each key have the same elements \wout taking 
        in consideration duplicates and order?)
-}
instance (Eq k, Eq v) => Eq (Dictionary k v) where
    (==) a b
        | length (keys a) /= length (keys b) || not (equals (keys a) (keys b)) = False
        | otherwise = checkValues a b
            where
                checkValues (Dict []) b = True
                checkValues (Dict ((k, v):ds)) b =
                    case Dictionary.lookup b k of
                        Just l -> equals v l && checkValues (Dict ds) b
                        Nothing -> False
{-
    Dicrionary declaration. A Dictionary is a list of pairs whose
    first component is the key and the second component is the list of
    elements associated with that key. 
    A Dictionary is well-formed if it do not contain two pairs with same key
    (!E k, k', vs, vs' | (k, vs) in Dictionary && (k', vs') in Dictionary)
-}

-- cant just derive Eq since semantically Eq on lists takes account the order of the elemtns
-- while we do not care about the ordering (both of the pairs and the inner lists)
-- type constructor: type -> new type
data Dictionary k v = Dict [(k, [v])] deriving (Show)

-- $ (aka apply) lowed priority: f $ x === f x, to reduce brackets

-- constructor: return the empty Dictionary
empty :: Eq k => Dictionary k v
empty = Dict []

-- SUPPORT --

-- unpack the Dictionary
getDict :: Dictionary k v -> [(k, [v])]
getDict (Dict d) = d

-- check if list1 is contained in l2
listSubset :: Eq a => [a] -> [a] -> Bool
listSubset [] _ = True
listSubset (x:xs) l2
    | x `elem` l2 = listSubset xs l2
    | otherwise  = False

-- check if two lists are our-definition-equals (order and duplicates dont matter)
equals :: Eq a => [a] -> [a] -> Bool
equals l1 l2 = listSubset l1 l2 && listSubset l2 l1

-- DICTIONARY OPERATIONS --

-- return the list of values of the dictionary
values :: Eq k => Dictionary k v -> [v]
values (Dict d) = concatMap snd d

-- return the keys of the dictionary as a list
keys :: Eq k => Dictionary k v -> [k]
keys (Dict []) = []
keys (Dict ((k, v):ds)) = k : keys (Dict ds)

-- insert a value (which is a list) associated to the key k in the dictionary
insert :: Eq k => Dictionary k v -> k -> [v] -> Dictionary k v
insert (Dict []) key vList = Dict [(key, vList)]
insert (Dict ((k, v):ds)) key vList
    | k == key = Dict $ (key, vList ++ v) : ds
    | otherwise =  Dict $ (k, v) : getDict (insert (Dict ds) key vList)

-- insert the single element v with the key k to the dictionary (insert v in the list associated to k)
insertSingleElement :: Eq k => Dictionary k v -> k -> v -> Dictionary k v
insertSingleElement (Dict []) key value = Dict [(key, [value])]
insertSingleElement (Dict ((k, v):ds)) key value
    | k == key = Dict $ (key, value:v) : ds
    | otherwise =  Dict $ (k, v) : getDict (insertSingleElement (Dict ds) key value)

-- return the value (Maybe[b]) associated to k or, 
-- if k is not in the dictonary, Nothing
lookup :: Eq k => Dictionary k v -> k -> Maybe [v]
lookup (Dict dictonary) key = Prelude.lookup key dictonary

-- 
{-
    merge two dictionaries returning a singole dictionary  
    - for each key k of the d1 
        - if d2 has an "entry" (k, v2)
            - then: call merge on the rest of d1 and the updated d2 (add v1 to v2 in d2)
            - else: call merge on the rest of d1 and d2
        
-}
merge :: Eq k => Dictionary k v -> Dictionary k v -> Dictionary k v
merge (Dict []) (Dict d2) = Dict d2
merge (Dict ((k, v):ds)) (Dict d2) = merge (Dict ds) (insert (Dict d2) k v)