import Data.List
import Data.Maybe (fromJust)
import qualified Data.Array as A
import qualified Data.Graph as G

main :: IO ()
main = do
    a1
    a2

type XY = (Int,Int)
type Map = A.Array XY Char
type Links = G.Graph

getInput :: IO Map
getInput = do
    content <- transpose . lines <$> readFile "y2023/inputs/d23"
    let size = length content - 1
    return (A.listArray ((0,0),(size,size)) (concat content))

createLinks :: Map -> (XY, XY, (Links, Int -> ([(XY,Int)], XY, [XY]), XY -> Maybe Int))
createLinks m = (start, end, G.graphFromEdges (travel <$> nodes))
    where
        (_,(w,h)) = A.bounds m
        start     = (1,0)
        end       = (w-1,h)

        nodes = start : [(x,y) | ((x,y),c) <- A.assocs m, c == '.', 0 < x && x < w, 0 < y && y < h, length (filter (\nb -> m A.! nb `elem` "<>v") [(x-1,y),(x+1,y),(x,y-1),(x,y+1)]) > 1] ++ [end]

        travel :: XY -> ([(XY,Int)], XY, [XY])
        travel xy@(x,y) = (paths, xy, map fst paths)
            where
                paths
                  | xy == start = [path xy (1,1) 1]
                  | xy == end   = []
                  | otherwise   = [path xy nb 1 | nb <- [(x+1,y),(x,y+1)], m A.! nb `elem` ">v"]

                path :: XY -> XY -> Int -> (XY,Int)
                path prev xy@(x,y) d | xy `elem` nodes = (xy,d)
                                     | otherwise       = path xy (head [nb | nb <- [(x-1,y),(x+1,y),(x,y-1),(x,y+1)], nb /= prev, m A.! nb /= '#']) (d+1)

dfs :: Int -> Int -> Links -> (Int -> Int -> Int) -> Int
dfs s e g dist = f [] s 0
    where
        f :: [Int] -> Int -> Int -> Int
        f set i d | i == e    = d
                  | otherwise = maximum $ [f (j:set) j (d + dist i j) | j <- g A.! i, j `notElem` set] ++ [0]

a1 :: IO ()
a1 = do
    (start, end, (links, fromIdx, toIdx)) <- createLinks <$> getInput
    let s = fromJust (toIdx start)
    let e = fromJust (toIdx end)
    let getDists (a,_,_) = a
    let getXY    (_,b,_) = b
    let getEdgeDist a b = fromJust $ getXY (fromIdx b) `lookup` getDists (fromIdx a)
    print $ dfs s e links getEdgeDist

a2 :: IO ()
a2 = do
    (start, end, (links, fromIdx, toIdx)) <- createLinks' <$> getInput
    let s = fromJust (toIdx start)
    let e = fromJust (toIdx end)
    let getDists (a,_,_) = a
    let getXY    (_,b,_) = b
    let getEdgeDist a b = fromJust $ getXY (fromIdx b) `lookup` getDists (fromIdx a)
    print $ dfs s e links getEdgeDist

createLinks' :: Map -> (XY, XY, (Links, Int -> ([(XY,Int)], XY, [XY]), XY -> Maybe Int))
createLinks' m = (start, end, G.graphFromEdges (travel <$> nodes))
    where
        (_,(w,h)) = A.bounds m
        start     = (1,0)
        end       = (w-1,h)

        nodes = start : [(x,y) | ((x,y),c) <- A.assocs m, c == '.', 0 < x && x < w, 0 < y && y < h, length (filter (\nb -> m A.! nb `elem` "<>v") [(x-1,y),(x+1,y),(x,y-1),(x,y+1)]) > 1] ++ [end]

        travel :: XY -> ([(XY,Int)], XY, [XY])
        travel xy@(x,y) = (paths, xy, map fst paths)
            where
                paths
                  | xy == start = [path xy (1,1) 1]
                  | xy == end   = []
                  | otherwise   = [path xy nb 1 | nb <- [(x+1,y),(x-1,y),(x,y-1),(x,y+1)], m A.! nb /= '#'] -- changed generator and condition

                path :: XY -> XY -> Int -> (XY,Int)
                path prev xy@(x,y) d | xy `elem` nodes = (xy,d)
                                     | otherwise       = path xy (head [nb | nb <- [(x-1,y),(x+1,y),(x,y-1),(x,y+1)], nb /= prev, m A.! nb /= '#']) (d+1)
