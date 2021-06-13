package y2019

import general.Day
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

object Day11 : Day() {
    override val name = "Space Police"

    override fun a1() {
        val panel = TreeMap<String, Int>()  // ugly string, but i am to lazy to do something else
        val painted = ArrayList<String>()

        var posX = 0
        var posY = 0
        var moveX = 0
        var moveY = 1

        var outputState = 0

        intCodeProgram(
            input = { panel["$posX,$posY"]?.toLong() ?: 0L },
            output = {
                 when (outputState++) {
                     0 -> {
                         val pos = "$posX,$posY"
                         if (panel[pos] ?: 0L != it && pos !in painted) painted += pos
                         panel[pos] = it.toInt()
                     }
                     1 -> {
                         when (it) {
                             0L -> moveY = moveX.also { moveX = -moveY }
                             1L -> moveY = -moveX.also { moveX = moveY }
                             else -> throw Exception("Unknown rotation input: $it")
                         }
                         posX += moveX
                         posY += moveY
                         outputState = 0
                     }
                 }
            },
        )

        println(painted.size)
    }

    override fun a2() {
        val panel = TreeMap<String, Int>()  // ugly string, but i am to lazy to do something else

        var posX = 0
        var posY = 0
        var moveX = 0
        var moveY = 1

        panel["$posX,$posY"] = 1

        var outputState = 0

        intCodeProgram(
            input = { panel["$posX,$posY"]?.toLong() ?: 0L },
            output = {
                when (outputState++) {
                    0 -> panel["$posX,$posY"] = it.toInt()
                    1 -> {
                        when (it) {
                            0L -> moveY = moveX.also { moveX = -moveY }
                            1L -> moveY = -moveX.also { moveX = moveY }
                            else -> throw Exception("Unknown rotation input: $it")
                        }
                        posX += moveX
                        posY += moveY
                        outputState = 0
                    }
                }
            },
        )

        var fromX = 0
        var toX = 0
        var fromY = 0
        var toY = 0
        for (pos in panel.keys) {
            val (x, y) = pos.split(",").map { it.toInt() }
            fromX = min(fromX, x)
            toX = max(toX, x)
            fromY = min(fromY, y)
            toY = max(toY, y)
        }

        val display = Array(toY - fromY + 1) { Array(toX - fromX + 1) { '.' } }
        for ((pos, color) in panel.entries) {
            val (x, y) = pos.split(",").map { it.toInt() }
            val char = if (color == 0) ' ' else if (color == 1) '#' else '.'
            display[y - fromY][x - fromX] = char
        }

        println(display.joinToString("\n") { it.joinToString("") { "$it$it" } }
            .split("\n").reversed().joinToString("\n")) // ups, my plane is mirrored on the x-axis :D (mine: [0,0] = bottom left) (expected: [0,0] = top left)
    }

    class Ram(initial: List<Long> = listOf()) {
        private val data = initial.toLongArray()
        private val memory = TreeMap<Long, Long>()

        operator fun set(address: Long, value: Long) = if (address < data.size) data[address.toInt()] = value else memory[address] = value
        operator fun get(address: Long)              = if (address < data.size) data[address.toInt()]         else memory[address] ?: 0
        override fun toString() = data.joinToString(",") + "," + memory.entries.joinToString(",") { "${it.key}:${it.value}" }
    }
    private fun intCodeProgram(
        data: List<Long> = INPUT.readText().split(",").map { it.toLong() },
        input: () -> Long = { print("Gimme int: ") ; readLine()!!.toLong() },
        output: (Long) -> Unit = { println("Yeet $it") },
    ) {
        val ram = Day09.Ram(data)
        var rb = 0L
        var pc = 0L

        while (true) {
            val opcode = ram[pc]

            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10
            val mode3 = opcode / 10000 % 10

            val value1 by lazy { if (mode1 == 2L) rb + ram[pc + 1] else if (mode1 == 1L) pc + 1 else if (mode1 == 0L) ram[pc + 1] else throw Error() }
            val value2 by lazy { if (mode2 == 2L) rb + ram[pc + 2] else if (mode2 == 1L) pc + 2 else if (mode2 == 0L) ram[pc + 2] else throw Error() }
            val value3 by lazy { if (mode3 == 2L) rb + ram[pc + 3] else if (mode3 == 1L) pc + 3 else if (mode3 == 0L) ram[pc + 3] else throw Error() }

            when (opcode % 100) {
                1L -> { ram[value3] = ram[value1] + ram[value2] ; pc += 4 }
                2L -> { ram[value3] = ram[value1] * ram[value2] ; pc += 4 }
                3L -> { ram[value1] = input() ; pc += 2 }
                4L -> { output(ram[value1]) ; pc += 2 }
                5L -> { if (ram[value1] != 0L) pc = ram[value2] else pc += 3 }
                6L -> { if (ram[value1] == 0L) pc = ram[value2] else pc += 3 }
                7L -> { ram[value3] = if (ram[value1] < ram[value2]) 1 else 0 ; pc += 4 }
                8L -> { ram[value3] = if (ram[value1] == ram[value2]) 1 else 0 ; pc += 4 }
                9L -> { rb += ram[value1] ; pc += 2 }
                99L -> break
                else -> throw Error("${opcode % 100}: $pc -> $ram")
            }
        }
    }
}