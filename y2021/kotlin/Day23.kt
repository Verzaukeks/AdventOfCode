package y2021

import general.Day

object Day23 : Day() {
    override val name = "Amphipod"

    private const val FREE: Byte = 0
    private const val A: Byte = 1
    private const val B: Byte = 2
    private const val C: Byte = 3
    private const val D: Byte = 4

    private val COST_MULTIPLICATOR = intArrayOf(1, 10, 100, 1000)
    private val AMPHIPOD_TO_STACK = intArrayOf(0, 4, 8, 12)

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
        intArrayOf( 6,  5,  4, 3,    8, 7, 6, 5,   10, 9, 8, 7,   12, 11, 10, 9),
        intArrayOf( 5,  4,  3, 2,    7, 6, 5, 4,    9, 8, 7, 6,   11, 10,  9, 8),
        intArrayOf( 5,  4,  3, 2,    5, 4, 3, 2,    7, 6, 5, 4,    9,  8,  7, 6),
        intArrayOf( 7,  6,  5, 4,    5, 4, 3, 2,    5, 4, 3, 2,    7,  6,  5, 4),
        intArrayOf( 9,  8,  7, 6,    7, 6, 5, 4,    5, 4, 3, 2,    5,  4,  3, 2),
        intArrayOf(11, 10,  9, 8,    9, 8, 7, 6,    7, 6, 5, 4,    5,  4,  3, 2),
        intArrayOf(12, 11, 10, 9,   10, 9, 8, 7,    8, 7, 6, 5,    6,  5,  4, 3),
    )

    private operator fun Long.get(index: Int) = (this shr (index * 4) and 0b1111).toByte()
    private operator fun Long.set(index: Int, value: Byte): Long {
        var number = this
        number = (number and ((-1L) xor (0b1111L shl (index * 4))))
        number = (number or (value.toLong() shl (index * 4)))
        return number
    }

    private fun getStacks(): ByteArray {
        val lines = INPUT.readLines()
        val out = ByteArray(8)

        fun Char.charToByte() = when (this) {
            'A' -> A
            'B' -> B
            'C' -> C
            'D' -> D
            else -> throw Error("$this")
        }

        out[0] = lines[3][3].charToByte()
        out[1] = lines[2][3].charToByte()
        out[2] = lines[3][5].charToByte()
        out[3] = lines[2][5].charToByte()
        out[4] = lines[3][7].charToByte()
        out[5] = lines[2][7].charToByte()
        out[6] = lines[3][9].charToByte()
        out[7] = lines[2][9].charToByte()

        return out
    }

    override fun a1() {
        val stacks = getStacks()

        a(0L
            .set( 0, A).set( 1, A).set( 2, stacks[0]).set( 3, stacks[1])
            .set( 4, B).set( 5, B).set( 6, stacks[2]).set( 7, stacks[3])
            .set( 8, C).set( 9, C).set(10, stacks[4]).set(11, stacks[5])
            .set(12, D).set(13, D).set(14, stacks[6]).set(15, stacks[7]))
    }

    override fun a2() {
        val stacks = getStacks()

        a(0L
            .set( 0, stacks[0]).set( 1, D).set( 2, D).set( 3, stacks[1])
            .set( 4, stacks[2]).set( 5, B).set( 6, C).set( 7, stacks[3])
            .set( 8, stacks[4]).set( 9, A).set(10, B).set(11, stacks[5])
            .set(12, stacks[6]).set(13, C).set(14, A).set(15, stacks[7]))
    }

    private fun a(stacks: Long) {

        val CORRECT = 0L
            .set( 0, A).set( 1, A).set( 2, A).set( 3, A)
            .set( 4, B).set( 5, B).set( 6, B).set( 7, B)
            .set( 8, C).set( 9, C).set(10, C).set(11, C)
            .set(12, D).set(13, D).set(14, D).set(15, D)

        val cache = HashMap<Pair<Long, Long>, Int>()

        fun step(hallway: Long, stacks: Long): Int {

            // check if all amphipods are in their correct stack
            if (stacks == CORRECT) return 0

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
                for (index in 0..3)
                    if (stacks[stackId + index] != amphipod && stacks[stackId + index] != FREE)
                        continue@f

                // is travel not blocked
                for (ci in TRAVEL_INDICES[h][amphipod - 1])
                    if (hallway[ci] != FREE) continue@f

                // get lowest stack index
                var s = stackId
                while (stacks[s] != FREE) s += 1

                // calculate cost
                val distance = TRAVEL_DISTANCES[h][s]
                val cost = distance * COST_MULTIPLICATOR[amphipod - 1]

                val newHallway = hallway.set(h, FREE)
                val newStacks = stacks.set(s, amphipod)

                return cost + step(newHallway, newStacks)
            }

            // stack to hallway
            var minEnergy = 1_000_000_000

            f@for (stackId in 0..15 step 4) {

                // is movable from stack
                for (i in 0..3) {
                    if (stacks[stackId + i] == FREE) continue@f
                    if (AMPHIPOD_TO_STACK[stacks[stackId + i] - 1] != stackId) break
                    if (i == 3) continue@f
                }

                // get movable from stack
                var s = stackId + 3
                while (stacks[s] == FREE) s -= 1
                val amphipod = stacks[s]


                // which hallway
                f@for (h in 0..6) {
                    if (hallway[h] != FREE) continue

                    // is travel not blocked
                    for (ci in TRAVEL_INDICES[h][stackId / 4])
                        if (hallway[ci] != FREE) continue@f

                    // calculate cost
                    val distance = TRAVEL_DISTANCES[h][s]
                    var cost = distance * COST_MULTIPLICATOR[amphipod - 1]

                    val newHallway = hallway.set(h, amphipod)
                    val newStacks = stacks.set(s, FREE)

                    cost += step(newHallway, newStacks)
                    if (cost < minEnergy)
                        minEnergy = cost
                }
            }

            cache[state] = minEnergy
            return minEnergy
        }

        println(step(0, stacks))
    }
}