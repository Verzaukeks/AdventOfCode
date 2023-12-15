import Data.List (transpose, groupBy)

main :: IO ()
main = do
    a1
    a2

getInput :: IO [String]
getInput = do
    content <- readFile "y2023/inputs/d14"
    return (transpose $ lines content)

weight :: String -> Int
weight l = w (length l) (length l) l
    where w i e [] = 0
          w i e (c:cs) | c == '.' =     w (i-1)  e    cs
                       | c == '#' =     w (i-1) (i-1) cs
                       | c == 'O' = e + w (i-1) (e-1) cs

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ sum $ weight <$> inp

a2 :: IO ()
a2 = do
    inp <- getInput
    let nums = take 300 $ sum . fmap weight2 <$> iterate spinCycle inp
    let cyl  = drop (length nums - cycleLength (reverse nums)) nums
    print $ cyl !! ((1000000000 - length nums) `mod` length cyl)
    print $ (nums ++ cycle cyl) !! 1000000000    -- yeah! did someone say maths?

spinCycle :: [String] -> [String]
spinCycle ls = iterate (spin . tilt) ls !! 4
    where spin = transpose . fmap reverse
          tilt = fmap $ concatMap left . groupBy (\a b -> (a/='#') == (b/='#'))
          left ls = filter (=='#') ls ++ filter (=='O') ls ++ filter (=='.') ls

weight2 :: String -> Int
weight2 l = sum [length l - i | (i,c) <- zip [0..] l, c == 'O']

cycleLength :: [Int] -> Int
cycleLength nums = head [len | len <- [1..length nums], take len nums == take len (drop len nums)]
