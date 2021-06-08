package y2020

import general.Day

object Day10 : Day() {
    override val name = "Adapter Array"

    override fun a1() {
        val input = INPUT
                .readLines()
                .map { it.toInt() }
                .sorted()
                .toMutableList()
        input += input.last() + 3

        var prev = 0
        val di = IntArray(10)

        for (i in input.indices) {
            di[input[i] - prev] ++
            prev = input[i]
        }

        println(di[1] * di[3])
    }

    override fun a2() {
        val input = INPUT
                .readLines()
                .map { it.toInt() }
                .sorted()
                .toMutableList()
        input.add(0, 0)
        input += input.last() + 3

        val solutions = LongArray(input.size)
        solutions[0] = 1

        for (i in 1 until input.size) {

            solutions[i] += solutions[i-1]
            if (i-2 >= 0 && (input[i] - 2 == input[i-2] || input[i] - 3 == input[i-2])) solutions[i] += solutions[i-2]
            if (i-3 >= 0 && (                              input[i] - 3 == input[i-3])) solutions[i] += solutions[i-3]

            //if (i-3 >= 0) print((if (                            input[i]-3 == input[i-3]) " %d  " else "[%d] ").replace("%d", "${input[i-3]}"))
            //if (i-2 >= 0) print((if (input[i]-2 == input[i-2] || input[i]-3 == input[i-2]) " %d  " else "[%d] ").replace("%d", "${input[i-2]}"))
            //print(" ${input[i-1]}  ")
            //println(" ${input[i]}")
        }

        println(solutions.last())
    }
}