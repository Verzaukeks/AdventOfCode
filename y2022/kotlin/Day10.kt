package y2022

import general.Day

object Day10 : Day() {
    override val name = "Cathode-Ray Tube"

    override fun a1() {
        val lines = INPUT.readLines().map { it.split(" ") }

        var X = 1
        var cycle = 0
        var catchCycle = 20
        var sum = 0

        for (line in lines) {
            when (line[0]) {
                "noop" -> cycle += 1
                "addx" -> cycle += 2
            }

            if (cycle >= catchCycle) {
                sum += X * catchCycle
                catchCycle += 40
            }

            when (line[0]) {
                "noop" -> {}
                "addx" -> X += line[1].toInt()
            }
        }

        println(sum)
    }

    override fun a2() {
        val lines = INPUT.readLines().map { it.split(" ") }

        val crt = Array(6) { CharArray(40) { ' ' } }
        var X = 1
        var cycle = 0

        for (line in lines) {
            val amount = when (line[0]) {
                "noop" -> 1
                "addx" -> 2
                else -> continue
            }

            repeat(amount) {
                val y = cycle / 40
                val x = cycle % 40

                if (X - 1 <= x && x <= X + 1)
                    crt[y][x] = '#'

                cycle++
            }

            when (line[0]) {
                "noop" -> {}
                "addx" -> X += line[1].toInt()
            }
        }

        println(crt.joinToString("\n") { it.joinToString("") { "$it$it" } })
    }

}