package y2021

import general.Day
import java.util.*

object Day14 : Day() {
    override val name = "Extended Polymerization"

    override fun a1() {
        val input = INPUT.readLines()

        var chainIndex: Int
        var chain = input[0]

        val map = TreeMap<String, String>()
        for (i in 2 until input.size) {
            val (from, to) = input[i].split(" -> ")
            map[from] = to
        }

        repeat(10) {
            chainIndex = 0

            while (chainIndex < chain.length - 1) {
                val subset = chain.substring(chainIndex, chainIndex + 2)
                val insert = map[subset]

                if (insert == null) {
                    chainIndex += 1
                    continue
                }

                chain = chain.substring(0, chainIndex + 1) +
                        insert +
                        chain.substring(chainIndex + 1)

                chainIndex += 2
            }
        }

        val count = IntArray('Z' - 'A' + 1)
        for (char in chain)
            count[char - 'A'] += 1

        val max = count.maxOrNull()!!
        val min = count.minOfOrNull { if (it == 0) Int.MAX_VALUE else it }!!

        println(max - min)
    }

    override fun a2() {
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

        repeat(40) {

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

        val max = count.maxOrNull()!!
        val min = count.minOfOrNull { if (it == 0L) Long.MAX_VALUE else it }!!

        // compensate double counting the every char
        val result = (max - min + 1) / 2

        println(result)
    }
}