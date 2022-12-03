package y2022

import general.Day

object Day03 : Day() {
    override val name = "Rucksack Reorganization"

    override fun a1() {
        println(INPUT
            .readLines()
            .sumOf {
                val a = it.substring(0, it.length / 2)
                val b = it.substring(it.length / 2)
                val c = a.find { it in b }!!
                when (c) {
                    in 'a'..'z' -> c - 'a' + 1
                    in 'A'..'Z' -> c - 'A' + 27
                    else -> 0
                }
            })
    }

    override fun a2() {
        println(INPUT
            .readLines()
            .chunked(3)
            .sumOf {
                val c = it[0].find { a -> a in it[1] && a in it[2] }!!
                when (c) {
                    in 'a'..'z' -> c - 'a' + 1
                    in 'A'..'Z' -> c - 'A' + 27
                    else -> 0
                }
            })
    }
}