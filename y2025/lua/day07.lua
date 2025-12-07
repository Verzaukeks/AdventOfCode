

local function readDiagram()
    local diagram = {}
    local file = io.open("../inputs/d07.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end
        
        local row = {}
        for char in line:gmatch(".") do
            table.insert(row, char)
        end

        table.insert(diagram, row)
    end

    return diagram
end


local function a1()
    local diagram = readDiagram()
    local result = 0

    for i = 1, #diagram - 1 do
        local curr = diagram[i]
        local next = diagram[i + 1]
        
        for j = 1, #curr do
            if curr[j] == "S" then
                if next[j] == "^" then
                    result = result + 1
                    if j > 1 then next[j - 1] = "S" end
                    if j < #curr then next[j + 1] = "S" end
                else
                    next[j] = "S"
                end
            end
        end
    end

    print(result)
end


local function getNumber(value)
    if value == "S" then return 1 end
    if type(value) == "number" then return value end
    return 0
end


local function a2()
    local diagram = readDiagram()
    local result = 0

    for i = 1, #diagram - 1 do
        local curr = diagram[i]
        local next = diagram[i + 1]
        
        for j = 1, #curr do
            local num = getNumber(curr[j])
            if num ~= 0 then
                if next[j] == "^" then
                    if j > 1 then next[j - 1] = getNumber(next[j - 1]) + num end
                    if j < #curr then next[j + 1] = getNumber(next[j + 1]) + num end
                else
                    next[j] = getNumber(next[j]) + num
                end
            end
        end
    end

    for _, value in pairs(diagram[#diagram]) do
        result = result + getNumber(value)
    end

    print(result)
end


a1()
a2()