local bit = require("bit")
local ffi = require("ffi")

ffi.cdef[[
    void *make_lp(int rows, int columns);
    void set_verbose(void *lp, int level);

    int set_int(void *lp, int column, int must_be_integer);

    int set_obj_fn(void *lp, double *row);
    void set_minim(void *lp);

    int add_constraint(void *lp, double *row, int constr_type, double rhs);

    int solve(void *lp);
    double get_var_primalresult(void *lp, int index);
]]

local lp = ffi.load("/usr/lib/lp_solve/liblpsolve55.so")


local function readManuals()
    local manuals = {}
    local file = io.open("../inputs/d10.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end

        local index = 0
        local manual = {
            target = 0,
            buttons = {},
            joltages = {},
        }

        for light in line:gmatch("[.#]") do
            if light == "#" then
                manual.target = bit.bor(manual.target, bit.lshift(1, index))
            end
            index = index + 1
        end

        for list in line:gmatch("%(.-%)") do
            local button = { toggle = 0 }
            for light in list:gmatch("%d+") do
                local l = tonumber(light)
                button.toggle = bit.bor(button.toggle, bit.lshift(1, l))
                button[l + 1] = true
            end
            table.insert(manual.buttons, button)
        end

        for list in line:gmatch("{.+}") do
            for joltage in list:gmatch("%d+") do
                table.insert(manual.joltages, tonumber(joltage))
            end
        end

        table.insert(manuals, manual)
    end

    return manuals
end


local function a1()
    local manuals = readManuals()
    local result = 0

    for _, machine in pairs(manuals) do
        local steps = 10000

        for mask = 0, bit.lshift(1, #machine.buttons) - 1 do
            local t = 0
            local s = 0

            for i, button in pairs(machine.buttons) do
                if bit.band(mask, bit.lshift(1, i - 1)) ~= 0 then
                    t = bit.bxor(t, button.toggle)
                    s = s + 1
                end
            end

            if t == machine.target and s < steps then
                steps = s
            end
        end

        result = result + steps
    end

    print(result)
end


local function a2()
    local manuals = readManuals()
    local result = 0

    for _, machine in pairs(manuals) do
        local nVariables = #machine.buttons
        local model = lp.make_lp(0, nVariables)
        lp.set_verbose(model, 0)

        local objWeights = {0}
        for var = 1, nVariables do
            table.insert(objWeights, 1)
            lp.set_int(model, var, 1)
        end

        local obj = ffi.new("double[" .. tostring(nVariables + 1) .. "]", objWeights)
        lp.set_obj_fn(model, obj)
        lp.set_minim(model)

        -- for id = 1, nVariables do
        --     local row = {0}
        --     for _ = 1, nVariables do table.insert(row, 0) end
        --     row[id] = 1
        --     local c = ffi.new("double[" .. tostring(nVariables + 1) .. "]", row)
        --     lp.add_constraint(model, c, 2 --[[greater equal]], 0)
        -- end

        for id, joltage in pairs(machine.joltages) do
            local row = {0}
            for _, button in ipairs(machine.buttons) do
                if button[id] then table.insert(row, 1)
                else table.insert(row, 0) end
            end
            local c = ffi.new("double[" .. tostring(nVariables + 1) .. "]", row)
            lp.add_constraint(model, c, 3 --[[equal]], joltage)
        end

        lp.solve(model)

        result = result + lp.get_var_primalresult(model, 0) -- variables are 1-indexed, 0 is objective result
    end

    print(result)
end


a1()
a2()
