package y2021

import general.Day

object Day06 : Day() {
    override val name = "Lanternfish"

    override fun a1() {
        val input = INPUT.readText().split(",").map { it.toInt() }
        val list = IntArray(9)

        for (fish in input)
            list[fish] += 1

        repeat (80) {
            val zero = list[0]
            for (day in 1..8)
                list[day - 1] = list[day]

            list[8] = zero
            list[6] += zero
        }

        println(list.sum())
    }

    override fun a2() {
        val input = INPUT.readText().split(",").map { it.toInt() }
        val list = LongArray(9)

        for (fish in input)
            list[fish] += 1L

        repeat (256) {
            val zero = list[0]
            for (day in 1..8)
                list[day - 1] = list[day]

            list[8] = zero
            list[6] += zero
        }

        println(list.sum())
    }
}