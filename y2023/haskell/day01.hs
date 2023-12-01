{-# LANGUAGE OverloadedStrings #-}

import Data.Char (isDigit)
import Data.Text (Text, append, cons, replace, pack, unpack)

main :: IO ()
main = do
    a1
    a2

fstLst :: [a] -> [a]
fstLst xs = [head xs, last xs]

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d01"
    print $ sum $ map (read . fstLst . filter isDigit) $ lines content

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d01"
    print $ sum $ map (read . fstLst . filter isDigit . wordToNum) $ lines content

wordToNum :: String -> String
wordToNum str = unpack $ foldr (\(w,n) -> replace w (append w $ cons n w)) (pack str)
    [("one", '1'), ("two", '2'), ("three", '3'), ("four", '4'), ("five", '5'), ("six", '6'), ("seven", '7'), ("eight", '8'), ("nine", '9')]
