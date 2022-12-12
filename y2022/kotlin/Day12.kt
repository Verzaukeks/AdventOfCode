package y2022

import general.Day
import java.util.*
import kotlin.Comparator

object Day12 : Day() {
    override val name = "Hill Climbing Algorithm"

    data class Pos(val x: Int, val y: Int, val d: Int, val c: Int)
    private val ADJ = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

    private fun shortestPath(bigStart: Boolean = false) {
        val map = INPUT.readLines().map { it.toCharArray() }

        val dist = Array(map.size) { IntArray(map[0].size) { 0x42424242 } }
        val pq = PriorityQueue(Comparator<Pos> { a, b -> a!!.d - b!!.d })

        f@for (y in map.indices)
            for (x in map[y].indices)
                if (map[y][x] == 'S') {
                    map[y][x] = 'a'
                    pq.add(Pos(x, y, 0, 0))
                    dist[y][x] = 0
                    break@f
                }

        while (pq.isNotEmpty()) {
            val c = pq.poll()
            if (dist[c.y][c.x] != c.d) continue

            for ((ox, oy) in ADJ) {
                val xx = c.x + ox
                val yy = c.y + oy
                var dd = c.d + 1
                if (yy < 0 || map.size <= yy) continue
                if (xx < 0 || map[yy].size <= xx) continue

                if (map[yy][xx] == 'E') {
                    if (c.c < 'z' - 'a' - 1) continue
                    return println(dd)
                }

                val cc = map[yy][xx] - 'a'
                if (bigStart && c.d == 0 && c.c == 0 && cc == 0) dd = 0

                if (cc > c.c + 1) continue
                if (dist[yy][xx] <= dd) continue

                dist[yy][xx] = dd
                pq.add(Pos(xx, yy, dd, cc))
            }
        }
    }

    override fun a1() {
        shortestPath()
    }

    override fun a2() {
        shortestPath(true)
    }
}