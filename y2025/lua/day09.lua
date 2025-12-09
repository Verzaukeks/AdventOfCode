

local function readTiles()
    local tiles = {}
    local file = io.open("../inputs/d09.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end

        local comma = line:find(",")
        local tile = {
            x = tonumber(line:sub(1, comma - 1)),
            y = tonumber(line:sub(comma + 1)),
        }

        table.insert(tiles, tile)
    end

    return tiles
end


local function a1()
    local tiles = readTiles()
    local result = 0

    for i, tile in pairs(tiles) do
        for j = i + 1, #tiles do
            local dx = math.abs(tile.x - tiles[j].x) + 1
            local dy = math.abs(tile.y - tiles[j].y) + 1
            result = math.max(result, dx * dy)
        end
    end

    print(result)
end


local function a2()
--[[
    the following code is still incorrect for all possible inputs,
    e.g. touching lines:
        1,1
        4,1
        4,3
        3,3
        3,2
        2,2
        2,3
        1,3
--]]
    local tiles = readTiles()
    local result = 0

    for i, tile in pairs(tiles) do
        local next = tiles[1 + (i % #tiles)]
        tile.vertical = (tile.x == next.x)
    end

    for i, tile in pairs(tiles) do
        for j = i + 1, #tiles do
            if tile.vertical == tiles[j].vertical then -- also accepts same corner types (which is not correct, but good enough for now)

                local ok  = true
                local lowX = math.min(tile.x, tiles[j].x)
                local lowY = math.min(tile.y, tiles[j].y)
                local highX = math.max(tile.x, tiles[j].x)
                local highY = math.max(tile.y, tiles[j].y)

                -- check for intersecting line
                -- (assumption: one side of the line touches tiles that are not red/green)
                for k = 1, #tiles - 1 do
                    local a = tiles[k]
                    local b = tiles[k + 1]

                    if
                        a.vertical and
                        a.x > lowX and
                        a.x < highX and
                        math.max(a.y, b.y) > lowY and
                        math.min(a.y, b.y) < highY
                    or
                        not a.vertical and
                        a.y > lowY and
                        a.y < highY and
                        math.max(a.x, b.x) > lowX and
                        math.min(a.x, b.x) < highX
                    then
                        ok = false
                        break
                    end
                end

                if ok then
                    local dx = highX - lowX + 1
                    local dy = highY - lowY + 1
                    result = math.max(result, dx * dy)
                end

            end
        end
    end

    print(result)
end


a1()
a2()
