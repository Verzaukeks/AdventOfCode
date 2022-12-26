package y2022

import general.Day
import java.util.*

object Day24 : Day() {
    override val name = "Blizzard Basin"

    private const val LEFT  = 0b0001
    private const val RIGHT = 0b0010
    private const val UP    = 0b0100
    private const val DOWN  = 0b1000
    private val ADJ = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1), Pair(0, 0))

    private fun run(part2: Boolean = false) {
        val map = INPUT.readLines().map { it.toCharArray() }
        val width = map[0].size
        val height = map.size

        val counter = Array(height) { IntArray(width) }
        var blizzards = Array(height) { IntArray(width) }
        var blizzardsSwap = Array(height) { IntArray(width) }
        val positions = LinkedList<Pair<Int, Int>>()
        var state = if (part2) 0 else 2

        positions.add(Pair(1, 0))

        for (y in map.indices)
            for (x in map[0].indices)
                blizzards[y][x] = when (map[y][x]) {
                    '<' -> LEFT
                    '>' -> RIGHT
                    '^' -> UP
                    'v' -> DOWN
                    else -> 0
                }


        var round = 0
        while (true) {
            round++

            for (y in map.indices)
                for (x in map[0].indices) {
                    val b = blizzards[y][x]
                    if (b and LEFT != 0) {
                        if (x == 1) blizzardsSwap[y][width-2] = blizzardsSwap[y][width-2] or LEFT
                        else blizzardsSwap[y][x-1] = blizzardsSwap[y][x-1] or LEFT
                    }
                    if (b and RIGHT != 0) {
                        if (x == width-2) blizzardsSwap[y][1] = blizzardsSwap[y][1] or RIGHT
                        else blizzardsSwap[y][x+1] = blizzardsSwap[y][x+1] or RIGHT
                    }
                    if (b and UP != 0) {
                        if (y == 1) blizzardsSwap[height-2][x] = blizzardsSwap[height-2][x] or UP
                        else blizzardsSwap[y-1][x] = blizzardsSwap[y-1][x] or UP
                    }
                    if (b and DOWN != 0) {
                        if (y == height-2) blizzardsSwap[1][x] = blizzardsSwap[1][x] or DOWN
                        else blizzardsSwap[y+1][x] = blizzardsSwap[y+1][x] or DOWN
                    }
                    blizzards[y][x] = 0
                }
            blizzards = blizzardsSwap.also { blizzardsSwap = blizzards }

            queue@for (ignored in positions.indices) {
                val pos = positions.removeFirst()
                for (offset in ADJ) {
                    val x = pos.first + offset.first
                    val y = pos.second + offset.second
                    if (y < 0 || height <= y) continue
                    if (map[y][x] == '#') continue
                    if (blizzards[y][x] != 0) continue
                    if (counter[y][x] == round) continue
                    counter[y][x] = round
                    positions.add(Pair(x, y))

                    if (state == 0) {
                        if (y == height-1) {
                            positions.clear()
                            positions.add(Pair(x, y))
                            state = 1
                            break@queue
                        }
                    } else if (state == 1) {
                        if (y == 0) {
                            positions.clear()
                            positions.add(Pair(x, y))
                            state = 2
                            break@queue
                        }
                    } else
                        if (y == height-1) return println(round)
                }
            }
        }
    }

    override fun a1() = run()
    override fun a2() = run(true)

}