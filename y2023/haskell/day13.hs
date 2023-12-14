import Data.List

main :: IO ()
main = do
    a1
    a2

getInput :: IO [[String]]
getInput = do
    content <- readFile "y2023/inputs/d13"
    return (filter (not . null . head) $ groupBy (\a b -> null a == null b) $ lines content)

findReflections :: [String] -> Int
findReflections ls = head $ [at | at <- [1..length ls - 1], check (splitAt at ls)] ++ [0]
    where check (l, r) = and $ zipWith (==) (reverse l) r 

a1 :: IO ()
a1 = do
    inp <- getInput
    let rows = sum $ findReflections             <$> inp
    let cols = sum $ findReflections . transpose <$> inp
    print $ rows * 100 + cols

a2 :: IO ()
a2 = do
    inp <- getInput
    let rows = sum $ findReflections2             <$> inp
    let cols = sum $ findReflections2 . transpose <$> inp
    print $ rows * 100 + cols

findReflections2 :: [String] -> Int
findReflections2 ls = head $ [at | at <- [1..length ls - 1], check (splitAt at ls)] ++ [0]
    where check (l, r) = and1 . concat $ zipWith (zipWith (==)) (reverse l) r
          and1 [] = False
          and1 ( True:xs) = and1 xs
          and1 (False:xs) = and  xs   
