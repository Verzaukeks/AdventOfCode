

local function readBanks()
    local banks = {}
    local file = io.open("../inputs/d03.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end

        local bank = {}
        for i = 1, #line do
            bank[i] = tonumber(line:sub(i, i))
        end

        banks[#banks + 1] = bank
    end

    return banks
end


local function a1()
    local banks = readBanks()
    local result = 0

    for _, bank in pairs(banks) do

        local before = nil
        local highest = nil
        local after = nil

        for _, num in pairs(bank) do
            if not highest or highest < num then
                before = highest
                highest = num
                after = nil
            elseif not after or after < num then
                after = num
            end
        end

        if after then
            result = result + (highest * 10 + after)
        else
            result = result + (before * 10 + highest)
        end
    end

    print(result)
end


local function a2()
    local banks = readBanks()
    local result = 0

    for _, bank in pairs(banks) do

        local number = {}
        local position = { [0] = 0 }
        -- rightmost number a battery can take
        for i = 1, 12 do
            local pos = #bank + i - 12
            number[i] = bank[pos]
            position[i] = pos
        end

        for i = 1, 12 do
            -- find higher (or equal) leftward number
            for j = position[i] - 1, position[i - 1] + 1, -1 do
                local n = bank[j]
                if n >= number[i] then
                    number[i] = n
                    position[i] = j
                end
            end
        end

        local num = 0
        for i = 1, 12 do
            num = num * 10 + number[i]
        end

        result = result + num
    end

    print(result)
end


a1()
a2()