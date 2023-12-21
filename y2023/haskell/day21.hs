import Data.List
import qualified Data.Array as A
import qualified Data.Set as S
import Debug.Trace

main :: IO ()
main = do
    a1
    a2_

toArray :: [a] -> A.Array Int a
toArray ls = A.listArray (0, length ls - 1) ls

type Garden = A.Array Int (A.Array Int Char)
type XY = (Int,Int)

getInput :: IO ([String], Garden)
getInput = do
    content <- lines <$> readFile "y2023/inputs/d21"
    return (content, toArray $ toArray <$> content)

dfs :: Garden -> XY -> Int -> [(Int,Int)]
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
    (_,inp) <- getInput
    let s@(sx,sy) = (65,65)
    let plots = dfs inp s 64
    print $ length $ filter (\(x,y) -> even (abs (x-sx) + abs (y-sy))) plots

a2' :: Int -> IO ()
a2' x = do
    (_,inp) <- getInput
    let s@(sx,sy) = (65,65)
    let plots = dfs' inp s x
    print $ length $ filter (\(x,y) -> even (abs (x-sx) + abs (y-sy))) plots

dfs' :: Garden -> XY -> Int -> [(Int,Int)]
dfs' g s steps = f [s] [(s,steps)]
    where
        f :: [XY] -> [(XY,Int)] -> [XY]
        f set [] = set
        f set ((p,0):ps) = f set ps
        f set (((x,y),i):ps) = let r = f set' ps' in if not (null ps) && i /= snd (head ps) then trace (show i ++ " -> " ++ show (i-1) ++ "  ::  " ++ show (length $ filter (\(x,y) -> even (abs (x-65) + abs (y-65))) set)) r else r
            where
                nb   = filter (\(a,b) -> g A.! (b `mod` 131) A.! (a `mod` 131) /= '#' && (a,b) `notElem` set) [(x-1,y),(x+1,y),(x,y-1),(x,y+1)]
                set' = set ++ nb
                ps'  = ps ++ ((,i-1) <$> nb)

a2_ :: IO ()
a2_ = do
    (raw,inp) <- getInput
    let parition [] = ([],[])
        parition [a] = ([a],[])
        parition (a:b:xs) = let (as,bs) = parition xs in (a:as,b:bs)
    let (raw1,raw2) = parition (concat raw)
    let empties1 = length $ filter (/='#') raw1 -- 7613
    let empties2 = length $ filter (/='#') raw2 -- 7577
    let f evl s@(sx,sy) st = length $ filter (\(x,y) -> evl (abs (x-sx) + abs (y-sy))) $ dfs inp s st

    let (sx,sy) = (65,65)
    let len   = 131
    let steps = 26501365

    let skips = (steps - sx) `div` len -- 202300
    let volume = empties1 * (skips-3)^2 + empties2 * (skips-2)^2 -- already too high :(

    let ov = steps - len * (skips - 1) -- 196
    let t  = f odd (   sx,len-1) (ov - sx - 1)
    let b  = f odd (   sx,    0) (ov - sx - 1)
    let l  = f odd (len-1,   sy) (ov - sy - 1)
    let r  = f odd (    0,   sy) (ov - sy - 1)
    let tl = f even (len-1,len-1) (ov - sx - sy - 2)
    let tr = f even (    0,len-1) (ov - sx - sy - 2)
    let bl = f even (len-1,    0) (ov - sx - sy - 2)
    let br = f even (    0,    0) (ov - sx - sy - 2)
    let circum = t+b+l+r + skips*(tl+tr+bl+br)

    print empties1
    print empties2
    print ov
    putStrLn ""
    print t
    print b
    print l
    print r
    putStrLn ""
    print tl
    print tr
    print bl
    print br
    putStrLn ""
    putStrLn "621494544278648?"
    print volume
    print circum
    print $ volume + circum
    putStrLn "621494544278648?"

{-

    #
   #o#
  #o.o#
 #o.o.o#
#o.o.o.o#
 #o.o.o#
  #o.o#
   #o#
    #

    #
   #\#
  #/.\#
 #/.o.\#
#/.o.o./#
 #\.o./#
  #\./#
   #\#
    #

-}
