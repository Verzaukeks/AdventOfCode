package y2022

import general.Day
import java.util.*

object Day05 : Day() {
    override val name = "Supply Stacks"

    override fun a1() {
        val lines = LinkedList(INPUT.readLines())
        val stackSize = (lines[0].length + 1) / 4
        val stacks = Array(stackSize+1) { Stack<Char>() }

        while (true) {
            val line = lines.removeFirst()
            if (line.isEmpty()) break

            for (i in 1..stackSize) {
                val char = line[i * 4 - 3]
                if (char.isDigit()) break
                if (char.isWhitespace()) continue
                stacks[i].add(char)
            }
        }

        for (i in 1..stackSize)
            stacks[i].reverse()

        lines.forEach {
            val spl = it.split(" ")
            val amount = spl[1].toInt()
            val from = spl[3].toInt()
            val to = spl[5].toInt()

            repeat(amount) {
                stacks[to].add(stacks[from].pop());
            }
        }

        for (i in 1..stackSize)
            if (stacks[i].isNotEmpty())
                print(stacks[i].peek())
        println()
    }

    override fun a2() {
        val lines = LinkedList(INPUT.readLines())
        val stackSize = (lines[0].length + 1) / 4
        val stacks = Array(stackSize+1) { Stack<Char>() }

        while (true) {
            val line = lines.removeFirst()
            if (line.isEmpty()) break

            for (i in 1..stackSize) {
                val char = line[i * 4 - 3]
                if (char.isDigit()) break
                if (char.isWhitespace()) continue
                stacks[i].add(char)
            }
        }

        for (i in 1..stackSize)
            stacks[i].reverse()

        lines.forEach {
            val spl = it.split(" ")
            val amount = spl[1].toInt()
            val from = spl[3].toInt()
            val to = spl[5].toInt()

            val buffer = Stack<Char>()
            repeat(amount) { buffer.add(stacks[from].pop()) }
            repeat(amount) { stacks[to].add(buffer.pop()) }
        }

        for (i in 1..stackSize)
            if (stacks[i].isNotEmpty())
                print(stacks[i].peek())
        println()
    }
}