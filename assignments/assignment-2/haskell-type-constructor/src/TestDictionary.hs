import Dictionary
    ( Dictionary, empty, equals, keys, insertSingleElement, lookup, merge )
import Data.Char ( toLower )
import Data.List ( sort )
import Text.Printf ( printf )

-- build the ciao string given a string
getCiao :: String -> String
getCiao s = sort (map toLower s)

-- build the dictionary (a list of pairs (ciao(word), [word]))
readDict :: FilePath -> IO (Dictionary String String)
readDict f = do
    fContent <- readFile f
    -- the function insert in the dictionary the single element w with its key (ciao word)
    return (foldl (\accum w -> Dictionary.insertSingleElement accum (getCiao w) w) Dictionary.empty (words fContent))

-- write in a file for each key the lenght of the value in the dictionary
writeDict :: Dictionary String String -> FilePath -> IO ()
writeDict d f = do
    let rs = concatMap (\k -> case Dictionary.lookup d k of
            Nothing -> ""
            Just values -> k ++ "," ++ show (length values) ++ "\n") (keys d)
    writeFile f rs

main :: IO ()
main = do
    d1 <- readDict "../input/anagram.txt"
    d2 <- readDict "../input/anagram-s1.txt"
    d3 <- readDict "../input/anagram-s2.txt"
    d4 <- readDict "../input/margana2.txt"

    putStrLn $ "d1 and d4 have the same keys: " ++ show (Dictionary.equals (Dictionary.keys d1) (Dictionary.keys d4)) 
    putStrLn $ "d1 and d2 are equal: " ++ show (d1 == d4) 
    putStrLn $ "d1 is equal to the merge of d2 and d3: " ++ show (d1 == Dictionary.merge d2 d3)

    writeDict d1 "../output/anag-out.txt"
    writeDict d4 "../output/gana-out.txt"
