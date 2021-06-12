package y2019

import general.Day
import java.util.*

object Day09 : Day() {
    override val name = "Sensor Boost"

    class Ram(initial: List<Long>) {
        private val data = initial.toLongArray()
        private val memory = TreeMap<Long, Long>()

        operator fun set(address: Long, value: Long) = if (address < data.size) data[address.toInt()] = value else memory[address] = value
        operator fun get(address: Long)              = if (address < data.size) data[address.toInt()]         else memory[address] ?: 0
        override fun toString() = data.joinToString(",") + "," + memory.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    override fun a1() {
        val data = INPUT.readText().split(",").map { it.toLong() }
        val ram = Ram(data)
        var rb = 0L // relative base
        var pc = 0L

        while (true) {
            val opcode = ram[pc]

            // 0:position  1:immediate  2:relative
            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10
            val mode3 = opcode / 10000 % 10

            val value1 by lazy { if (mode1 == 2L) rb + ram[pc + 1] else if (mode1 == 1L) pc + 1 else if (mode1 == 0L) ram[pc + 1] else throw Error() }
            val value2 by lazy { if (mode2 == 2L) rb + ram[pc + 2] else if (mode2 == 1L) pc + 2 else if (mode2 == 0L) ram[pc + 2] else throw Error() }
            val value3 by lazy { if (mode3 == 2L) rb + ram[pc + 3] else if (mode3 == 1L) pc + 3 else if (mode3 == 0L) ram[pc + 3] else throw Error() }

            when (opcode % 100) {
                1L -> { ram[value3] = ram[value1] + ram[value2]; pc += 4 }
                2L -> { ram[value3] = ram[value1] * ram[value2]; pc += 4 }
                3L -> { print("Gimme int: "); ram[value1] = readLine()!!.toLong(); pc += 2 }
                4L -> { println("Yeet ${ram[value1]}"); pc += 2 }
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

    override fun a2() = a1()
}