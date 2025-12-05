

local function readInput()
    local ranges = {}
    local numbers = {}
    local file = io.open("../inputs/d05.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end
        if line == "" then break end

        local dash = line:find("[-]")

        table.insert(ranges, {
            from = tonumber(line:sub(1, dash - 1)),
            to = tonumber(line:sub(dash + 1, #line)),
        })
    end

    while true do
        local line = file:read()
        if not line then break end

        table.insert(numbers, tonumber(line))
    end

    return ranges, numbers
end


local function a1()
    local ranges, numbers = readInput()
    local result = 0

    for _, number in pairs(numbers) do
        for _, range in pairs(ranges) do
            if range.from <= number and number <= range.to then
                result = result + 1
                break
            end
        end
    end
    
    print(result)
end


local function a2()
    local ranges = readInput()
    local result = 0

    table.sort(ranges, function(a, b)
        if a.from < b.from then return true end
        if a.from > b.from then return false end
        return a.to < b.to
    end)

    local bound = 0

    for _, range in pairs(ranges) do
        if bound < range.to then
            local f = math.max(bound + 1, range.from)
            local t = range.to

            result = result + (t - f + 1)

            bound = t
        end
    end

    print(result)
end


a1()
a2()
