

local function readBoxes()
    local boxes = {}
    local file = io.open("../inputs/d08.txt")
    assert(file)

    while true do
        local line = file:read()
        if not line then break end

        local box = {}

        for number in line:gmatch("%d+") do
            table.insert(box, tonumber(number))
        end

        box.x = box[1]
        box.y = box[2]
        box.z = box[3]

        table.insert(boxes, box)
    end

    return boxes
end


local function getEdges(boxes)
    local edges = {}

    for i = 1, #boxes do
        for j = i + 1, #boxes do

            local dx = boxes[i].x - boxes[j].x
            local dy = boxes[i].y - boxes[j].y
            local dz = boxes[i].z - boxes[j].z
            local dd = dx * dx + dy * dy + dz * dz

            table.insert(edges, {
                u = i,
                v = j,
                dd = dd,
            })
        end
    end

    table.sort(edges, function (b1, b2)
        return b1.dd < b2.dd
    end)

    return edges
end


local function uFind(parent, id)
    if parent[id] ~= id then
        parent[id] = uFind(parent, parent[id])
    end
    return parent[id]
end


local function uUnion(parent, size, i, j)
    i = uFind(parent, i)
    j = uFind(parent, j)
    if i == j then return false end
    parent[j] = i
    size[i] = size[i] + size[j]
    size[j] = 0
    return true
end


local function a1()
    local boxes = readBoxes()
    local edges = getEdges(boxes)

    local parent = {}
    local size = {}

    for id = 1, #boxes do
        parent[id] = id
        size[id] = 1
    end

    for i, edge in pairs(edges) do
        uUnion(parent, size, edge.u, edge.v)
        if i == 1000 then break end
    end

    table.sort(size, function (s1, s2) return s1 > s2 end)
    print(size[1] * size[2] * size[3])
end


local function a2()
    local boxes = readBoxes()
    local edges = getEdges(boxes)

    local parent = {}
    local size = {}

    for id = 1, #boxes do
        parent[id] = id
        size[id] = 1
    end

    local connected = 0

    for _, edge in pairs(edges) do
        if uUnion(parent, size, edge.u, edge.v) then
            connected = connected + 1
            if connected == #boxes - 1 then
                local x1 = boxes[edge.u].x
                local x2 = boxes[edge.v].x
                print(x1 * x2)
                return
            end
        end
    end
end


a1()
a2()