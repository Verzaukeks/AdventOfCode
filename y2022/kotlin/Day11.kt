package y2022

import general.Day
import general.productOf
import java.util.*

object Day11 : Day() {
    override val name = "Monkey in the Middle"

    class Monkey {
        val items = LinkedList<Long>()
        var opAdd = 0
        var opMul = 1
        var test = 0
        var onTrue = 0
        var onFalse = 0
        var inspected = 0

        fun calc(num: Long, div3: Boolean = true): Pair<Long, Int> {
            var x = when {
                opAdd == -1 -> num + num
                opMul == -1 -> num * num
                else -> (num + opAdd) * opMul
            }
            if (div3) x /= 3
            val id = if (x % test == 0L) onTrue else onFalse
            return Pair(x, id)
        }
    }

    private fun readMonkeys(): Array<Monkey> {
        val lines = INPUT.readText().replace("\r", "").split("\n\n")
        val monkeys = Array(lines.size) {
            val line = lines[it].split("\n")
            Monkey().apply {
                items.addAll(line[1].trim().substringAfter(": ").split(", ").map { it.toLong() })
                opAdd = line[2].trim().substringAfter("+ ", "0").replace("old", "-1").toInt()
                opMul = line[2].trim().substringAfter("* ", "1").replace("old", "-1").toInt()
                test = line[3].split(" ").last().toInt()
                onTrue = line[4].split(" ").last().toInt()
                onFalse = line[5].split(" ").last().toInt()
            }
        }
        return monkeys
    }

    override fun a1() {
        val monkeys = readMonkeys()

        repeat(20) {
            monkeys.forEach {
                it.inspected += it.items.size
                it.items.forEach { num ->
                    val (x, id) = it.calc(num)
                    monkeys[id].items += x
                }
                it.items.clear()
            }
        }

        monkeys.sortBy { -it.inspected }
        println(monkeys[0].inspected * monkeys[1].inspected)
    }

    override fun a2() {
        val monkeys = readMonkeys()

        // only possible because all test values are coprime to each other
        val mod = monkeys.productOf { it.test }

        repeat(10000) {
            monkeys.forEach {
                it.inspected += it.items.size
                it.items.forEach { num ->
                    val (x, id) = it.calc(num, false)
                    monkeys[id].items += (x % mod)
                }
                it.items.clear()
            }
        }

        monkeys.sortBy { -it.inspected }
        println(monkeys[0].inspected.toLong() * monkeys[1].inspected.toLong())
    }
}