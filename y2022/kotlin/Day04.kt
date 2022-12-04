package y2022

import general.Day

object Day04 : Day() {
    override val name = "Camp Cleanup"

    override fun a1() {
        println(INPUT
            .readLines()
            .count {
                val split = it.split(",")
                val ab = split[0].split("-").map { it.toInt() }
                val cd = split[1].split("-").map { it.toInt() }
                ab[0] <= cd[0] && cd[1] <= ab[1] ||
                cd[0] <= ab[0] && ab[1] <= cd[1]
            })
    }

    override fun a2() {
        println(INPUT
            .readLines()
            .count {
                val split = it.split(",")
                val ab = split[0].split("-").map { it.toInt() }
                val cd = split[1].split("-").map { it.toInt() }
                cd[0] <= ab[1] &&
                ab[0] <= cd[1]
            })
    }
}