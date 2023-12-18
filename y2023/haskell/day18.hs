import Data.List
import Data.Maybe (fromJust)

main :: IO ()
main = do
    a1
    a2

type Point = (Int,Int)
data Cmd   = Cmd Char Int String
    deriving Show

getInput :: IO [Cmd]
getInput = do
    content <- readFile "y2023/inputs/d18"
    let toCmd [a,b,c] = Cmd (head a) (read b) (init $ drop 2 c)
    return (toCmd . words <$> lines content)

polygon :: Point -> [Cmd] -> [Point]
polygon p [] = [p]
polygon (x,y) (Cmd d a cl : cs) = (x,y) : polygon (move d) cs
    where move 'U' = (x, y+a)
          move 'D' = (x, y-a)
          move 'L' = (x-a, y)
          move 'R' = (x+a, y)

area :: [Point] -> Int
area poly = (twoarea poly + circumf poly) `div` 2 + 1
    where twoarea ((ax,ay):b@(bx,by):ps) = (bx-ax)*(by+ay) + twoarea (b:ps)
          twoarea _ = 0
          circumf ((ax,ay):b@(bx,by):ps) = abs (ax-bx) + abs (ay-by) + circumf (b:ps)
          circumf _ = 0

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ area (polygon (0,0) inp)

a2 :: IO ()
a2 = do
    inp <- fmap transform <$> getInput
    print $ area (polygon (0,0) inp)

transform :: Cmd -> Cmd
transform (Cmd _ _ enc) = Cmd (dir $ last enc) (dist $ init enc) ""
    where dir '0' = 'R'
          dir '1' = 'D'
          dir '2' = 'L'
          dir '3' = 'U'
          dist = foldl (\a c -> a * 16 + fromJust (c `elemIndex` "0123456789abcdef")) 0
