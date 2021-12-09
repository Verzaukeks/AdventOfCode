package y2021

import general.Day
import java.util.*
import kotlin.collections.ArrayList

object Day09 : Day() {
    override val name = "Smoke Basin"

    override fun a1() {
        val input = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        var sumRisk = 0
        val height = input.size
        val width = input[0].size

        for (y in 0 until height) for (x in 0 until width) {
            val point = input[y][x]

            if (x + 1 < width && input[y][x + 1] <= point) continue
            if (x - 1 >= 0 && input[y][x - 1] <= point) continue
            if (y + 1 < height && input[y + 1][x] <= point) continue
            if (y - 1 >= 0 && input[y - 1][x] <= point) continue

            sumRisk += point + 1
        }

        println(sumRisk)
    }

    override fun a2() {
        val input = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        val basins = ArrayList<Int>()
        val height = input.size
        val width = input[0].size

        for (y in 0 until height) for (x in 0 until width) {
            val point = input[y][x]

            if (x + 1 < width && input[y][x + 1] <= point) continue
            if (x - 1 >= 0 && input[y][x - 1] <= point) continue
            if (y + 1 < height && input[y + 1][x] <= point) continue
            if (y - 1 >= 0 && input[y - 1][x] <= point) continue

            val basin = ArrayList<Int>()
            var index = 0
            var range: IntRange
            val locations = ArrayList<Pair<Int, Int>>()
            val previous = TreeMap<Int, Int>()

            locations += Pair(x, y)
            previous[y * width + x] = -1

            while (index < locations.size) {
                range = index until locations.size
                index = locations.size

                for (i in range) {
                    val lx = locations[i].first
                    val ly = locations[i].second
                    val pos = ly * width + lx

                    if (lx !in 0 until width) continue
                    if (ly !in 0 until height) continue

                    val hp = input[ly][lx]
                    if (hp == 9 || hp <= (previous[pos] ?: 9)) continue

                    if (pos !in basin)
                        basin += pos

                    val a = Pair(lx + 1, ly)
                    val b = Pair(lx - 1, ly)
                    val c = Pair(lx, ly + 1)
                    val d = Pair(lx, ly - 1)

                    for (p in arrayOf(a, b, c, d)) {

                        if (p in locations) {
                            val prev = previous[p.second * width + p.first] ?: -1
                            if (prev <= hp)
                                continue
                        }

                        locations += p
                        previous[p.second * width + p.first] = hp
                    }
                }
            }

            basins += basin.size
        }


        var a = 0
        var b = 0
        var c = 0

        for (size in basins)
            if (size > a) { c = b ; b = a ; a = size }
            else if (size > b) { c = b ; b = size }
            else if (size > c) { c = size }

        println(a * b * c)
    }
}