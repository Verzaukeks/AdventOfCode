import Data.List

main :: IO ()
main = do
    a1
    a2

-- list of amount of galaxies per line
galaxies :: [String] -> [Int]
galaxies = fmap (length . filter (=='#'))

-- sweep through lines (keep track of how many galaxies need to 'travel' through a line)
travel :: Int -> [Int] -> Int
travel c ls = fst $ foldl f (0,0) ls
    where f (acc,gals) 0 = (acc + c * cost gals, gals) -- expand row/column
          f (acc,gals) g = (acc +     cost gals, gals + g)
          cost g = (sum ls - g) * g

a1 :: IO ()
a1 = do
    content <- readFile "y2023/inputs/d11"
    let ls   = lines content
    let rows = travel 2 $ galaxies ls
    let cols = travel 2 $ galaxies (transpose ls)
    print (rows + cols)

a2 :: IO ()
a2 = do
    content <- readFile "y2023/inputs/d11"
    let ls   = lines content
    let rows = travel 1000000 $ galaxies ls
    let cols = travel 1000000 $ galaxies (transpose ls)
    print (rows + cols)
