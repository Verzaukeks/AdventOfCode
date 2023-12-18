import qualified Data.Array as A
import qualified Data.Set as S
import qualified Data.Heap as H

main :: IO ()
main = do
    a1
    a2

toArray :: [a] -> A.Array Int a
toArray ls = A.listArray (0, length ls - 1) ls

data Dir   = Up | Do | Le | Ri deriving (Eq, Ord)
type City  = A.Array Int (A.Array Int Int)
type Cache = S.Set (Int,Int,Dir,Int)
type Queue = H.MinHeap (Int,Int,Int,Dir,Int)

move :: (Int,Int) -> Dir -> (Int,Int)
move (x,y) Up = (x, y-1)
move (x,y) Do = (x, y+1)
move (x,y) Le = (x-1, y)
move (x,y) Ri = (x+1, y)

opposite :: Dir -> Dir
opposite Up = Do
opposite Do = Up
opposite Le = Ri
opposite Ri = Le

getInput :: IO City
getInput = do
    content <- lines <$> readFile "y2023/inputs/d17"
    return (toArray $ toArray . fmap (read . return) <$> content)

search :: City -> Int
search city = call (S.empty, H.singleton (0,0,0,Ri,0))
    where
        (_,height) = A.bounds city
        (_,width)  = A.bounds (city A.! 0)
        inBounds (x,y) = 0 <= x && x <= width && 0 <= y && y <= height

        call :: (Cache,Queue) -> Int
        call (c,pq) | x == width && y == height = loss
                    | otherwise                 = call (foldl update (c,pqq) [Up,Do,Le,Ri])
            where
                Just ((loss,x,y,dir,dirCnt),pqq) = H.view pq

                update :: (Cache,Queue) -> Dir -> (Cache,Queue)
                update (c,pq) d | opposite d == dir            = (c, pq)
                                | not (inBounds (x',y'))       = (c, pq)
                                | dirCnt > 3                   = (c, pq)
                                | S.member (x',y',d,dirCnt') c = (c, pq)
                                | otherwise                    = (S.insert (x',y',d,dirCnt') c, H.insert (loss',x',y',d,dirCnt') pq)
                    where
                        (x',y') = move (x,y) d
                        loss'   = loss + city A.! y' A.! x'
                        dirCnt' = if d == dir then dirCnt + 1 else 1

a1 :: IO ()
a1 = do
    inp <- getInput
    print $ search inp

a2 :: IO ()
a2 = do
    inp <- getInput
    print $ search2 inp

search2 :: City -> Int
search2 city = call (S.empty, H.singleton (0,0,0,Ri,0))
    where
        (_,height) = A.bounds city
        (_,width)  = A.bounds (city A.! 0)
        inBounds (x,y) = 0 <= x && x <= width && 0 <= y && y <= height

        call :: (Cache,Queue) -> Int
        call (c,pq) | x == width && y == height && dirCnt >= 3 = loss -- condition added
                    | otherwise                                = call (foldl update (c,pqq) [Up,Do,Le,Ri])
            where
                Just ((loss,x,y,dir,dirCnt),pqq) = H.view pq

                update :: (Cache,Queue) -> Dir -> (Cache,Queue)
                update (c,pq) d | opposite d == dir            = (c, pq)
                                | not (inBounds (x',y'))       = (c, pq)
                                | dirCnt < 4 && d /= dir       = (c, pq) -- line added
                                | dirCnt > 10                  = (c, pq) -- condition changed
                                | S.member (x',y',d,dirCnt') c = (c, pq)
                                | otherwise                    = (S.insert (x',y',d,dirCnt') c, H.insert (loss',x',y',d,dirCnt') pq)
                    where
                        (x',y') = move (x,y) d
                        loss'   = loss + city A.! y' A.! x'
                        dirCnt' = if d == dir then dirCnt + 1 else 1
