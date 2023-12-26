import Data.List
import qualified Data.Array as A
import qualified Data.Map as M

import Control.Monad.Trans.State

main :: IO ()
main = do
    a1
    a2

type RawAdj = [(String, [String])]
type IntAdj = [(Int, [Int])]

getInput :: IO RawAdj
getInput = do
    content <- lines <$> readFile "y2023/inputs/d25"
    let pre     = takeWhile (/=':')
    let suf x   = words $ dropWhile (/=' ') x
    let links l = (pre l, suf l)
    return (links <$> content)

type EnumM = State (Int, M.Map String Int)

enumerate :: RawAdj -> (IntAdj,Int)
enumerate inp = (graph, nodeCount-1)
    where
        (graph, (nodeCount, mapping)) = runState (f inp) (0, M.empty)

        f :: RawAdj -> EnumM IntAdj
        f = mapM $ \(x, xs) -> do
            y  <- index x
            ys <- mapM index xs
            return (y, ys)

index :: String -> EnumM Int
index str = do
    (i,m) <- get
    if str `M.member` m
    then return (m M.! str)
    else do
        put (i+1, M.insert str i m)
        return i

type Graph = A.Array Int [Int]

undirected :: IntAdj -> Int -> Graph
undirected inp n = A.listArray (0,n) sorted
    where
        unsorted = concatMap (\(x,xs) -> [(x,),(,x)] <*> xs) inp
        sorted   = fmap (fmap snd) $ groupBy (\a b -> fst a == fst b) $ sort unsorted

bfs :: Graph -> Int -> Int
bfs graph node = walk [] [node] 0
    where
        walk :: [Int] -> [Int] -> Int -> Int
        walk prev [] depth = depth
        walk prev xs depth = walk xs bs (depth + 1)
            where
                as = concatMap (graph A.!) xs
                bs = nub as \\ prev

dfs :: Graph -> [Int] -> Int -> Int
dfs graph core node = walk [] [node] 1
    where
        walk :: [Int] -> [Int] -> Int -> Int
        walk prev [] cnt = cnt
        walk prev xs cnt = walk xs bs (cnt + length bs)
            where
                nbs x | x `elem` core = (graph A.! x) \\ core
                      | otherwise     =  graph A.! x
                as = concatMap nbs xs
                bs = (nub as \\ prev) \\ xs

a1 :: IO ()
a1 = do
    (inp,n) <- enumerate <$> getInput
    let graph   = undirected inp n
    let depths  = [(i, bfs graph i) | i <- [0..n-1]]
    -- should probably not work, but it did, lol
    let core    = fmap fst $ take 6 $ sortOn snd depths
    let sizes   = dfs graph core <$> core
    let [s1,s2] = nub sizes
    print $ s1 * s2

a2 :: IO ()
a2 = do
    inp <- getInput
    putStrLn "Merry X-MAS"
