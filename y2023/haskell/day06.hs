import Data.Char (isDigit)
import Data.List (groupBy)
import Text.Regex.Base (getAllTextMatches)
import Text.Regex.TDFA ((=~))

main :: IO ()
main = do
    a1
    a2

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d06"
    let times = read <$> getAllTextMatches (lines content !! 0 =~ "[[:digit:]]+") :: [Int]
    let dists = read <$> getAllTextMatches (lines content !! 1 =~ "[[:digit:]]+") :: [Int]
    print $ product [length [() | ti <- [1..t], ti * (t - ti) > d] | (t, d) <- zip times dists]


a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d06"
    let time = read (filter isDigit (lines content !! 0)) :: Int
    let dist = read (filter isDigit (lines content !! 1)) :: Int
    let det' = sqrt (fromIntegral $ time * time - 4 * dist)
    print $ floor det'
