package y2021

import general.Day
import java.util.*

object Day04 : Day() {
    override val name = "Giant Squid"

    class Board {
        val data = Array(5) { Array(5) { 0 } }

        @Suppress("ControlFlowWithEmptyBody")
        fun read(input: List<String>, startIndex: Int): Int {

            var row = 0
            var index = startIndex

            if (index >= input.size) return -1
            while (input[index].isBlank())
                if (++index >= input.size) return -1

            while (true) {
                if (index >= input.size) break
                val line = input[index]
                if (line.isBlank()) break

                data[row] = line
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
                    .toTypedArray()

                row += 1
                index += 1
            }

            if (row == 0) return -1
            return index
        }

        fun draw(number: Int) {
            for (i in 0..4) for (j in 0..4)
                if (data[i][j] == number)
                    data[i][j] = -1
        }

        fun hasWon(): Boolean {
            column@for (i in 0..4) {
                for (j in 0..4) if (data[i][j] != -1) continue@column
                return true
            }

            row@for (i in 0..4) {
                for (j in 0..4) if (data[j][i] != -1) continue@row
                return true
            }

            return false
        }

        fun sum(): Int {
            var sum = 0

            for (i in 0..4) for (j in 0..4)
                if (data[i][j] >= 0)
                    sum += data[i][j]

            return sum
        }
    }

    private fun readInput(): Pair<List<Int>, LinkedList<Board>> {
        val input = INPUT.readLines()
        val numbers = input[0].split(",").map { it.toInt() }
        val boards = LinkedList<Board>()

        var index = 2
        while (index < input.size) {

            val board = Board()
            val end = board.read(input, index)
            if (end == -1) break

            boards += board
            index = end
        }

        return Pair(numbers, boards)
    }

    override fun a1() {
        val (numbers, boards) = readInput()

        for (number in numbers)
            for (board in boards) {

                board.draw(number)
                if (board.hasWon()) {
                    println(number * board.sum())
                    return
                }
            }

        println("no board has won")
    }

    override fun a2() {
        val (numbers, boards) = readInput()

        for (number in numbers) {

            val it = boards.iterator()
            while (it.hasNext()) {
                val board = it.next()

                board.draw(number)
                if (board.hasWon()) it.remove()

                if (boards.size == 0) {
                    println(number * board.sum())
                    return
                }
            }
        }

        println("no board has won")

    }
}