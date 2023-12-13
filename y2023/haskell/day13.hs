import Data.List

main :: IO ()
main = do
    a1
    a2

getInput :: IO [[String]]
getInput = do
    content <- readFile "y2023/inputs/d13"
    return (filter (not . null . head) $ groupBy (\a b -> null a == null b) $ lines content)

findReflections :: [String] -> [Int]
findReflections ls = [at | at <- [1..length ls - 1], check (splitAt at ls)]
    where check (l, r) = and $ zipWith (==) (reverse l) r 

maximum0 :: [Int] -> Int
maximum0 [] = 0
maximum0 xs = maximum xs

a1 :: IO ()
a1 = do
    inp <- getInput
    let f    = maximum0 . findReflections
    let rows = sum $ f             <$> inp
    let cols = sum $ f . transpose <$> inp
    print $ rows * 100 + cols

a2 :: IO ()
a2 = do
    inp <- getInput
    let f1   = maximum0 . findReflections
    let f2 x = maximum0 . delete (f1 x) . findReflections2 $ x                          -- new:   smudge  on check
    -- let f2 x = maximum0 $ maximum0 . delete (f1 x) . findReflections <$> mixup x     -- old: generate and check
    let rows = sum $ f2             <$> inp
    let cols = sum $ f2 . transpose <$> inp
    print $ rows * 100 + cols

findReflections2 :: [String] -> [Int]
findReflections2 ls = [at | at <- [1..length ls - 1], check (splitAt at ls)]
    where check (l, r) = and1 . concat $ zipWith (zipWith (==)) (reverse l) r
          and1 [] = True
          and1 ( True:xs) = and1 xs
          and1 (False:xs) = and  xs   

-- mixup :: [String] -> [[String]]
-- mixup ls = itr 0 0
--     where maxI = length ls
--           maxJ = length (head ls)
--           itr i j | i == maxI = []
--                   | j == maxJ = itr (i+1) 0
--                   | otherwise = swapLs i j : itr i (j+1)
--           swapLs i j = take i ls ++ [swapL (ls !! i) j] ++ drop (i+1) ls
--           swapL  l j = take j l  ++ [swapC (l  !! j)  ] ++ drop (j+1) l
--           swapC '#' = '.'
--           swapC '.' = '#'
