package y2023

import general.Day
import general.product
import kotlin.math.sqrt

object Day06 : Day() {
    override val name = "Wait For It"

    override fun a1() {
        val lines = INPUT.readLines()
        val times = "\\d+".toRegex().findAll(lines[0]).map { it.value.toInt() }.toList()
        val dists = "\\d+".toRegex().findAll(lines[1]).map { it.value.toInt() }.toList()

        println(
            times.zip(dists)
                .map { (t, d) ->
//                    var sum = 0L
//                    for (ti in 1 until t) {
//                        val di = ti * (t - ti)
//                        if (di > d) sum++
//                    }
//                    sum
                    val det = t * t - 4 * d
                    if (det < 0) 0L
                    else {
                        val a = (t - sqrt(det.toDouble())) / 2
                        val b = (t + sqrt(det.toDouble())) / 2
                        b.toLong() - a.toLong()
                    }
                }.product()
        )
    }

    override fun a2() {
        val lines = INPUT.readLines()
        val time = lines[0].filter { it.isDigit() }.toLong()
        val dist = lines[1].filter { it.isDigit() }.toLong()

//        var sum = 0
//        for (ti in 1 until time) {
//            val di = ti * (time - ti)
//            if (di > dist) sum++
//        }
        val det = time * time - 4 * dist
        if (det < 0) println(0)
        else {
//            val a = (time - sqrt(det.toDouble())) / 2
//            val b = (time + sqrt(det.toDouble())) / 2
//            println(b.toLong() - a.toLong())
            println(sqrt(det.toDouble()).toLong())
        }
    }
}