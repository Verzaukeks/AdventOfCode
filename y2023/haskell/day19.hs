{-# LANGUAGE QuasiQuotes #-}

import Data.List
import Text.Scanf

-- GHC.Utils.Misc
split :: Char -> String -> [String]
split c s = case rest of
                []     -> [chunk]
                _:rest -> chunk : split c rest
    where (chunk, rest) = break (==c) s

main :: IO ()
main = do
    a1
    a2

type Name = String
data Rule = Rule Name [Cond]
data Cond = Cond (Part -> Bool) (RPart -> (RPart,RPart)) Name
data Part = Part { x :: Int, m :: Int, a :: Int, s :: Int }

toRule :: String -> Rule
toRule r = Rule name (toCond <$> conds)
    where (name,conds) = split ',' . init . tail <$> break (=='{') r

findRule :: [Rule] -> String -> Rule
findRule (r@(Rule n _) : rs) name | n == name = r
                                  | otherwise = findRule rs name

toCond :: String -> Cond
toCond c | '<' `elem` c = Cond (sel (<) n0 p0) (       spl p0 (read n0    )) t0
         | '>' `elem` c = Cond (sel (>) n1 p1) (swap $ spl p1 (read n1 + 1)) t1
         | otherwise    = Cond (const True) (,RPart (1,0) (1,0) (1,0) (1,0)) c
    where (p0,(n0,t0)) = (tail <$>) . break (==':') . tail <$> break (=='<') c
          (p1,(n1,t1)) = (tail <$>) . break (==':') . tail <$> break (=='>') c
          getF "x" = x
          getF "m" = m
          getF "a" = a
          getF "s" = s
          sel f n p = flip f (read n) . getF p
          -- 2. star
          spl "x" lt (RPart rx rm ra rs) = let (a,b) = rlcut rx lt in (RPart a rm ra rs, RPart b rm ra rs)
          spl "m" lt (RPart rx rm ra rs) = let (a,b) = rlcut rm lt in (RPart rx a ra rs, RPart rx b ra rs)
          spl "a" lt (RPart rx rm ra rs) = let (a,b) = rlcut ra lt in (RPart rx rm a rs, RPart rx rm b rs)
          spl "s" lt (RPart rx rm ra rs) = let (a,b) = rlcut rs lt in (RPart rx rm ra a, RPart rx rm ra b)
          swap f x = let (a,b) = f x in (b,a)

toPart :: String -> Part
toPart p = Part px pm pa ps
    where Just (px :+ pm :+ pa :+ ps :+ _) = scanf [fmt|{x=%d,m=%d,a=%d,s=%d}|] p

norm :: Part -> Int
norm (Part px pm pa ps) = px + pm + pa + ps

getInput :: IO ([Rule], [Part])
getInput = do
    content <- readFile "y2023/inputs/d19"
    let (a,b) = tail <$> break null (lines content)
    return (toRule <$> a , toPart <$> b)

workflow :: [Rule] -> Rule -> Part -> Bool
workflow rules (Rule "A" _) p = True
workflow rules (Rule "R" _) p = False
workflow rules (Rule  _ cs) p = w cs
    where w (Cond sel _ to : cs) | sel p     = workflow rules (findRule rules to) p
                                 | otherwise = w cs

a1 :: IO ()
a1 = do
    (rules,parts) <- getInput
    let rules' = rules ++ [Rule "A" [], Rule "R" []]
    let inRule = findRule rules "in"
    print $ sum $ norm <$> filter (workflow rules' inRule) parts

a2 :: IO ()
a2 = do
    (rules,_) <- getInput
    let rules' = rules ++ [Rule "A" [], Rule "R" []]
    let inRule = findRule rules "in"
    print $ worksize rules' inRule (RPart (1,4000) (1,4000) (1,4000) (1,4000))

type Range = (Int,Int)
data RPart = RPart { rx :: Range, rm :: Range, ra :: Range, rs :: Range }

rlcut :: Range -> Int -> (Range,Range)
rlcut (a,b) c = (,) (a, min b (c-1)) (max a c, b)

rnorm :: RPart -> Int
rnorm (RPart rx rm ra rs) = product $ map rsize [rx,rm,ra,rs]
    where rsize (a,b) = b - a + 1

rvalid :: RPart -> Bool
rvalid (RPart rx rm ra rs) = all rcheck [rx,rm,ra,rs]
    where rcheck (a,b) = a <= b

worksize :: [Rule] -> Rule -> RPart -> Int
worksize rules (Rule "A" _) p = rnorm p
worksize rules (Rule "R" _) p = 0
worksize rules (Rule  n cs) p = w cs p
    where w :: [Cond] -> RPart -> Int
          w _ rp | not (rvalid rp)  = 0
          w (Cond _ spl to : cs) rp = let (hit,nohit) = spl rp
                                      in worksize rules (findRule rules to) hit + w cs nohit
