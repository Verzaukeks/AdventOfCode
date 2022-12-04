package y2022

import general.Day

object Day04 : Day() {
    override val name = "Camp Cleanup"

    override fun a1() {
        println(INPUT
            .readLines()
            .sumOf {
                val split = it.split(",")
                val ab = split[0].split("-").map { it.toInt() }
                val cd = split[1].split("-").map { it.toInt() }
                when {
                    ab[0] <= cd[0] && cd[1] <= ab[1] -> 1
                    cd[0] <= ab[0] && ab[1] <= cd[1] -> 1
                    else -> 0L
                }
            })
    }

    override fun a2() {
        println(INPUT
            .readLines()
            .sumOf {
                val split = it.split(",")
                val ab = split[0].split("-").map { it.toInt() }
                val cd = split[1].split("-").map { it.toInt() }
                val x = ab[0]..ab[1]
                val y = cd[0]..cd[1]
                when {
                    x.first in y || x.last in y -> 1
                    y.first in x || y.last in x -> 1
                    else -> 0L
                }
            })
    }
}