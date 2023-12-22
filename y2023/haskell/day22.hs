{-# LANGUAGE QuasiQuotes #-}

import Data.List
import Text.Scanf
import qualified Data.Array as A
import qualified Data.Map.Strict as M

main :: IO ()
main = do
    a1
    a2

type XYZ = (Int,Int,Int)
type Brick = (XYZ,XYZ)

getX, getY, getZ :: XYZ -> Int
getX (x,_,_) = x
getY (_,y,_) = y
getZ (_,_,z) = z

toBrick :: String -> Brick
toBrick str = ((min x0 x1, min y0 y1, min z0 z1), (max x0 x1, max y0 y1, max z0 z1))
    where Just (x0 :+ y0 :+ z0 :+ x1 :+ y1 :+ z1 :+ _) = scanf [fmt|%d,%d,%d~%d,%d,%d|] str

move :: Brick -> XYZ -> Brick
move ((x0,y0,z0),(x1,y1,z1)) (x,y,z) = ((x0+x,y0+y,z0+z),(x1+x,y1+y,z1+z))

isBrickOverBrick :: Brick -> Brick -> Bool
isBrickOverBrick ((ax0,ay0,az0),(ax1,ay1,_)) ((bx0,by0,_),(bx1,by1,bz1)) =
        (ax0,ax1) `intersects` (bx0,bx1) &&
        (ay0,ay1) `intersects` (by0,by1) &&
        az0 > bz1
    where intersects (a0,a1) (b0,b1) = (a0 <= b0 && b0 <= a1) || (b0 <= a0 && a0 <= b1)

isBrickOnBrick :: Brick -> Brick -> Bool
isBrickOnBrick a b = isBrickOverBrick a b && getZ (fst a) == getZ (snd b) + 1

getInput :: IO [Brick]
getInput = do
    content <- lines <$> readFile "y2023/inputs/d22"
    return (toBrick <$> content)

settle :: [Brick] -> [Brick]
settle bs = foldl collapse [] bs_sorted
    where
        bs_sorted = sortOn (getZ . fst) bs
        collapse ls b = b' : ls
            where ls'  = filter (isBrickOverBrick b) ls
                  maxZ = if null ls' then 0 else maximum (getZ . snd <$> ls')
                  b'   = move b (0, 0, maxZ - getZ (fst b) + 1)

a1 :: IO ()
a1 = do
    inp <- getInput
    let settled = settle inp
    let below   = fmap   (\b -> (length $ filter (isBrickOnBrick b) settled, b)) settled
    let safe    = filter (\b -> and [bel > 1 | (bel,br) <- below, isBrickOnBrick br b]) settled
    print $ length safe

a2 :: IO ()
a2 = do
    inp <- getInput
    let sorted  = sortOn (getZ . snd) (settle inp)
    let indexed = zip [0..] sorted
    let adj    = A.listArray (0,length inp-1) [[bi | (bi,bs) <- indexed, isBrickOnBrick b bs] | b <- sorted]
    print $ sum [M.foldl (\a f -> if f == 1.0 then 1 + a else a) (-1) $ flow adj M.empty (i,1.0) | i <- [0..length inp-1]]

type Adj = A.Array Int [Int]
type Map = M.Map Int Double

flow :: Adj -> Map -> (Int,Double) -> Map
flow adj ma (idx,flw) = f (M.insert idx (curr+flw) ma) (adj A.! idx) flw
    where
        curr = M.findWithDefault 0 idx ma
        f :: Map -> [Int] -> Double -> Map
        f m     [] fl =             m
        f m    [i] fl =    flow adj m (i,fl)
        f m (i:is) fl = f (flow adj m (i,fl/2)) is (fl/2)
