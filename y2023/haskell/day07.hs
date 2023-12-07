import Data.List (nub, sortOn)
import Data.Maybe (fromJust)
import qualified Text.Parsec as P

main :: IO ()
main = do
    a1
    a2

parser :: P.Parsec String () [(String, Int)]
parser = P.many $ do
        hand <- P.manyTill P.anyChar (P.char ' ')
        bet  <- P.manyTill P.digit (P.char '\n')
        return (hand, read bet)

remap :: Eq a => [(a, b)] -> [a] -> [b]
remap m = fmap (fromJust . flip lookup m)

rank :: String -> Int
rank s = rank' (length cnts) (sum cnts) (head cnts) (last cnts)
    where cnts = sortOn ((-1)*) $ fmap (\c -> length $ filter (==c) s) (nub s)
          rank' sz sum fst lst | sz == 0 || fst == sum        = 7   -- five of a kind
                               | fst == sum - 1 && lst == 1   = 6   -- four of a kind
                               | fst == sum - 2 && lst == 2   = 5   -- full house
                               | fst == sum - 2 && lst == 1   = 4   -- three of a kind
                               | sz == 3                      = 3   -- two pair
                               | sum == 4 || fst == 2         = 2   -- one pair
                               | otherwise                    = 1   -- high card

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d07"
    let Right x = P.parse parser "" content
    let y = sortOn (remap (zip "23456789TJQKA" [1..]) . fst) x
    let z = sortOn (rank . fst) y
    print $ sum $ zipWith (*) [1..] (fmap snd z)

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d07"
    let Right x = P.parse parser "" content
    let y = sortOn (remap (zip "J23456789TQKA" [1..]) . fst) x
    let z = sortOn (rank . filter (/='J') . fst) y
    print $ sum $ zipWith (*) [1..] (fmap snd z)
