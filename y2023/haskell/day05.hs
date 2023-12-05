import qualified Text.Parsec as P

main :: IO ()
main = do
    a1
    a2

parser :: P.Parsec String () ([Int], [[(Int, (Int,Int))]])
parser = do
    seeds <- P.string "seeds: " >> pNumbers
    maps  <- P.many pMap
    return (seeds, rangesOf $ filterEmpty <$> maps)
    where pNumbers = fmap read <$> P.sepBy (P.many1 P.digit) (P.char ' ')
          pMap     = P.manyTill P.anyChar (P.char ':') >> P.many1 (P.char '\n' >> pNumbers)
          filterEmpty = filter (not . null)
          rangesOf    = fmap $ fmap (\[a,b,c] -> (a-b, (b, b+c-1)))

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d05"
    let Right (seeds, maps) = P.parse parser ">:)" content
    print $ minimum $ flip (foldl remap) maps <$> seeds
    where remap id [] = id
          remap id ((off, (rs,re)):ms) | rs <= id && id <= re = id + off
          remap id (_:ms) = remap id ms

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d05"
    let Right (seeds, maps) = P.parse parser ">:)" content
    print $ minimum $ minOf maps <$> toRanges seeds
    where toRanges [] = []
          toRanges (a:b:xs) = (a,a+b) : toRanges xs
          minOf      _ (ra,rb) | ra > rb = maxBound
          minOf     [] (ra, _) = ra
          minOf ([]:ms) range  = minOf ms range
          minOf ( m:ms) range  = minimum
                [ minOf (rr:ms) (range `intersect` (minBound, rs-1))
                , minOf     ms  (range `intersect` (rs, re) `add` off)
                , minOf (rr:ms) (range `intersect` (re+1, maxBound)) ]
                where (off,(rs,re)) : rr = m
          intersect (s1, e1) (s2, e2) = (max s1 s2, min e1 e2)
          add (s, e) off = (s+off, e+off)
