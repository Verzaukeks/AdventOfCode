import Data.Char (isDigit)
import Data.List (isInfixOf)

main :: IO ()
main = do
    a1
    a2

type Pos = (Int, Int)
type NumbersP = [(Int, Pos)]
type SymbolsP = [(Char, Pos)]

parse :: [String] -> (NumbersP, SymbolsP)
parse lines = (numbers, symbols)
    where indexed = zip lines [0..]
          numbers = concatMap getNumbers indexed
          symbols = concatMap getSymbols indexed

getNumbers :: (String, Int) -> NumbersP
getNumbers (l, y) = reverse $ addY $ combine $ mapToInt $ filterDigits $ zip l [0..]
    where filterDigits = filter (isDigit . fst)
          mapToInt     = map (\(a, b) -> (read [a], b))
          addY         = map (\(num, x) -> (num, (x,y)))
          combine      = foldl f []
          f                [] (num, pos) = [(num, pos)]
          f ((anum, apos):as) (num, pos) | apos + 1 == pos = (anum*10 + num, pos) : as
                                         | otherwise       = (num, pos) : (anum, apos) : as

getSymbols :: (String, Int) -> SymbolsP
getSymbols (l, y) = addY $ filterSymbols $ zip l [0..]
    where filterSymbols = filter (not . (\x -> isDigit x || x == '.') . fst)
          addY          = map (\(num, x) -> (num, (x,y)))

spanOf :: (Int, Pos) -> [Pos]
spanOf (num, (px, py)) = [(px+ox, py+oy) | ox <- [-len..1], oy <- [-1..1]]
    where len = length (show num)

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d03"
    let (numbers, symbols) = parse $ lines content
    let symbolsPos = map snd symbols
    print $ sum $ map fst $ filter (any (`elem` symbolsPos) . spanOf) numbers

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d03"
    let (numbers, symbols) = parse $ lines content
    let symbols' = map snd $ filter (('*'==) . fst) symbols
    -- oh god it's horrible
    print $ sum $ map (product . map fst) $ filter ((2==) . length) $ (\spos -> filter (elem spos . spanOf) numbers) <$> symbols'
