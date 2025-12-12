

local function readInput()
    local shapes = {}
    local regions = {}
    local file = io.open("../inputs/d12.txt")
    assert(file)

    for _ = 1, 6 do
        local shape = { volume = 0 }
        local line = file:read()
        
        for i = 1, 3 do
            shape[i] = {}
            line = file:read() -- ignore shape index, we have i

            for j = 1, 3 do
                shape[i][j] = line:sub(j,j)

                if shape[i][j] == "#" then
                    shape.volume = shape.volume + 1
                end
            end
        end

        table.insert(shapes, shape)
        line = file:read() -- ignore empty line
    end

    while true do
        local line = file:read()
        if not line then break end
        
        local region = {
            width = tonumber(line:match("(%d+)x")),
            length = tonumber(line:match("x(%d+)")),
            quantities = {},
        }

        for num in line:sub(line:find(":"), -1):gmatch("%d+") do
            table.insert(region.quantities, tonumber(num))
        end

        table.insert(regions, region)
    end

    return shapes, regions
end


local function a1()
    local shapes, regions = readInput()
    local result = 0

    for _, region in pairs(regions) do
        local volume = 0

        for i, quantity in pairs(region.quantities) do
            volume = volume + quantity * shapes[i].volume
        end

        if volume < region.width * region.length then
            result = result + 1
        end

        -- i mean, it is a puzzle, isn't it?
        -- if it works, it works.
    end

    print(result)
end


a1()