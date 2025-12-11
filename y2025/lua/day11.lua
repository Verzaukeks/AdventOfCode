

local function readGraph()
    local graph = {}
    local file = io.open("../inputs/d11.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end

        local colon = line:find(":")
        local key = line:sub(1, colon - 1)
        local list = {}

        list.visited = false

        for out in line:sub(colon + 1):gmatch("%w+") do
            table.insert(list, out)
        end

        graph[key] = list
    end

    return graph
end


local function a1()
    local graph = readGraph()

    local function dfs(i)
        if i == "out" then return 1 end
        local res = 0
        for _, j in ipairs(graph[i]) do
            res = res + dfs(j)
        end
        return res
    end

    print(dfs("you"))
end


local function a2()
    local graph = readGraph()
    local topsort = {}

    graph["out"] = {}

    -- topsort
    local function dfs(i)
        if graph[i].visited then return end
        graph[i].visited = true
        for _, j in ipairs(graph[i]) do dfs(j) end
        table.insert(topsort, i)
    end

    dfs("svr")
    
    for i = 1, #topsort // 2 do
        local j = #topsort - i + 1
        topsort[i], topsort[j] = topsort[j], topsort[i]
    end

    -- initialize
    for _, node in pairs(graph) do
        node.none = 0
        node.dac = 0
        node.fft = 0
        node.both = 0
    end

    graph["svr"].none = 1

    -- "bfs"
    for _, name in pairs(topsort) do
        local node = graph[name]

        if name == "dac" then
            node.dac = node.dac + node.none
            node.both = node.both + node.fft
            node.none, node.fft = 0, 0
        end

        if name == "fft" then
            node.fft = node.fft + node.none
            node.both = node.both + node.dac
            node.none, node.dac = 0, 0
        end

        for _, j in ipairs(node) do
            local next = graph[j]
            next.none = next.none + node.none
            next.dac = next.dac + node.dac
            next.fft = next.fft + node.fft
            next.both = next.both + node.both
        end
    end

    print(graph["out"].both)
end


a1()
a2()
