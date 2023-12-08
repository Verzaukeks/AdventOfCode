import Data.Map ((!))
import qualified Data.Map as Map
import qualified Text.Parsec as P

main :: IO ()
main = do
    a1
    a2

type Nodes = Map.Map String (String, String)

parser :: P.Parsec String () (String, Nodes)
parser = do
    instructions <- P.manyTill P.anyChar (P.string "\n\n")
    res <- P.many $ do
        key <- P.manyTill P.anyChar (P.string " = (")
        l   <- P.manyTill P.anyChar (P.string ", ")
        r   <- P.manyTill P.anyChar (P.string ")\n")
        return (key, (l, r))
    return (instructions, Map.fromList res)

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d08"
    let Right (instr, nodes) = P.parse parser "" content
    print $ f "AAA" 0 (cycle instr) nodes
        where f "ZZZ" steps       _      _ = steps
              f  curr steps ('L':is) nodes = f (fst $ nodes ! curr) (steps+1) is nodes
              f  curr steps ('R':is) nodes = f (snd $ nodes ! curr) (steps+1) is nodes

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d08"
    let Right (instr, nodes) = P.parse parser "" content
    let starts     = filter (('A'==) . last) (Map.keys nodes)
    let runF start = f start (cycle instr) nodes
    print $ foldl lcm 1 (fmap runF starts)
            where
                f curr (i:is) nodes | last curr == 'Z' = 0
                                    | i == 'L'         = 1 + f (fst $ nodes ! curr) is nodes
                                    | i == 'R'         = 1 + f (snd $ nodes ! curr) is nodes
