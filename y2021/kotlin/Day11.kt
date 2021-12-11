package y2021

import general.Day

object Day11 : Day() {
    override val name = "Dumbo Octopus"

    private fun map(): MutableList<MutableList<Int>> =
        INPUT.readLines()
            .map {
                it.toCharArray()
                    .map { it - '0' }
                    .toMutableList()
            }.toMutableList()

    override fun a1() {
        val map = map()
        var flashes = 0

        repeat(100) {
            flashes += step(map)
        }

        println(flashes)
    }

    private fun step(map: MutableList<MutableList<Int>>): Int {
        var flashes = 0

        for (y in map.indices)
            for (x in map[y].indices)
                flashes += increase(map, x, y)

        for (y in map.indices)
            for (x in map[y].indices)
                if (map[y][x] == -1)
                    map[y][x] = 0

        return flashes
    }

    private fun increase(map: MutableList<MutableList<Int>>, x: Int, y: Int): Int {
        if (y < 0 || x < 0) return 0
        if (y >= 10 || x >= 10) return 0

        val value = map[y][x]
        if (value == -1) return 0

        if (value == 9) {
            map[y][x] = -1

            var f = 1
            f += increase(map, x - 1, y - 1)
            f += increase(map, x, y - 1)
            f += increase(map, x + 1, y - 1)
            f += increase(map, x - 1, y)
            f += increase(map, x + 1, y)
            f += increase(map, x - 1, y + 1)
            f += increase(map, x, y + 1)
            f += increase(map, x + 1, y + 1)
            return f
        }

        map[y][x] += 1
        return 0
    }

    override fun a2() {
        val map = map()
        var steps = 0

        while (true) {
            steps += 1
            val flashes = step(map)
            if (flashes == 100) break
        }

        println(steps)
    }
}