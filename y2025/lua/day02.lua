

local function readRanges()
    local ranges = {}
    local file = io.open("../inputs/d02.txt", "r")
    assert(file)

    local line = file:read()
    for range in line:gmatch("%d+[-]%d+") do
        local minus = range:find("[-]")
        ranges[#ranges + 1] = {
            from = range:sub(1, minus - 1),
            to = range:sub(minus + 1),
        }
    end

    return ranges
end


local function a(isInvalid)
    local ranges = readRanges()
    local result = 0

    for _, range in pairs(ranges) do
        local f = tonumber(range.from)
        local t = tonumber(range.to)

        for num = f, t do
            if isInvalid(num) then
                result = result + num
            end
        end
    end

    print(result)
end


local function a1()
    a(function (num)
        local str = tostring(num)
        local len = #str

        if len % 2 == 1 then
            return false
        end

        local prefix = str:sub(1, len // 2)
        local suffix = str:sub(len // 2 + 1)

        return prefix == suffix
    end)
end


local function a2()
    a(function (num)
        local str = tostring(num)
        local len = #str

        for r = 1, len // 2 do
            if len % r == 0 then
                local fix = str:sub(1, r)
                local hits = 1
                local start = 1 + r

                while start <= len do
                    local f = str:sub(start, start + r - 1)

                    if f ~= fix then
                        break
                    end

                    hits = hits + 1
                    start = start + r
                end

                if hits == len // r then
                    return true
                end
            end
        end

        return false
    end)
end


a1()
a2()