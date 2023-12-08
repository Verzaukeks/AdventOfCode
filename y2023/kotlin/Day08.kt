package y2023

import general.Day
import general.lcm

object Day08 : Day() {
    override val name = "Haunted Wasteland"

    private fun readInput(): Pair<String, Map<String, Pair<String, String>>> {
        val lines = INPUT.readLines()
        val instructions = lines[0]
        val nodes = lines.drop(2).associate {
            val a = it.substringBefore(" ")
            val b = it.substringAfter("(").substringBefore(",")
            val c = it.substringAfter(", ").substringBefore(")")
            a to Pair(b, c)
        }
        return Pair(instructions, nodes)
    }

    override fun a1() {
        val (instructions, nodes) = readInput()

        var steps = 0
        var curr = "AAA"
        while (curr != "ZZZ") {
            val l = instructions[(steps++) % instructions.length] == 'L'
            curr = if (l) nodes[curr]!!.first else nodes[curr]!!.second
        }

        println(steps)
    }

    override fun a2() {
        val (instructions, nodes) = readInput()

        nodes.filter { it.key.endsWith("A") }
            .map {
                var steps = 0
                var curr = it.key
                while (!curr.endsWith("Z")) {
                    val l = instructions[(steps++) % instructions.length] == 'L'
                    curr = if (l) nodes[curr]!!.first else nodes[curr]!!.second
                }
                steps.toLong()
            }
            .fold(1L, ::lcm)
            .also { println(it) }
    }
}