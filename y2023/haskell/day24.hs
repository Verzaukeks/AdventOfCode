{-# LANGUAGE QuasiQuotes #-}

import Data.List
import Text.Scanf hiding ((%))
import Data.Ratio ((%), numerator, denominator)
import Data.Maybe (isJust, fromJust)

main :: IO ()
main = do
    a1
    a2

type XYZ = (Integer,Integer,Integer)
type Hail = (XYZ,XYZ)

getX, getY, getZ :: XYZ -> Integer
getX (x,y,z) = x
getY (x,y,z) = y
getZ (x,y,z) = z

minus :: XYZ -> XYZ -> XYZ
minus (x,y,z) (ox,oy,oz) = (x-ox,y-oy,z-oz)

toHail :: String -> Hail
toHail str = ((fromIntegral x, fromIntegral y, fromIntegral z), (fromIntegral vx, fromIntegral vy, fromIntegral vz))
    where Just (x :+ y :+ z :+ vx :+ vy :+ vz :+ _) = scanf [fmt|%d, %d, %d @ %d, %d, %d|] str

toLine :: Hail -> Hail
toLine ((x,y,z),(vx,vy,vz)) = ((x,y,z),(x+vx,y+vy,z+vz))

getInput :: IO [Hail]
getInput = do
    content <- lines <$> readFile "y2023/inputs/d24"
    return (toHail <$> content)

lineIntersect :: Hail -> Hail -> Maybe (Double,Double)
lineIntersect h1 h2 = let d13 = fst h1 `minus` fst h2
                          d43 = snd h2 `minus` fst h2
                          d21 = snd h1 `minus` fst h1
                          un = fromIntegral $ getX d43 * getY d13 - getY d43 * getX d13
                          ud = fromIntegral $ getY d43 * getX d21 - getX d43 * getY d21
                          x = fromIntegral (getX (fst h1)) + un * fromIntegral (getX d21) / ud
                          y = fromIntegral (getY (fst h1)) + un * fromIntegral (getY d21) / ud
                       in if abs ud == 0 then Nothing else Just (x, y)

countIntersections :: [Hail] -> Int
countIntersections = f 0
    where f c [] = c
          f c (h:hs) = f (c + length (filter (g h) hs)) hs
          g h1 h2 = isJust sol && (200000000000000 <= sx && sx <= 400000000000000) && (200000000000000 <= sy && sy <= 400000000000000) && h1posX && h1posY && h2posX && h2posY
            where sol = lineIntersect (toLine h1) (toLine h2)
                  Just (sx,sy) = sol
                  h1posX = (sx - (fromIntegral . getX . fst $ h1)) * signum (fromIntegral . getX . snd $ h1) >= 0
                  h1posY = (sy - (fromIntegral . getY . fst $ h1)) * signum (fromIntegral . getY . snd $ h1) >= 0
                  h2posX = (sx - (fromIntegral . getX . fst $ h2)) * signum (fromIntegral . getX . snd $ h2) >= 0
                  h2posY = (sy - (fromIntegral . getY . fst $ h2)) * signum (fromIntegral . getY . snd $ h2) >= 0

a1 :: IO ()
a1 = do
    hails <- getInput
    print $ countIntersections hails

a2 :: IO ()
a2 = do
    inp <- getInput
    let (h1:h2:h3:_) = inp 
    print $ solve h1 h2 h3

solve :: Hail -> Hail -> Hail -> Integer
solve (p0@(x0,y0,z0),v0) h1 h2 = x0 + y0 + z0 + numerator off
    where
        offset (p,v) = (p `minus` p0, v `minus` v0)
        cross ((x,y,z),(vx,vy,vz)) = (y*vz-z*vy, z*vx-x*vz, x*vy-y*vx)

        o1@((x1,y1,z1),(vx1,vy1,vz1)) = offset h1 :: Hail
        o2@((x2,y2,z2),(vx2,vy2,vz2)) = offset h2 :: Hail

        e1@(e1x, e1y, e1z) = cross o1  :: XYZ
        e2@(e2x, e2y, e2z) = cross o2  :: XYZ
        n@(nx, ny, nz) = cross (e1,e2) :: XYZ

        u1 = (y1 * vx1 - x1 * vy1) % (ny * vx1 - nx * vy1)
        u2 = (y2 * vx2 - x2 * vy2) % (ny * vx2 - nx * vy2)

        f _ _ _ 0 = Nothing
        f ni u pi vi = Just $ 1 % vi * (ni % 1 * u - pi % 1)
        t1 = fromJust . head $ filter (/=Nothing) [f nx u1 x1 vx1, f ny u1 y1 vy1, f nz u1 z1 vz1]
        t2 = fromJust . head $ filter (/=Nothing) [f nx u2 x2 vx2, f ny u2 y2 vy2, f nz u2 z2 vz2]

        off = (nx + ny + nz) % 1 * (u1 * t2 - u2 * t1) / (t2 - t1)
