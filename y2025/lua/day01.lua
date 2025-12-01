

local function readRotations()
    local index = 1
    local rotations = {}
    local file = io.open("../inputs/d01.txt", "r")
    assert(file)
    
    while true do
        local line = file:read()
        if not line then break end

        local number = tonumber(line:sub(2))
        if line:sub(1, 1) == "L" then number = -number end

        rotations[index] = number
        index = index + 1
    end

    return rotations
end


local function a1()
    local rotations = readRotations()
    local result = 0
    local dial = 50

    for i = 1, #rotations do
        dial = (dial + rotations[i]) % 100
        if dial == 0 then result = result + 1 end
    end

    print(result)
end


local function a2()
    local rotations = readRotations()
    local result = 0
    local dial = 50

    for i = 1, #rotations do
        local d = dial
        local rot = rotations[i]
        --[[
        notice that // is floor division not integer division, so -3 // 2 returns -2 = floor(-1.5)
        https://www.lua.org/manual/5.4/manual.html#3.4.1
        that's why i am doing this *thing*
        --]]
        if rot >= 0 then
            result = result + rot // 100
            rot = rot % 100
        else
            result = result + (-rot) // 100
            rot = -((-rot) % 100)
        end

        dial = dial + rot
        if (d ~= 0 and dial < 0)
        or (dial ~= 100 and dial > 100)
        then result = result + 1 end

        dial = dial % 100
        if dial == 0 then result = result + 1 end
    end

    print(result)
end


a1()
a2()
