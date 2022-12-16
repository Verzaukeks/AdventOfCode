package y2022

import general.Day
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day15 : Day() {
    override val name = "Beacon Exclusion Zone"

    override fun a1() {
        // parse input
        val lines = INPUT.readLines().map {
            it.replace("[^0-9,:-]".toRegex(), "")
                .split(",", ":").map { it.toInt() }
        }

        // determine intervals that cover x-positions on y=targetY
        val targetY = 2000000
        val intervals = ArrayList<Pair<Int, Int>>()
        for ((sx, sy, bx, by) in lines) {
            val d = abs(sx - bx) + abs(sy - by)
            val dx = d - abs(targetY - sy)
            if (dx >= 0) intervals += Pair(sx - dx, sx + dx)
        }

        intervals.sortWith { a, b ->
            if (a.first == b.first) a.second - b.second else a.first - b.first
        }

        // sum interval size after combining overlapping intervals
        var count = 0
        var curr = intervals[0]
        for (int in intervals) {
            count += min(curr.second, int.first - 1) - curr.first + 1
            curr = Pair(int.first, max(curr.second, int.second))
        }
        count += curr.second - curr.first + 1

        // subtract beacon positions
        val set = TreeSet<Int>()
        for ((sx, sy, bx, by) in lines)
            if (by == targetY)
                set.add(bx)
        count -= set.size

        println(count)
    }

    override fun a2() {
        a2ForEachLine()
//        a2Polygons()
    }

    private fun a2ForEachLine() {
        val lines = INPUT.readLines().map {
            it.replace("[^0-9,:-]".toRegex(), "")
                .split(",", ":").map { it.toInt() }
        }

        for (targetY in 0..4000000) {

            val intervals = ArrayList<Pair<Int, Int>>()
            for ((sx, sy, bx, by) in lines) {
                val d = abs(sx - bx) + abs(sy - by)
                val dx = d - abs(targetY - sy)
                if (dx >= 0) intervals += Pair(sx - dx, sx + dx)
            }

            intervals.sortWith { a, b ->
                if (a.first == b.first) a.second - b.second else a.first - b.first
            }

            var curr = intervals[0]
            for (int in intervals) {
                if (curr.second + 1 == int.first - 1) {
                    println(curr.second + 1)
                    println(targetY)
                    return println((curr.second + 1) * 4000000L + targetY)
                }
                curr = Pair(int.first, max(curr.second, int.second))
            }
        }
    }

    /*
    private fun a2Polygons() {
        val lines = INPUT.readLines().map {
            it.replace("[^0-9,:-]".toRegex(), "")
                .split(",", ":").map { it.toDouble() }
        }

        val low = 0.0
        val high = 4000000.0
        val gf = GeometryFactory()
        var p: Geometry = gf.createPolygon(arrayOf(
            Coordinate(low, low),
            Coordinate(high, low),
            Coordinate(high, high),
            Coordinate(low, high),
            Coordinate(low, low),
        ))

        for ((sx, sy, bx, by) in lines) {
            val d = abs(sx - bx) + abs(sy - by) + 0.5
            val points = arrayOf(
                Coordinate(sx - d, sy),
                Coordinate(sx, sy + d),
                Coordinate(sx + d, sy),
                Coordinate(sx, sy - d),
                Coordinate(sx - d, sy),
            )

            p = p.difference(gf.createPolygon(points))
        }

        println(p.interiorPoint.let { it.x.toLong() * 4000000L + it.y.toLong() })
    }
    */

}