package y2019

import general.Day

object Day02 : Day() {
    override val name = "1202 Program Alarm"

    override fun a1() {
        val data = INPUT.readText().split(",").map { it.toInt() }

        println(calc(data, 12, 2))
    }

    override fun a2() {
        val data = INPUT.readText().split(",").map { it.toInt() }

        for (noun in 0..99) for (verb in 0..99)
            if (calc(data, noun, verb) == 19690720) {
                println(noun * 100 + verb)
                return
            }
    }

    private fun calc(data: List<Int>, noun: Int, verb: Int): Int {
        val ram = data.toIntArray()
        var pc = 0    // program counter

        ram[1] = noun
        ram[2] = verb

        while (true) {
            val opcode = ram[pc]

            when (opcode) {
                1 -> ram[ram[pc + 3]] = ram[ram[pc + 1]] + ram[ram[pc + 2]]
                2 -> ram[ram[pc + 3]] = ram[ram[pc + 1]] * ram[ram[pc + 2]]
                99 -> break
                else -> throw Error()
            }

            pc += 4
        }

        return ram[0]
    }
}