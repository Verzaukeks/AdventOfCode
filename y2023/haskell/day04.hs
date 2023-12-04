import Data.List (intersect)

main :: IO ()
main = do
    a1
    a2

parse :: String -> Int
parse line = length . (\[a,b] -> a `intersect` b) $ mapToInt . filter (not . null) . split ' ' <$> split '|' (takeAfter ':' line)
    where takeAfter c xs = drop 1 $ dropWhile (/=c) xs
          split c [] = [[]]
          split c (x:xs) | x == c    =      [] : split c xs
                         | otherwise = (x : y) : ys
                                where (y:ys) = split c xs
          mapToInt = fmap read :: [String] -> [Int]

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d04"
    let cards = parse <$> lines content
    print $ sum $ flip div 2 . (2^) <$> cards

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d04"
    let cards = parse <$> lines content
    print $ fst $ foldl f (0, replicate (length cards) 1) cards
        where f (sum, count:cs) card = (sum + count, zipWith (+) (replicate card count ++ repeat 0) cs)
