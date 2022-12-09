package y2022

import general.Day
import java.util.TreeSet
import kotlin.math.abs
import kotlin.math.sign

object Day09 : Day() {
    override val name = "Rope Bridge"

    data class Pos(var x: Int, var y: Int) : Comparable<Pos> {
        override fun compareTo(other: Pos): Int {
            if (x == other.x) return other.y - y
            return other.x - x
        }
    }

    override fun a1() {
        val lines = INPUT.readLines().map { it.split(" ") }
        val poss = TreeSet<Pos>()

        var hx = 0
        var hy = 0
        var tx = 0
        var ty = 0

        for ((dir, am) in lines)
            repeat(am.toInt()) {

                when (dir) {
                    "U" -> hy++
                    "D" -> hy--
                    "R" -> hx++
                    "L" -> hx--
                }

                if (tx == hx && ty == hy);
                else if (tx + 2 == hx) { tx = hx - 1 ; ty = hy }
                else if (tx - 2 == hx) { tx = hx + 1 ; ty = hy }
                else if (ty + 2 == hy) { ty = hy - 1 ; tx = hx }
                else if (ty - 2 == hy) { ty = hy + 1 ; tx = hx }
                else if (abs(hx - tx) + abs(hy - ty) <= 2)
                else println("$hx,$hy : $tx,$ty")

                poss.add(Pos(tx, ty))
            }

        println(poss.size)
    }

    override fun a2() {
        val lines = INPUT.readLines().map { it.split(" ") }
        val poss = TreeSet<Pos>()

        val head = Pos(0, 0)
        val tails = Array(9) { Pos(0, 0) }

        for ((dir, am) in lines)
            repeat(am.toInt()) {

                when (dir) {
                    "U" -> head.y++
                    "D" -> head.y--
                    "R" -> head.x++
                    "L" -> head.x--
                }

                var prev = head
                for (tail in tails) {
                    val dx = prev.x - tail.x
                    val dy = prev.y - tail.y
                    if (dx * dx + dy * dy > 2) {
                        tail.x += dx.sign
                        tail.y += dy.sign
                    }
                    prev = tail
                }

                poss.add(prev.copy())
            }

        println(poss.size)
    }
}