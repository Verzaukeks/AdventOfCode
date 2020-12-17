package y2020

object Day17 : Day() {

    override val day = 17
    override val name = "Conway Cubes"

    data class Quad(val first: Int, val second: Int, val third: Int, val forth: Int)

    override fun a1() {
        val size = 20 // manually increase until the result no longer changes
        val sizeHalf = size / 2
        val sizeRange = 0 until size
        var mapIn = Array(size) { Array(size) { Array(size) { false } } }
        var mapOut = Array(size) { Array(size) { Array(size) { false } } }

        // create mask to define neighbors
        var maskI = 0
        val mask = Array(26) { Triple(0, 0, 0) }
        for (x in -1..1) for (y in -1..1) for (z in -1..1)
            if (!(x == 0 && y == 0 && z == 0))
                mask[maskI++] = Triple(x, y, z)

        // helpful functions
        val add = { posA: Triple<Int, Int, Int>, posB: Triple<Int, Int, Int> -> Triple(posA.first + posB.first, posA.second + posB.second, posA.third + posB.third) }
        val isValid    = { pos: Triple<Int, Int, Int> -> (pos.first + sizeHalf in sizeRange) && (pos.second + sizeHalf in sizeRange) && (pos.third + sizeHalf in sizeRange) }
        val isValidRaw = { pos: Triple<Int, Int, Int> -> (pos.first            in sizeRange) && (pos.second            in sizeRange) && (pos.third            in sizeRange) }
        val get    = { map: Array<Array<Array<Boolean>>>, pos: Triple<Int, Int, Int> -> map[pos.first + sizeHalf][pos.second + sizeHalf][pos.third + sizeHalf] }
        val getRaw = { map: Array<Array<Array<Boolean>>>, pos: Triple<Int, Int, Int> -> map[pos.first           ][pos.second           ][pos.third           ] }
        val set    = { map: Array<Array<Array<Boolean>>>, pos: Triple<Int, Int, Int>, value: Boolean -> map[pos.first + sizeHalf][pos.second + sizeHalf][pos.third + sizeHalf] = value }
        val setRaw = { map: Array<Array<Array<Boolean>>>, pos: Triple<Int, Int, Int>, value: Boolean -> map[pos.first           ][pos.second           ][pos.third           ] = value }
        val display = { map: Array<Array<Array<Boolean>>> ->
            for (rz in 0 until size) {
                println("z=${rz-sizeHalf}")
                for (ry in 0 until size) {
                    for (rx in 0 until size)
                        print(if (map[rx][ry][rz]) '#' else '.')
                    println()
                }
            }
        }

        // load input into map
        val input = INPUT.readText().replace("\r", "").split("\n")
        val widthHalf = input[0].length / 2
        val heightHalf = input.size / 2
        for (ry in input.indices)
            for (rx in input[ry].indices) {
                val x = rx - widthHalf
                val y = ry - heightHalf - 1
                val z = 0
                val pos = Triple(x, y, z)
                val value = input[ry][rx] == '#'
                set(mapIn, pos, value)
            }
        //display(mapIn)

        // cycle 6 times
        repeat(6) {
            for (rz in sizeRange) for (ry in sizeRange) for (rx in sizeRange) {
                val rawPos = Triple(rx, ry, rz)
                val active = getRaw(mapIn, rawPos)
                var neighbors = 0

                // count neighbors
                for (maskPos in mask) {
                    val pos = add(rawPos, maskPos)
                    if (isValidRaw(pos))
                        if (getRaw(mapIn, pos)) neighbors++
                }

                // apply rules
                if (active) {
                    val value = (neighbors == 2 || neighbors == 3)
                    setRaw(mapOut, rawPos, value)
                } else {
                    val value = (neighbors == 3)
                    setRaw(mapOut, rawPos, value)
                }
            }

            // swap
            val tmp = mapIn
            mapIn = mapOut
            mapOut = tmp

            //println("\n$it:")
            //display(mapIn)
        }

        // output
        var active = 0
        for (rz in sizeRange) for (ry in sizeRange) for (rx in sizeRange)
            if (getRaw(mapIn, Triple(rx, ry, rz))) active ++
        println(active)
    }

    // easily expand to 4 dimensions
    override fun a2() {
        val size = 20 // manually increase until the result no longer changes
        val sizeHalf = size / 2
        val sizeRange = 0 until size
        var mapIn = Array(size) { Array(size) { Array(size) { Array(size) { false } } } }
        var mapOut = Array(size) { Array(size) { Array(size) { Array(size) { false } } } }

        // create mask to define neighbors
        var maskI = 0
        val mask = Array(80) { Quad(0, 0, 0, 0) }
        for (x in -1..1) for (y in -1..1) for (z in -1..1) for (w in -1..1)
            if (!(x == 0 && y == 0 && z == 0 && w == 0))
                mask[maskI++] = Quad(x, y, z, w)

        // helpful functions
        val add = { posA: Quad, posB: Quad -> Quad(posA.first + posB.first, posA.second + posB.second, posA.third + posB.third, posA.forth + posB.forth) }
        val isValid    = { pos: Quad -> (pos.first + sizeHalf in sizeRange) && (pos.second + sizeHalf in sizeRange) && (pos.third + sizeHalf in sizeRange) && (pos.forth + sizeHalf in sizeRange) }
        val isValidRaw = { pos: Quad -> (pos.first            in sizeRange) && (pos.second            in sizeRange) && (pos.third            in sizeRange) && (pos.forth            in sizeRange) }
        val get    = { map: Array<Array<Array<Array<Boolean>>>>, pos: Quad -> map[pos.first + sizeHalf][pos.second + sizeHalf][pos.third + sizeHalf][pos.forth + sizeHalf] }
        val getRaw = { map: Array<Array<Array<Array<Boolean>>>>, pos: Quad -> map[pos.first           ][pos.second           ][pos.third           ][pos.forth           ] }
        val set    = { map: Array<Array<Array<Array<Boolean>>>>, pos: Quad, value: Boolean -> map[pos.first + sizeHalf][pos.second + sizeHalf][pos.third + sizeHalf][pos.forth + sizeHalf] = value }
        val setRaw = { map: Array<Array<Array<Array<Boolean>>>>, pos: Quad, value: Boolean -> map[pos.first           ][pos.second           ][pos.third           ][pos.forth           ] = value }
        val display = { map: Array<Array<Array<Array<Boolean>>>> ->
            for (rw in 0 until size)
                for (rz in 0 until size) {
                    println("z=${rz-sizeHalf}, w=${rw-sizeHalf}")
                    for (ry in 0 until size) {
                        for (rx in 0 until size)
                            print(if (map[rx][ry][rz][rw]) '#' else '.')
                        println()
                    }
                }
        }

        // load input into map
        val input = INPUT.readText().replace("\r", "").split("\n")
        val widthHalf = input[0].length / 2
        val heightHalf = input.size / 2
        for (ry in input.indices)
            for (rx in input[ry].indices) {
                val x = rx - widthHalf
                val y = ry - heightHalf - 1
                val z = 0
                val w = 0
                val pos = Quad(x, y, z, w)
                val value = input[ry][rx] == '#'
                set(mapIn, pos, value)
            }
        //display(mapIn)

        // cycle 6 times
        repeat(6) {
            for (rw in sizeRange) for (rz in sizeRange) for (ry in sizeRange) for (rx in sizeRange) {
                val rawPos = Quad(rx, ry, rz, rw)
                val active = getRaw(mapIn, rawPos)
                var neighbors = 0

                // count neighbors
                for (maskPos in mask) {
                    val pos = add(rawPos, maskPos)
                    if (isValidRaw(pos))
                        if (getRaw(mapIn, pos)) neighbors++
                }

                // apply rules
                if (active) {
                    val value = (neighbors == 2 || neighbors == 3)
                    setRaw(mapOut, rawPos, value)
                } else {
                    val value = (neighbors == 3)
                    setRaw(mapOut, rawPos, value)
                }
            }

            // swap
            val tmp = mapIn
            mapIn = mapOut
            mapOut = tmp

            //println("\n$it:")
            //display(mapIn)
        }

        // output
        var active = 0
        for (rw in sizeRange) for (rz in sizeRange) for (ry in sizeRange) for (rx in sizeRange)
            if (getRaw(mapIn, Quad(rx, ry, rz, rw))) active ++
        println(active)
    }

}