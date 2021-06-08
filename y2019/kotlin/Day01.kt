package y2019

import general.Day

object Day01 : Day() {
    override val name = "The Tyranny of the Rocket Equation"

    override fun a1() {
        INPUT
            .readLines()
            .sumOf { it.toInt() / 3 - 2 }
            .also(::println)
    }

    override fun a2() {
        var sum = 0

        INPUT.readLines().forEach {
            var mass = it.toInt()

            while (true) {
                val fuel = mass / 3 - 2
                if (fuel <= 0) break

                sum += fuel
                mass = fuel
            }
        }

        println(sum)
    }
}