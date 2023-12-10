import Data.Array
import Data.List
import Data.Maybe

main :: IO ()
main = do
    a1
    a2

toArray :: [a] -> Array Int a
toArray ls = listArray (0, length ls - 1) ls

type Map = Array Int (Array Int Char)
type Point = (Int, Int)

dfs :: Map -> Point -> Point -> [Point]
dfs m prev c@(cx,cy) | m ! cy ! cx == 'S' = [c]
dfs m prev c@(cx,cy) = c : dfs m c next'
    where chr = m ! cy ! cx
          off (ox,oy) = (cx+ox,cy+oy)
          next '|' = [( 0,-1), ( 0, 1)]
          next '-' = [(-1, 0), ( 1, 0)]
          next 'L' = [( 0,-1), ( 1, 0)]
          next 'J' = [( 0,-1), (-1, 0)]
          next '7' = [( 0, 1), (-1, 0)]
          next 'F' = [( 0, 1), ( 1, 0)]
          next' = head $ filter (/=prev) (off <$> next chr)

findStart :: [[Char]] -> Point
findStart lin = let perY = elemIndex 'S' <$> lin
                    startY = fromJust $ findIndex (/=Nothing) perY
                    startX = fromJust $ perY !! startY
                in (startX, startY)

findNext :: Map -> Point -> Point
findNext m (x,y) | m ! (y-1) ! x `elem` "|7F" = (x,y-1)
                 | m ! (y+1) ! x `elem` "|LJ" = (x,y+1)
                 | m ! y ! (x-1) `elem` "-LF" = (x-1,y)
                 | m ! y ! (x+1) `elem` "-J7" = (x+1,y)

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d10"
    let lin = lines content
    let arr = toArray $ toArray <$> lin
    let start = findStart lin
    let next  = findNext arr start
    print $ flip div 2 . length $ dfs arr start next

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d10"
    let lin = lines content
    let arr = toArray $ toArray <$> lin
    let start = findStart lin
    let next  = findNext arr start
    let points = dfs arr start next
    let area   = twoarea (start:points)
    print $ (area - length points) `div` 2 + 1 -- Pick’s Theorem

twoarea :: [Point] -> Int
twoarea ((ax,ay):b@(bx,by):ps) = (bx-ax)*(by+ay) + twoarea (b:ps)
twoarea _ = 0
