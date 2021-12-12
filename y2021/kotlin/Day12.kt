package y2021

import general.Day
import java.util.*
import kotlin.collections.ArrayList

object Day12 : Day() {
    override val name = "Passage Pathing"

    override fun a1() = a(false)
    override fun a2() = a(true)

    class Path(val free: Boolean, val nodes: IntArray) {
        fun walk(free: Boolean, node: Int): Path {
            val nodes = this.nodes.copyOf(this.nodes.size + 1)
            nodes[nodes.lastIndex] = node
            return Path(free, nodes)
        }
    }

    // Not in the mood to improve things as of now
    private fun a(defaultFree: Boolean) {
        var indexSmall = 0
        var indexBig = 25
        val indexMap = TreeMap<String, Int>()
        val edges = Array(50) { BooleanArray(50) }

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

            edges[fromIndex][toIndex] = true
            edges[toIndex][fromIndex] = true
        }

        val startIndex = indexMap["start"]!!
        val endIndex = indexMap["end"]!!

        val paths = LinkedList<Path>()
        val finished = ArrayList<Path>()

        paths += Path(defaultFree, IntArray(1) { startIndex })

        while (paths.isNotEmpty()) {
            for (i in 1..paths.size) {
                val path = paths.removeFirst()
                val lastIndex = path.nodes.last()

                if (lastIndex == endIndex) {
                    finished += path
                    continue
                }

                val conns = edges[lastIndex]
                for (node in conns.indices) {
                    if (!conns[node]) continue
                    if (node == startIndex) continue

                    var free = path.free
                    if (node < 25)
                        if (node in path.nodes) {
                            if (!free) continue
                            free = false
                        }

                    val pathNew = path.walk(free, node)
                    if (pathNew !in paths)
                        paths += pathNew
                }
            }

//            println("${paths.size} // ${finished.size}")
        }

        println(finished.size)
    }
}