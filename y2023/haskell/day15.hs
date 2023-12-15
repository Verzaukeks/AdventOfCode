import Data.Char
import Data.List

-- GHC.Utils.Misc
split :: Char -> String -> [String]
split c s = case rest of
                []     -> [chunk]
                _:rest -> chunk : split c rest
    where (chunk, rest) = break (==c) s

main :: IO ()
main = do
    a1
    a2

data Op = Add { label :: String, lens :: Int }
        | Rem { label :: String }
        deriving Eq

instance Show Op where
  show Add { label, lens } = label ++ "=" ++ show lens
  show Rem { label }       = label ++ "-"

readOp :: String -> Op
readOp s | '=' `elem` s = Add (takeWhile (/='=') s) (read $ dropWhile (not . isDigit) s)
         | '-' `elem` s = Rem (init s)

getInput :: IO [Op]
getInput = do
    content <- readFile "y2023/inputs/d15"
    return (readOp <$> split ',' content)

hash :: String -> Int
hash = foldl (\h c -> 17 * (h + ord c) `mod` 256) 0

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ sum $ hash . show <$> inp

a2 :: IO ()
a2 = do
    inp <- getInput
    print $ sum . zipWith (*) [1..] $ power . filterAdd . filterRem <$> seperate 0 inp

seperate :: Int -> [Op] -> [[Op]]
seperate i [] = []
seperate i ls = as : seperate (i+1) bs
    where (as, bs) = partition ((==i) . hash . label) ls

filterRem :: [Op] -> [Op]
filterRem [] = []
filterRem (Rem l   : bs) = filterRem bs
filterRem (Add l s : bs) | Rem l `elem` bs =           filterRem bs
                         | otherwise       = Add l s : filterRem bs

filterAdd :: [Op] -> [Op]
filterAdd [] = []
filterAdd box@(Add l s : bs) = last ls : filterAdd rs
    where (ls, rs) = partition ((==l) . label) box

power :: [Op] -> Int
power = sum . zipWith (*) [1..] . fmap lens
