package y2022

import general.Day
import kotlin.math.max

typealias Field = Array<CharArray>

object Day17 : Day() {
    override val name = "Pyroclastic Flow"

    private val PIECES = listOf(
        Piece("####".split(",")),
        Piece(" # ,###, # ".split(",")),
        Piece("  #,  #,###".split(",")),
        Piece("#,#,#,#".split(",")),
        Piece("##,##".split(",")),
    )
    private val AVERAGE_PIECE_HEIGHT = PIECES.sumOf { it.height() }.toDouble() / PIECES.size.toDouble()

    class Piece(piece: List<String>) {
        private val raw = piece.reversed()

        fun height() = raw.size

        fun canPlace(x: Int, y: Int, field: Field): Boolean {
            if (x < 0 || field[0].size < x + raw[0].length) return false
            if (y < 0) return false;
            for (oy in raw.indices)
                for (ox in raw[oy].indices)
                    if (raw[oy][ox] == '#')
                        if (field[y + oy][x + ox] != '.')
                            return false
            return true
        }

        fun place(x: Int, y: Int, field: Field) {
            for (oy in raw.indices)
                for (ox in raw[oy].indices)
                    if (raw[oy][ox] == '#')
                        field[y + oy][x + ox] = '#'
        }
    }

    private fun simulate(rounds: Int): IntArray {
        val jets = INPUT.readText().toCharArray()
        val heights = IntArray(rounds)
        val field = Array((rounds * AVERAGE_PIECE_HEIGHT).toInt()) { CharArray(7) { '.' } }

        var restY = 0
        var jetI = 0
        repeat(rounds) {
            var x = 2
            var y = restY + 3
            val p = PIECES[it % PIECES.size]

            while (true) {
                if (jets[jetI] == '>') {
                    if (p.canPlace(x + 1, y, field)) x++
                } else {
                    if (p.canPlace(x - 1, y, field)) x--
                }
                jetI = (jetI + 1) % jets.size

                if (p.canPlace(x, y - 1, field)) y--
                else {
                    restY = max(restY, y + p.height())
                    heights[it] = restY
                    p.place(x, y, field)
                    break
                }
            }
        }

        return heights
    }

    override fun a1() {
        val heights = simulate(2022)
        println(heights.last())
    }

    override fun a2() {
        val ROUNDS = 100_000
        val heights = simulate(ROUNDS)
        val diffs = IntArray(ROUNDS)

        diffs[0] = heights[0]
        for (i in 1 until ROUNDS)
            diffs[i] = heights[i] - heights[i - 1]

        // find pattern length
        val offset = 10_000
        var length = PIECES.size
        loop@while (true) {
            length++
            for (i in 0 until length) {
                if (diffs[offset + i] != diffs[offset + i +     length]) continue@loop
                if (diffs[offset + i] != diffs[offset + i + 2 * length]) continue@loop
            }
            break
        }

        // calc result
        var rounds = 1000000000000L
        var result = 0L
        val patternHeight = heights[offset + length] - heights[offset]

        // heights before we check for the pattern cannot be assumed to follow the pattern
        rounds -= offset - 1
        result += heights[offset - 1]

        // quick modulo calc
        val times = rounds / length
        rounds -= times * length
        result += times * patternHeight

        // leftover rounds where the pattern is to big
        result += heights[offset + rounds.toInt() - 1] - heights[offset]

        println(result)
    }
}