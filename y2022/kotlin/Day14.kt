package y2022

import general.Day
import java.util.*
import kotlin.math.max
import kotlin.math.min

object Day14 : Day() {
    override val name = "Regolith Reservoir"

    private fun genCave(): Pair<Array<CharArray>, Int> {
        // parse input
        val lines = INPUT.readLines().map {
            it.split(" -> ").map {
                it.split(",").map {
                    it.toInt()
                }
            }
        }

        // determine bounds
        var highY = 0
        for (line in lines)
            for ((x, y) in line)
                highY = max(highY, y)

        highY += 2
        val lowX = 500 - highY
        val highX = 500 + highY
        val dx = highX - lowX + 1
        val cave = Array(highY + 1) { CharArray(dx) { '.' } }

        // place walls
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

        // 'endless' floor
        for (x in cave[highY].indices)
            cave[highY][x] = '#'

        return Pair(cave, lowX)
    }

    override fun a1() {
        val (cave, lowX) = genCave()
        val maxY = cave.size - 2

        // return = <sand placed, abyss reached>
        fun dfs(x: Int, y: Int): Pair<Int, Boolean> {
            if (y == maxY) return Pair(0, true)
            if (cave[y][x] != '.') return Pair(0, false)

            val d = dfs(x, y + 1)
            if (d.second) return Pair(d.first, true)
            val l = dfs(x - 1, y + 1)
            if (l.second) return Pair(d.first + l.first, true)
            val r = dfs(x + 1, y + 1)
            if (r.second) return Pair(d.first + l.first + r.first, true)

            cave[y][x] = 'o'
            return Pair(d.first + l.first + r.first + 1, false)
        }

        println(dfs(500 - lowX, 0).first)
    }

    override fun a2() {
        val (cave, lowX) = genCave()

        fun bfs(): Int {
            var count = 1
            val q = LinkedList<Pair<Int, Int>>()

            q.add(Pair(500 - lowX, 0))

            while (q.isNotEmpty()) {
                val (x, y) = q.pop()

                for (ox in -1..1) {
                    val xx = x + ox
                    val yy = y + 1

                    if (cave[yy][xx] == '.') {
                        cave[yy][xx] = 'o'

                        count++
                        q.add(Pair(xx, yy))
                    }
                }
            }

            return count
        }

        println(bfs())
    }
}