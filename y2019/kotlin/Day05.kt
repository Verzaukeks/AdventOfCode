package y2019

import general.Day

object Day05 : Day() {
    override val name = "Sunny with a Chance of Asteroids"

    override fun a1() {
        val data = INPUT.readText().split(",").map { it.toInt() }
        val ram = data.toIntArray()
        var pc = 0

        while (true) {
            val opcode = ram[pc]
            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10
//            val mode3 = opcode / 10000 % 10

            val value1 by lazy { if (mode1 == 1) ram[pc + 1] else if (mode1 == 0) ram[ram[pc + 1]] else throw Error() }
            val value2 by lazy { if (mode2 == 1) ram[pc + 2] else if (mode2 == 0) ram[ram[pc + 2]] else throw Error() }
//            val value3 by lazy { if (mode3 == 1) ram[pc + 3] else if (mode3 == 0) ram[ram[pc + 3]] else throw Error() }

            when (opcode % 100) {
                1 -> { ram[ram[pc + 3]] = value1 + value2; pc += 4 }
                2 -> { ram[ram[pc + 3]] = value1 * value2; pc += 4 }
                3 -> { print("Gimme int: "); ram[ram[pc + 1]] = readLine()!!.toInt(); pc += 2 }
                4 -> { println("Yeet $value1"); pc += 2 }
                99 -> break
                else -> throw Error("${opcode % 100}: $pc -> ${ram.joinToString(",")}")
            }
        }
    }

    override fun a2() {
        val data = INPUT.readText().split(",").map { it.toInt() }
        val ram = data.toIntArray()
        var pc = 0

        while (true) {
            val opcode = ram[pc]
            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10
//            val mode3 = opcode / 10000 % 10

            val value1 by lazy { if (mode1 == 1) ram[pc + 1] else if (mode1 == 0) ram[ram[pc + 1]] else throw Error() }
            val value2 by lazy { if (mode2 == 1) ram[pc + 2] else if (mode2 == 0) ram[ram[pc + 2]] else throw Error() }
//            val value3 by lazy { if (mode3 == 1) ram[pc + 3] else if (mode3 == 0) ram[ram[pc + 3]] else throw Error() }

            when (opcode % 100) {
                1 -> { ram[ram[pc + 3]] = value1 + value2; pc += 4 }
                2 -> { ram[ram[pc + 3]] = value1 * value2; pc += 4 }
                3 -> { print("Gimme int: "); ram[ram[pc + 1]] = readLine()!!.toInt(); pc += 2 }
                4 -> { println("Yeet $value1"); pc += 2 }
                5 -> { if (value1 != 0) pc = value2 else pc += 3 }
                6 -> { if (value1 == 0) pc = value2 else pc += 3 }
                7 -> { ram[ram[pc + 3]] = if (value1 < value2) 1 else 0 ; pc += 4 }
                8 -> { ram[ram[pc + 3]] = if (value1 == value2) 1 else 0 ; pc += 4 }
                99 -> break
                else -> throw Error("${opcode % 100}: $pc -> ${ram.joinToString(",")}")
            }
        }
    }
}