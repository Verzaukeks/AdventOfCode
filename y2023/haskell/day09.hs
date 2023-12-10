
main :: IO ()
main = do
    a1
    a2

toIntList :: String -> [Int]
toIntList str = read <$> words str

seqq :: [Int] -> Int
seqq nums | all (==0) nums = 0
          | otherwise      = last nums + seqq (zipWith (-) (drop 1 nums) nums)

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d09"
    print $ sum $ seqq . toIntList <$> lines content

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d09"
    print $ sum $ seqq . reverse . toIntList <$> lines content
