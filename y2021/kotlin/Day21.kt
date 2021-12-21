package y2021

import general.Day
import java.util.*

object Day21 : Day() {
    override val name = "Dirac Dice"

    private fun getPlayers() = INPUT.readLines()
        .map { it.substring(28).toInt() }

    override fun a1() {
        var (player1, player2) = getPlayers()
        var (score1, score2) = Pair(0, 0)
        var dice = 1

        while (true) {
            player1 = (player1 + 3 * dice + 3) % 10
            score1 += player1
            dice += 3

            if (player1 == 0) score1 += 10

            if (score1 >= 1000) {
                println(score2 * (dice - 1))
                return
            }

            // swap
            player1 = player2.also { player2 = player1 }
            score1 = score2.also { score2 = score1 }
        }
    }

    data class State(val pos1: Int, val score1: Int, val pos2: Int, val score2: Int)
    data class Wins(var win1: Long, var win2: Long) {
        fun max() = if (win1 > win2) win1 else win2
    }

    private fun getPossibilities(): IntArray {
        val possibilities = IntArray(10)

        for (a in 1..3)
            for (b in 1..3)
                for (c in 1..3)
                    possibilities[(a + b + c) % 10] += 1

        return possibilities
    }

    private fun getPossibilitiesMap(possibilities: IntArray): Array<IntArray> {
        val possibilitiesMap = Array(10) { IntArray(10) }

        for (offset in possibilitiesMap.indices)
            for (i in possibilities.indices)
                possibilitiesMap[offset][(i + offset) % 10] = possibilities[i]

        return possibilitiesMap
    }

    override fun a2() {
        val (player1, player2) = getPlayers()

        val scores = IntArray(10) { if (it == 0) 10 else it }
        val possibilities = getPossibilities()
        val possibilitiesMap = getPossibilitiesMap(possibilities)
        val cache = HashMap<State, Wins>()

        fun roll(pos1: Int, score1: Int, pos2: Int, score2: Int): Wins {
            val state = State(pos1, score1, pos2, score2)

            if (state.score1 >= 21) return Wins(1, 0)
            if (state.score2 >= 21) return Wins(0, 1)
            if (state in cache) return cache[state]!!

            val wins = Wins(0, 0)

            for (nextPos in 0..9) {
                val multiplier = possibilitiesMap[pos1][nextPos]
                if (multiplier == 0) continue
                val nextScore = score1 + scores[nextPos]

                val (wins2, wins1) = roll(pos2, score2, nextPos, nextScore)
                wins.win1 += wins1 * multiplier
                wins.win2 += wins2 * multiplier
            }

            cache[state] = wins
            return wins
        }

        println(roll(player1, 0, player2, 0).max())
        println("630797200227453")
    }
}