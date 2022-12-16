package y2022

import general.Day
import general.len
import java.util.*
import kotlin.math.max

object Day16 : Day() {
    override val name = "Proboscidea Volcanium"

    private const val DEBUG = false
    private const val VALVES_WITH_FLOW_RATE = 15
    private const val VALVES_COUNT = 46

    private val nameToId = TreeMap<String, Int>()
    private val MASK = (1 shl VALVES_WITH_FLOW_RATE) - 1
    private val AA = VALVES_WITH_FLOW_RATE // readGraph() "assures" assign to this value

    private val nodes = ArrayList<Int>()
    private val graph = Array<List<Int>>(VALVES_COUNT) { ArrayList() }

    private val distance = Array(VALVES_WITH_FLOW_RATE + 1) { IntArray(VALVES_COUNT) { 0x42424242 } }

    private var initialized = false
    private fun init() {
        if (initialized) return
        initialized = true

        readGraph()
        calcShortestPath()
    }

    private fun readGraph() {
        var maxId = 0

        // enforce valves with output flow-rate to have the first indices
        INPUT.readLines().forEach {
            val source = it.split(" ")[1]
            val rate = it.split("=", ";")[1].toInt()
            if (rate != 0) {
                nodes += rate
                nameToId[source] = maxId++
            }
        }

        nameToId["AA"] = maxId++

        // assign indices to all other valves
        INPUT.readLines().forEach {
            val source = it.split(" ")[1]
            if (source !in nameToId) nameToId[source] = maxId++
        }

        // create graph
        INPUT.readLines().forEach {
            val source = nameToId[it.split(" ")[1]]!!
            val tunnels = it.substringAfter("valves ")
                .substringAfter("valve ")
                .split(", ")
                .map { nameToId[it]!! }

            graph[source] = tunnels.toList()
        }

        // DEBUG
        if (DEBUG) {
            for (name in nameToId.keys) print("$name ") ; println()
            for (id in nameToId.values) print("$id ".len(3)) ; println()
            for (id in nameToId.values) if (id < VALVES_WITH_FLOW_RATE) print("${nodes[id]} ".len(3)) else print("   ") ; println()
            println()
        }
    }

    private fun calcShortestPath() {
        fun bfs(node: Int) {
            val queue = LinkedList<Int>()

            distance[node][node] = 0
            queue.add(node)

            while (queue.isNotEmpty()) {
                val curr = queue.pop()

                for (adj in graph[curr]) {
                    val dis = distance[node][curr] + 1
                    if (distance[node][adj] <= dis) continue

                    distance[node][adj] = dis
                    queue.add(adj)
                }
            }
        }

        // determine distance for all valves with flow-rates and our starting point
        for (node in 0..VALVES_WITH_FLOW_RATE) bfs(node)

        // DEBUG
        if (DEBUG) {
            print("   ") ; for (name in nameToId.keys) print("$name ") ; println()
            for ((name, id) in nameToId.entries) {
                if (VALVES_WITH_FLOW_RATE < id) continue
                print("$name ") ; for (to in nameToId.values) print("${distance[id][to]} ".len(3)) ; println()
            }
            println()
        }
    }

    private val cache = Array(VALVES_WITH_FLOW_RATE + 1) { Array(MASK + 1) { IntArray(31) { -1 } } }
    private fun dynamicProgramming(i: Int, mask: Int, time: Int): Int {
        if (mask == 0) return 0
        if (time <= 0) return 0
        if (cache[i][mask][time] != -1) return cache[i][mask][time]

        var res = 0
        for (j in 0 until VALVES_WITH_FLOW_RATE) {
            val mId = 1 shl j
            if ((mask and mId) == 0) continue

            val m = mask xor mId
            val t = time - distance[i][j] - 1
            val pressure = t * nodes[j]
            res = max(res, dynamicProgramming(j, m, t) + pressure)
        }

        cache[i][mask][time] = res
        return res
    }

    override fun a1() {
        init()
        println(dynamicProgramming(AA, MASK, 30))
    }

    override fun a2() {
        init()
        var res = 0
        for (mask in 0..(MASK / 2)) {
            val a = dynamicProgramming(AA, mask, 26)
            val b = dynamicProgramming(AA, MASK xor mask, 26)
            res = max(res, a + b)
        }
        println(res)
    }
}