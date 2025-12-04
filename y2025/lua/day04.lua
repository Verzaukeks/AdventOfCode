

local grid = {}
local file = io.open("../inputs/d04.txt")
assert(file)

while true do
    local line = file:read()
    if not line then break end

    local row = {}
    for char in line:gmatch(".") do
        table.insert(row, char)
    end

    table.insert(grid, row)
end
local len = #grid


local function isAccessable(i, j)
    if grid[i][j] ~= "@" then return false end

    local nb = 0

    for oi = -1, 1 do
        for oj = -1, 1 do
            local ii = i + oi
            local jj = j + oj
            if
                (1 <= ii and ii <= len) and
                (1 <= jj and jj <= len) and
                (ii ~= i or jj ~= j) and
                grid[ii][jj] == "@"
            then
                nb = nb + 1
            end
        end
    end

    return nb < 4
end


local function a1()
    local result = 0
    
    for i = 1, len do
        for j = 1, len do
            if isAccessable(i, j) then
                result = result + 1
            end
        end
    end

    print(result)
end


local function a2()
    local result = 0

    repeat
        local res = 0
        local access = {}

        for i = 1, len do
            for j = 1, len do
                if isAccessable(i, j) then
                    res = res + 1
                    table.insert(access, { i = i, j = j })
                end
            end
        end

        for _, ij in pairs(access) do
            grid[ij.i][ij.j] = "."
        end

        result = result + res
    until res == 0

    print(result)
end


a1()
a2()