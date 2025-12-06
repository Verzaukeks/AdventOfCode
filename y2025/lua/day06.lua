

local function trim(str)
    local res = str:gsub("%s+", "")
    return res
end


local function readProblems()
    local lines = {}
    local problems = {}
    local file = io.open("../inputs/d06.txt")
    assert(file)

    local prevCol = 0
    local lineWidth = 0

    while true do
        local line = file:read()
        if not line then break end

        table.insert(lines, line)
        lineWidth = math.max(lineWidth, #line)
    end

    for col = 1, lineWidth + 1 do
        local isSeparator = true

        for _, line in pairs(lines) do
            isSeparator = isSeparator and (line:sub(col, col) == " " or line:sub(col, col) == "")
        end

        if isSeparator then
            local problem = {}

            for _, line in pairs(lines) do
                table.insert(problem, line:sub(prevCol + 1, col - 1))
            end

            problem.op = trim(problem[#problem])
            table.remove(problem, #problem)

            prevCol = col
            table.insert(problems, problem)
        end
    end

    return problems
end


local function operationOf(op)
    if op == "+" then
        return 0, function (a, b) return a + b end
    elseif op == "*" then
        return 1, function (a, b) return a * b end
    end
end


local function a1()
    local problems = readProblems()
    local result = 0

    for _, problem in pairs(problems) do
        local r, f = operationOf(problem.op)

        for _, raw in ipairs(problem) do
            local number = tonumber(raw)
            r = f(r, number)
        end

        result = result + r
    end

    print(result)
end


local function a2()
    local problems = readProblems()
    local result = 0

    for _, problem in pairs(problems) do
        local r, f = operationOf(problem.op)
        local colWidth = 0

        for _, line in ipairs(problem) do
            colWidth = math.max(colWidth, #line)
        end

        for col = 1, colWidth do
            local raw = ""

            for _, line in ipairs(problem) do
                raw = raw .. line:sub(col, col) 
            end

            local number = tonumber(trim(raw))
            r = f(r, number)
        end

        result = result + r
    end

    print(result)
end


a1()
a2()