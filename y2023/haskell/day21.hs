import Data.List
import qualified Data.Array as A

main :: IO ()
main = do
    a1
    a2

toArray :: [a] -> A.Array Int a
toArray ls = A.listArray (0, length ls - 1) ls

type Garden = A.Array Int (A.Array Int Char)
type XY = (Int,Int)

getInput :: IO Garden
getInput = do
    content <- lines <$> readFile "y2023/inputs/d21"
    return (toArray $ toArray <$> content)

dfs :: Garden -> XY -> Int -> [XY]
dfs g s steps = f [s] [(s,steps)]
    where
        f :: [XY] -> [(XY,Int)] -> [XY]
        f set [] = set
        f set ((p,0):ps) = f set ps
        f set (((x,y),i):ps) = f set' ps'
            where
                inBounds (x,y) = 0 <= x && x < 131 && 0 <= y && y < 131
                nb   = filter (\(a,b) -> inBounds (a,b) && g A.! b A.! a /= '#' && (a,b) `notElem` set) [(x-1,y),(x+1,y),(x,y-1),(x,y+1)]
                set' = set ++ nb
                ps'  = ps ++ ((,i-1) <$> nb)

a1 :: IO ()
a1 = do
    inp <- getInput
    let s@(sx,sy) = (65,65)
    let plots = dfs inp s 64
    print $ length $ filter (\(x,y) -> even (abs (x-sx) + abs (y-sy))) plots

-- https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21
a2 :: IO ()
a2 = do
    inp <- getInput

    let s@(sx,sy) = (65,65)
    let len       = 131
    let steps     = 26501365

    let fields   = dfs inp s 1000
    let fields65 = dfs inp s 65
    let fieldsCr = fields \\ fields65

    let f sel ls = length $ filter (\(x,y) -> sel $ abs (x-sx) + abs(y-sy)) ls

    let fieldsOdd   = f odd  fields
    let fieldsEven  = f even fields
    let cornersOdd  = f odd  fieldsCr
    let cornersEven = f even fieldsCr
    
    let size    = (steps - sx) `div` len -- 202300
    let volume  =   fieldsOdd * (size+1)^2 +  fieldsEven * size^2
    let corners = -cornersOdd * (size+1)   + cornersEven * size  

    print $ volume + corners
