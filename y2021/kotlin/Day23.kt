package y2021

import general.Day
import java.io.File
import kotlin.collections.HashMap
import kotlin.collections.contains
import kotlin.collections.set

fun main() {
    Day23.a1()
}

object Day23 : Day() {
    override val name = "Amphipod"

    private const val FREE: Byte = 0
    private const val A: Byte = 1
    private const val B: Byte = 2
    private const val C: Byte = 3
    private const val D: Byte = 4

    private val COST_MULTIPLICATOR = intArrayOf(1, 10, 100, 1000)
    private val AMPHIPOD_TO_STACK = intArrayOf(0, 2, 4, 6)

    private val TRAVEL_INDICES = arrayOf(
        arrayOf(
            intArrayOf(1),
            intArrayOf(1, 2),
            intArrayOf(1, 2, 3),
            intArrayOf(1, 2, 3, 4),
        ),
        arrayOf(
            intArrayOf(),
            intArrayOf(2),
            intArrayOf(2, 3),
            intArrayOf(2, 3, 4),
        ),
        arrayOf(
            intArrayOf(),
            intArrayOf(),
            intArrayOf(3),
            intArrayOf(3, 4),
        ),
        arrayOf(
            intArrayOf(2),
            intArrayOf(),
            intArrayOf(),
            intArrayOf(4),
        ),
        arrayOf(
            intArrayOf(3, 2),
            intArrayOf(3),
            intArrayOf(),
            intArrayOf(),
        ),
        arrayOf(
            intArrayOf(4, 3, 2),
            intArrayOf(4, 3),
            intArrayOf(4),
            intArrayOf(),
        ),
        arrayOf(
            intArrayOf(5, 4, 3, 2),
            intArrayOf(5, 4, 3),
            intArrayOf(5, 4),
            intArrayOf(5),
        ),
    )
    private val TRAVEL_DISTANCES = arrayOf(
        intArrayOf(4, 3, 6, 5, 8, 7, 10, 9),
        intArrayOf(3, 2, 5, 4, 7, 6, 9, 8),
        intArrayOf(3, 2, 3, 2, 5, 4, 7, 6),
        intArrayOf(5, 4, 3, 2, 3, 2, 5, 4),
        intArrayOf(7, 6, 5, 4, 3, 2, 3, 2),
        intArrayOf(9, 8, 7, 6, 5, 4, 3, 2),
        intArrayOf(10, 9, 8, 7, 6, 5, 4, 3)
    )

    private operator fun Int.get(index: Int) = (this shr (index * 4) and 0b1111).toByte()
    private operator fun Int.set(index: Int, value: Byte): Int {
        var number = this
        number = (number and ((-1) xor (0b1111 shl (index * 4))))
        number = (number or (value.toInt() shl (index * 4)))
        return number
    }

    private fun Int.toHallwayString(): String {
        var string = ""
        for (h in 0..6)
            string += when (get(h)) {
                FREE -> "."
                A -> "A"
                B -> "B"
                C -> "C"
                D -> "D"
                else -> get(h)
            }
        return string
    }
    private fun Int.toStacksString(): String {
        var string = ""
        for (s in 0..7) {
            string += when (get(s)) {
                FREE -> "."
                A -> "A"
                B -> "B"
                C -> "C"
                D -> "D"
                else -> get(s)
            }
            if (s and 1 != 0) string += " "
        }
        return string
    }

    private fun toChar(number: Byte) = when (number) {
        FREE -> ' '
        A -> 'A'
        B -> 'B'
        C -> 'C'
        D -> 'D'
        else -> '?'
    }

    private fun toString(hallway: Int, stacks: Int) = """
        #############
        #${toChar(hallway[0])}${toChar(hallway[1])} ${toChar(hallway[2])} ${toChar(hallway[3])} ${toChar(hallway[4])} ${toChar(hallway[5])}${toChar(hallway[6])}#
        ###${toChar(stacks[1])}#${toChar(stacks[3])}#${toChar(stacks[5])}#${toChar(stacks[7])}###
          #${toChar(stacks[0])}#${toChar(stacks[2])}#${toChar(stacks[4])}#${toChar(stacks[6])}#
          #########
    """.trimIndent()

    private var PATH_INDEX = 0

    override fun a1() {

        val CORRECT = 0
            .set(0, A).set(1, A)
            .set(2, B).set(3, B)
            .set(4, C).set(5, C)
            .set(6, D).set(7, D)

        val cache = HashMap<Pair<Int, Int>, Int>()

        fun step(hallway: Int, stacks: Int, prevPath: Int): Int {
            val path = ++PATH_INDEX

//            run {
//                val f = File("C:/Users/Verzaukeks/Downloads/aoc", "$path.txt")
//                val p = File("C:/Users/Verzaukeks/Downloads/aoc", "$prevPath.txt")
//
//                f.writeText(toString(hallway, stacks))
//                f.appendText("\n\n")
//                f.appendText(p.readText())
//            }

            // check if all amphipods are in their correct stack
            if (stacks == CORRECT)
                return 0

            // caching
            val state = Pair(hallway, stacks)
            if (state in cache)
                return cache[state]!!

            // hallway to stack
            f@for (h in 0..6) {
                val amphipod = hallway[h]
                if (amphipod == FREE) continue

                // is stack accessible
                val stackId = AMPHIPOD_TO_STACK[amphipod - 1]
                if (stacks[stackId] != amphipod && stacks[stackId] != FREE) continue
                if (stacks[stackId + 1] != amphipod && stacks[stackId + 1] != FREE) continue

                // is travel not blocked
                for (ci in TRAVEL_INDICES[h][amphipod - 1])
                    if (hallway[ci] != FREE) continue@f

                // get lowest stack index
                var s = -1
                if (stacks[stackId] == FREE) s = stackId
                else if (stacks[stackId + 1] == FREE) s = stackId + 1

//                println("$path: h=$h, amphipod=$amphipod, stack=$stackId, s=$s")

                // calculate cost
                val distance = TRAVEL_DISTANCES[h][s]
                val cost = distance * COST_MULTIPLICATOR[amphipod - 1]

                val newHallway = hallway.set(h, FREE)
                val newStacks = stacks.set(s, amphipod)

                return cost + step(newHallway, newStacks, path)
            }

            // stack to hallway
            var minEnergy = 1_000_000_000

//            for (s in 0..7) {
            for (s_ in 0..7 step 2) {
//                val amphipod = stacks[s]
//                if (amphipod == FREE) continue

                // is movable from stack
                if (stacks[s_] == FREE && stacks[s_ + 1] == FREE) continue
                if (AMPHIPOD_TO_STACK[stacks[s_] - 1] == s_) {
                    if (stacks[s_ + 1] == FREE) continue
                    if (AMPHIPOD_TO_STACK[stacks[s_ + 1] - 1] == s_) continue
                }
//                val stack = AMPHIPOD_TO_STACK[amphipod - 1]
//                if (s and 1 == 0) {
//                    if (stack == s) continue
//                    if (stacks[s + 1] != FREE) continue
//                } else {
//                    if (stack == s - 1)
//                        if (AMPHIPOD_TO_STACK[stacks[s - 1] - 1] == s - 1)
//                            continue
//                }

                // get movable from stack
                var s = -1
                if (stacks[s_ + 1] != FREE) s = s_ + 1
                else if (stacks[s_] != FREE) s = s_
                val amphipod = stacks[s]

                // which hallway
                f@for (h in 0..6) {
                    if (hallway[h] != FREE) continue

                    // is travel not blocked
                    for (ci in TRAVEL_INDICES[h][s_ / 2])
                        if (hallway[ci] != FREE) continue@f

                    // calculate cost
                    val distance = TRAVEL_DISTANCES[h][s]
                    var cost = distance * COST_MULTIPLICATOR[amphipod - 1]

                    val newHallway = hallway.set(h, amphipod)
                    val newStacks = stacks.set(s, FREE)

                    cost += step(newHallway, newStacks, path)
                    if (cost < minEnergy)
                        minEnergy = cost

                    if (path < 30_000)
                    println("$cost  ->  path=$path, h=$h, s=$s")
                }
            }

            cache[state] = minEnergy
            return minEnergy
        }

//        println(step(0, 0
//            .set(0, A).set(1, B)
//            .set(2, D).set(3, C)
//            .set(4, C).set(5, B)
//            .set(6, A).set(7, D)
//        , 0))
        println(step(0, 0
            .set(0, B).set(1, A)
            .set(2, C).set(3, D)
            .set(4, D).set(5, A)
            .set(6, C).set(7, B)
        , 0))
    }

    override fun a2() {
    }
}