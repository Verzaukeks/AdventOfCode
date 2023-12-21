import Data.List
import Control.Monad (foldM)
import Control.Monad.Trans.State (State, modify, execState)
import Debug.Trace (trace)

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



data Signal = Low  { from :: String }
            | High { from :: String }
            deriving Show

isHigh :: Signal -> Bool
isHigh (High _) = True
isHigh _ = False

data ModuleType = Broadcast { name :: String, outs :: [String] }
                | FlipFlop  { name :: String, outs :: [String] }
                | Nand      { name :: String, outs :: [String] }
                deriving Show

isFlipFlop :: ModuleType -> Bool
isFlipFlop (FlipFlop _ _) = True
isFlipFlop _ = False

isNand :: ModuleType -> Bool
isNand (Nand _ _) = True
isNand _ = False

data ModuleState = None
                 | Bit Bool
                 | Bits [(String,Bool)]
                 deriving Show

type Module = (ModuleType,ModuleState)
type Counter = State (Int,Int)

countSignal :: Signal -> Counter ()
countSignal (Low _)  = modify (\(l,h) -> (l+1,h))
countSignal (High _) = modify (\(l,h) -> (l,h+1))

getInput :: IO [ModuleType]
getInput = do
    content <- readFile "y2023/inputs/d20"
    return $ Broadcast "rx" [] : (parse <$> lines content)
    where
        parse (s:str) | s == '%'  = FlipFlop     name  nodes
                      | s == '&'  = Nand         name  nodes
                      | otherwise = Broadcast (s:name) nodes
            where
                name  = takeWhile (/=' ') str
                nodes = tail <$> split ',' (drop 2 $ dropWhile (/='-') str)

createModules :: [ModuleType] -> [Module]
createModules types = create <$> types
    where
        create t@(Broadcast _ _) = (t, None)
        create t@(FlipFlop _ _)  = (t, Bit False)
        create t@(Nand n _)      = (t, Bits $ (,False) . name <$> filter ((n `elem`) . outs) types)

findModule :: String -> [Module] -> (Module, [Module])
findModule n [] = error $ "Could not find module: " ++ n
findModule n (m:ms) | name (fst m) == n = (m,ms)
                    | otherwise         = let (y,ys) = findModule n ms in (y,m:ys)

signal :: Signal -> Module -> Counter (Module,[(Signal,String)])
signal s m@(Broadcast n to, _) = do
    countSignal s
    return (m, (Low n,) <$> to)

signal s@(Low _) (t@(FlipFlop n to), Bit b) = do
    countSignal s
    let b' = not b
    let m' = (t, Bit b')
    let s' = if b' then High n else Low n
    return (m', (s',) <$> to)
signal s@(High _) m@(FlipFlop n _, _) = do
    countSignal s
    return (m, [])

signal s (t@(Nand n to), Bits bs) = do
    countSignal s
    let bs' = (from s, isHigh s) : filter ((/= from s) . fst) bs
    let m'  = (t, Bits bs')
    let s'  = if all snd bs' then Low n else High n
    return (m', (s',) <$> to)

propagate :: [Module] -> [(Signal,String)] -> Counter [Module]
propagate modules [] = return modules
propagate modules ((sig,name):ss) = do
    let (m,ms) = findModule name modules
    (m',response) <- signal sig m
    propagate (m':ms) (ss ++ response)

a1 :: IO ()
a1 = do
    modules <- createModules <$> getInput
    let presses = replicate 1000 [(Low "button", "broadcaster")]
    print $ uncurry (*) $ execState (foldM propagate modules presses) (0,0)

a2 :: IO ()
a2 = do
    -- input are 4 binary counters that reset at different numbers
    -- rx fires when all counters reset at the same moment
    modules <- getInput
    let find n = head $ filter (\m -> name m == n) modules
    let cntrs = getCounterSize modules . find <$> outs (find "broadcaster")
    print $ foldl lcm 1 cntrs

getCounterSize :: [ModuleType] -> ModuleType -> Int
getCounterSize modules = f 1
    where
        f p (FlipFlop n to) = (+)
                (if null ff then 0 else f (p*2) (head ff))
                (if null na then 0 else p)
            where
                to' = filter (\m -> name m `elem` to) modules
                ff = filter isFlipFlop to'
                na = filter isNand to'
