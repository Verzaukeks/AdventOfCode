package y2022

import general.Day
import kotlin.math.max
import kotlin.math.min

object Day14 : Day() {
    override val name = "Regolith Reservoir"

    private fun simulate(part2: Boolean = false) {
        // parse input
        val lines = INPUT.readLines().map {
            it.split(" -> ").map {
                it.split(",").map {
                    it.toInt()
                }
            }
        }

        // determine cave bounds
        var lowX = 500
        var highX = 500
        var highY = 0
        for (line in lines)
            for ((x, y) in line) {
                lowX = min(lowX, x)
                highX = max(highX, x)
                highY = max(highY, y)
            }

        if (part2) {
            highY += 2
            lowX = 500 - highY
            highX = 500 + highY
        }

        // generate cave
        val dx = (++highX) - (--lowX) + 1
        val cave = Array(highY + 1) { CharArray(dx) { '.' } }

        for (line in lines)
            for (i in 1 until line.size) {
                val prev = line[i - 1]
                val curr = line[i]

                val fx = min(prev[0], curr[0])
                val tx = max(prev[0], curr[0])
                val fy = min(prev[1], curr[1])
                val ty = max(prev[1], curr[1])

                for (y in fy..ty)
                    for (x in fx..tx)
                        cave[y][x - lowX] = '#'
            }

        if (part2) {
            for (x in cave[highY].indices)
                cave[highY][x] = '#'
        }

        // simulate
        var steps = 0
        while (true) {

            var x = 500 - lowX
            var y = 0

            if (cave[y][x] != '.') {
                println(steps)
                return
            }

            while (true)
                if (y < highY) {
                    if (cave[y + 1][x    ] == '.') { y++;      continue }
                    if (cave[y + 1][x - 1] == '.') { y++; x--; continue }
                    if (cave[y + 1][x + 1] == '.') { y++; x++; continue }
                    cave[y][x] = 'o'
                    break
                } else {
                    println(steps)
                    return
                }

            steps++
        }
    }

    override fun a1() {
        simulate()
    }

    override fun a2() {
        simulate(true)
    }
}