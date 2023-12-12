import Data.Array ((!), listArray)
import Data.List (intercalate)

main :: IO ()
main = do
    a1
    a2

getInput :: IO [(String, [Int])]
getInput = do
    content <- readFile "y2023/inputs/d12"
    let ls = words <$> lines content
    let f [a,b] = (a, read $ '[' : b ++ "]")
    return (f <$> ls)

dp :: (String, [Int]) -> Int
dp (record, nums) = arr ! (0,0)
    where lenR = length record
          lenN = length nums
          arr  = listArray ((0,0),(lenR,lenN)) [f i ni | i <- [0..lenR], ni <- [0..lenN]]
          f i ni | i >= lenR && ni == lenN = 1
                 | i >= lenR               = 0
                 | record !! i == '#'      = f' i ni (nums !! ni)
                 | record !! i == '.'      = arr ! (i+1, ni)
                 | record !! i == '?'      = arr ! (i+1, ni) + f' i ni (nums !! ni)
          f' i ni nni | ni == lenN     = 0 -- no more broken springs available
                      | i + nni > lenR = 0 -- spring longer than available record
                      | '.' `elem` take nni (drop i record) = 0 -- spring has to be functional inside slice
                      | i + nni < lenR = if record !! (i + nni) == '#' then 0 -- record demands longer broken spring
                                         else arr ! (i + nni + 1, ni + 1)
                      | otherwise = arr ! (lenR, ni + 1) -- broken spring reaches till end of record

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ sum (dp <$> inp)

a2 :: IO ()
a2 = do
    inp <- getInput
    let expand (r,n) = (intercalate "?" $ replicate 5 r, concat $ replicate 5 n)
    print $ sum (dp . expand <$> inp)
