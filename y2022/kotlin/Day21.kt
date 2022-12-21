package y2022

import general.Day
import java.util.*

object Day21 : Day() {
    override val name = "Monkey Math"

    override fun a1() {
        val nameToIndex = TreeMap<String, Int>()
        INPUT.readLines().forEach { nameToIndex[it.substringBefore(":")] = nameToIndex.size }
        val nodes = Array(nameToIndex.size) { Triple(0, ' ', 0) }
        INPUT.readLines().forEach {
            val split = it.split(" ")
            val id = nameToIndex[it.substringBefore(":")]!!
            val idx = it.indexOfAny("+-*/".toCharArray())
            if (idx == -1) nodes[id] = Triple(split[1].toInt(), ' ', 0)
            else nodes[id] = Triple(nameToIndex[split[1]]!!, it[idx], nameToIndex[split[3]]!!)
        }

        val visited = BooleanArray(nodes.size)
        val top = ArrayList<Int>(nodes.size)
        fun dfs(i: Int) {
            if (visited[i]) return
            visited[i] = true
            if (nodes[i].second != ' ') {
                dfs(nodes[i].first)
                dfs(nodes[i].third)
            }
            top += i
        }
        for (i in nodes.indices) dfs(i)

        val values = LongArray(nodes.size)
        for ((id, n) in top.associateWith { nodes[it] })
            values[id] = when (n.second) {
                '+' -> values[n.first] + values[n.third]
                '-' -> values[n.first] - values[n.third]
                '*' -> values[n.first] * values[n.third]
                '/' -> values[n.first] / values[n.third]
                else -> n.first.toLong()
            }

        println(values[nameToIndex["root"]!!])
    }

    override fun a2() {
        val nameToIndex = TreeMap<String, Int>()
        INPUT.readLines().forEach { nameToIndex[it.substringBefore(":")] = nameToIndex.size }
        val nodes = Array(nameToIndex.size) { Triple(0, ' ', 0) }
        INPUT.readLines().forEach {
            val split = it.split(" ")
            val id = nameToIndex[it.substringBefore(":")]!!
            val idx = it.indexOfAny("+-*/".toCharArray())
            if (idx == -1) nodes[id] = Triple(split[1].toInt(), ' ', 0)
            else nodes[id] = Triple(nameToIndex[split[1]]!!, it[idx], nameToIndex[split[3]]!!)
        }

        val parent = IntArray(nodes.size) { -1 }
        for ((i, n) in nodes.withIndex())
            if (n.second != ' ') {
                if (parent[n.first] != -1) System.err.println("Input is not a tree. Solution may fail.")
                if (parent[n.third] != -1) System.err.println("Input is not a tree. Solution may fail.")
                parent[n.first] = i
                parent[n.third] = i
            }

        fun down(n: Int): Long =
            when (nodes[n].second) {
                '+' -> down(nodes[n].first) + down(nodes[n].third)
                '-' -> down(nodes[n].first) - down(nodes[n].third)
                '*' -> down(nodes[n].first) * down(nodes[n].third)
                '/' -> down(nodes[n].first) / down(nodes[n].third)
                else -> nodes[n].first.toLong()
            }

        fun up(i: Int): Long {
            val p = nodes[parent[i]]
            val part = down(p.first xor p.third xor i)
            if (parent[parent[i]] == -1) return part

            val left = p.first == i
            val res = up(parent[i])
            return when (p.second) {
                '+' -> res - part
                '-' -> if (left) res + part else part - res
                '*' -> res / part
                '/' -> if (left) res * part else part / res
                else -> throw IllegalStateException("parent of node has no children")
            }
        }

        println(up(nameToIndex["humn"]!!))
    }
}