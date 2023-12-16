import Data.Array
import Data.Set

main :: IO ()
main = do
    a1
    a2

toArray :: [a] -> Array Int a
toArray ls = listArray (0, length ls - 1) ls

type Map = Array Int (Array Int Char)
type Vis = Set Vec
type Vis2 = Set Beam

type Vec = (Int,Int)
data Dir = Up | Do | Le | Ri deriving (Show, Eq, Ord)
data Beam = Beam Vec Dir     deriving (Show, Eq, Ord)

move :: Vec -> Dir -> Vec
move (x,y) Up = (x,y-1)
move (x,y) Do = (x,y+1)
move (x,y) Le = (x-1,y)
move (x,y) Ri = (x+1,y)

getInput :: IO Map
getInput = do
    content <- readFile "y2023/inputs/d16"
    return (toArray $ toArray <$> lines content)

dfs :: Map -> Vis -> Vis2 -> [Beam] -> Int
dfs m v v2 [] = size v
dfs m v v2 (b@(Beam xy@(x,y) dir) : bs) | member b v2       = dfs m v v2 bs
                                        | x < 0 || 109 < x  = dfs m v v2 bs
                                        | y < 0 || 109 < y  = dfs m v v2 bs
                                        | m ! y ! x == '.'  = moves [dir]
                                        | m ! y ! x == '|'  = if dir `elem` [Le, Ri] then moves [Up, Do] else moves [dir]
                                        | m ! y ! x == '-'  = if dir `elem` [Up, Do] then moves [Le, Ri] else moves [dir]
                                        | m ! y ! x == '/'  = case dir of
                                                                   Up -> moves [Ri]
                                                                   Do -> moves [Le]
                                                                   Le -> moves [Do]
                                                                   Ri -> moves [Up]
                                        | m ! y ! x == '\\' = case dir of
                                                                   Up -> moves [Le]
                                                                   Do -> moves [Ri]
                                                                   Le -> moves [Up]
                                                                   Ri -> moves [Do]
    where dmv        = dfs m (insert xy v) (insert b v2)
          moves dirs = dmv (fmap (Beam =<< move xy) dirs ++ bs)

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ dfs inp empty empty [Beam (0,0) Ri]

a2 :: IO ()
a2 = do
    inp <- getInput
    let f xy d = dfs inp empty empty [Beam xy d]
    print $ maximum $ concat [[f (i,0) Do, f (i,109) Up, f (0,i) Ri, f (109,i) Le] | i <- [0..109]]
