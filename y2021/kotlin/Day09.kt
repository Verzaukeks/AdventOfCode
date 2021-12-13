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
            if (isLocalMinimum(input, x, y, width, height))
                sumRisk += input[y][x] + 1
        }

        println(sumRisk)
    }

    private fun isLocalMinimum(input: List<List<Int>>, x: Int, y: Int, width: Int, height: Int): Boolean {
        val point = input[y][x]

        if (x + 1 < width  && input[y][x + 1] <= point) return false
        if (x - 1 >= 0     && input[y][x - 1] <= point) return false
        if (y + 1 < height && input[y + 1][x] <= point) return false
        if (y - 1 >= 0     && input[y - 1][x] <= point) return false

        return true
    }

    override fun a2() {
        val input = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        val basins = ArrayList<Int>()
        val height = input.size
        val width = input[0].size

        for (y in 0 until height) for (x in 0 until width) {
            if (isLocalMinimum(input, x, y, width, height))
                basins += getBasinSize(input, x, y, width, height)
        }

        val (a, b, c) = getHighestThree(basins)
        println(a * b * c)
    }

    private fun getBasinSize(input: List<List<Int>>, x: Int, y: Int, width: Int, height: Int): Int {
        // All points inside the basin
        val basin = ArrayList<Int>()

        // Locations to check
        var index = 0
        var range: IntRange
        val locations = ArrayList<Pair<Int, Int>>()

        // [Point] -> [Lowest neighbour height (only points inside basin)]
        val previous = TreeMap<Int, Int>()

        // Add center basin point
        locations += Pair(x, y)
        previous[y * width + x] = -1

        // do as long there are points to check
        while (index < locations.size) {
            range = index until locations.size
            index = locations.size

            // check every point
            for (i in range) {
                val lx = locations[i].first
                val ly = locations[i].second
                val pos = ly * width + lx

                // prevent points outside grid
                if (lx !in 0 until width) continue
                if (ly !in 0 until height) continue

                // sort for higher points less than 9
                val hp = input[ly][lx]
                if (hp == 9 || hp <= (previous[pos] ?: 9)) continue

                // prevent duplicates inside basin
                if (pos !in basin)
                    basin += pos

                // neighbours to check
                val a = Pair(lx + 1, ly)
                val b = Pair(lx - 1, ly)
                val c = Pair(lx, ly + 1)
                val d = Pair(lx, ly - 1)

                for (p in arrayOf(a, b, c, d)) {

                    // correct if a point has a lower neighbour before previously tested
                    if (p in locations) {
                        val prev = previous[p.second * width + p.first] ?: -1
                        if (prev <= hp)
                            continue
                    }

                    // add point to queue for checking
                    locations += p
                    previous[p.second * width + p.first] = hp
                }
            }
        }

        return basin.size
    }

    private fun getHighestThree(list: List<Int>): Triple<Int, Int, Int> {
        var a = 0
        var b = 0
        var c = 0

        for (size in list)
            if (size > a) { c = b ; b = a ; a = size }
            else if (size > b) { c = b ; b = size }
            else if (size > c) { c = size }

        return Triple(a, b, c)
    }
}