package y2021

import general.Day
import java.util.*
import kotlin.collections.ArrayList

object Day12 : Day() {
    override val name = "Passage Pathing"

    override fun a1() = a(false)
    override fun a2() = a(true)

    class Data(val edges: Array<ArrayList<Int>>, val startIndex: Int, val endIndex: Int)

    class Path(val free: Boolean, val nodes: IntArray) {
        fun walk(free: Boolean, node: Int): Path {
            val nodes = this.nodes.copyOf(this.nodes.size + 1)
            nodes[nodes.lastIndex] = node
            return Path(free, nodes)
        }
    }

    private fun a(defaultFree: Boolean) {
        var indexSmall = 0
        var indexBig = 25
        val indexMap = TreeMap<String, Int>()
        val edges = Array(50) { ArrayList<Int>() }

        for (line in INPUT.readLines()) {
            val (from, to) = line.split("-")
            var fromIndex = indexMap[from] ?: -1
            var toIndex = indexMap[to] ?: -1

            if (fromIndex == -1) {
                fromIndex = if (from == from.toLowerCase()) indexSmall++ else indexBig++
                indexMap[from] = fromIndex
            }
            if (toIndex == -1) {
                toIndex = if (to == to.toLowerCase()) indexSmall++ else indexBig++
                indexMap[to] = toIndex
            }

            edges[fromIndex] += toIndex
            edges[toIndex] += fromIndex
        }

        val startIndex = indexMap["start"]!!
        val endIndex = indexMap["end"]!!
        val data = Data(edges, startIndex, endIndex)
        val path = Path(defaultFree, IntArray(1) { startIndex })

        val paths = getPathsAmount(data, path)
        println(paths)
    }

    private fun getPathsAmount(data: Data, path: Path): Int {
        val lastIndex = path.nodes.last()
        if (lastIndex == data.endIndex) return 1

        var amount = 0

        for (node in data.edges[lastIndex]) {
            if (node == data.startIndex) continue

            var free = path.free
            if (node < 25)
                if (node in path.nodes) {
                    if (!free) continue
                    free = false
                }

            val pathNew = path.walk(free, node)
            amount += getPathsAmount(data, pathNew)
        }

        return amount
    }
}