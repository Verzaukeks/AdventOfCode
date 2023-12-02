import Data.Char (isDigit)
import Data.List (isInfixOf)

main :: IO ()
main = do
    a1
    a2

parse :: String -> [[Int]]
parse game = [getNumbers "red", getNumbers "green", getNumbers "blue"]
    where sets = split "" game
          getNumbers t = toNumbers (filter (isInfixOf t) sets)
          toNumbers = map (read . filter isDigit) :: [String] -> [Int]
          split acc [] = [acc]
          split acc (':' : gs) =       split "" gs
          split acc (',' : gs) = acc : split "" gs
          split acc (';' : gs) = acc : split "" gs
          split acc ( g  : gs) = split (acc ++ [g]) gs

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d02"
    print $ sum $ map fst $ filter (\(id,[r,g,b]) -> r <= 12 && g <= 13 && b <= 14) $ zip [1..] $ map maximum . parse <$> lines content

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d02"
    print $ sum $ product . map maximum . parse <$> lines content
