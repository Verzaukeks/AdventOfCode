package y2021

import general.Day

object Day02 : Day() {
    override val name = "Dive!"

    override fun a1() {
        val input = INPUT.readLines()
            .map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }

        var horizontal = 0
        var depth = 0

        for ((command, amount) in input)
            when (command) {
                "forward" -> horizontal += amount
                "down" -> depth += amount
                "up" -> depth -= amount
            }

        println(horizontal * depth)
    }

    override fun a2() {
        val input = INPUT.readLines()
            .map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }

        var horizontal = 0
        var depth = 0
        var aim = 0

        for ((command, amount) in input)
            when (command) {
                "forward" -> {
                    horizontal += amount
                    depth += aim * amount
                }
                "down" -> aim += amount
                "up" -> aim -= amount
            }

        println(horizontal * depth)
    }
}