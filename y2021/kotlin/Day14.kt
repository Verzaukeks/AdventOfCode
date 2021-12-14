package y2021

import general.Day
import java.util.*

object Day14 : Day() {
    override val name = "Extended Polymerization"

    override fun a1() = a(10)
    override fun a2() = a(40)

    private fun a(steps: Int) {
        val input = INPUT.readLines()

        var contains = TreeMap<String, Long>()
        var containsTo = TreeMap<String, Long>()
        val creates = TreeMap<String, Pair<String, String>>()

        //  XY -> Z  ::  Map[XY -> (XZ, ZY)]
        for (i in 2 until input.size) {
            val (from, to) = input[i].split(" -> ")
            creates[from] = Pair("${from[0]}$to", "$to${from[1]}")
        }

        // Count original pairs
        val chain = input[0]
        for (i in 0 until chain.length - 1) {
            val subset = chain.substring(i, i + 2)
            contains[subset] = (contains[subset] ?: 0) + 1
        }

        repeat(steps) {

            for ((from, to) in creates) {
                val (to0, to1) = to
                val fromAmount = contains[from] ?: 0

                containsTo[to0] = (containsTo[to0] ?: 0) + fromAmount
                containsTo[to1] = (containsTo[to1] ?: 0) + fromAmount
            }

            // swap & clear
            contains = containsTo.also { containsTo = contains }
            for (key in containsTo.keys) containsTo[key] = 0
        }

        val count = LongArray('Z' - 'A' + 1)
        for ((combi, amount) in contains.entries) {
            count[combi[0] - 'A'] += amount
            count[combi[1] - 'A'] += amount
        }

        // help the two letters, which are only counted once
        // (first char in chain has no partner to the left )
        // ( last char in chain has no partner to the right)
        count[chain.first() - 'A'] += 1L
        count[chain.last()  - 'A'] += 1L

        val max = count.maxOrNull()!!
        val min = count.minOfOrNull { if (it == 0L) Long.MAX_VALUE else it }!!

        // compensate double counting every char
        val result = (max - min) / 2

        println(result)
    }
}
