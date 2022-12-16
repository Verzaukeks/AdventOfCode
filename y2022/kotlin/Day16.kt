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

    // assign to each valve an index for better iteration
    private val nameToId = TreeMap<String, Int>()
    // MASK = set of all indices of valves with flow-rate
    private val MASK = (1 shl VALVES_WITH_FLOW_RATE) - 1
    // easy access index for starting point valve AA
    private val AA = VALVES_WITH_FLOW_RATE // readGraph() "assures" assign to this value

    // contains flow-rate for each valve
    // (valves with flow-rate have lower indices than valves without flow-rate)
    // (=> values without flow-rate are not stored)
    private val nodes = ArrayList<Int>()
    // adjacency list for each node
    private val graph = Array<List<Int>>(VALVES_COUNT) { ArrayList() }

    // distance[i][j] = path length from node i to node j
    // (i has to be a valve with flow-rate or the starting point AA)
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

        // ensure index of AA = VALVES_WITH_FLOW_RATE for better iteration/caching
        if (maxId != VALVES_WITH_FLOW_RATE) throw IllegalStateException("Found $maxId valves with flow-rate instead of $VALVES_WITH_FLOW_RATE")
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
        // default breath-first-search implemenation for shortest distance
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
    // i = node we are currently standing in
    // mask = set of all valves with flow-rate we have yet to open if we can
    // time = time left before the volcano erupts
    private fun dynamicProgramming(i: Int, mask: Int, time: Int): Int {
        // base-cases: no more valve to open, no more time left, case already computed
        if (mask == 0) return 0
        if (time <= 0) return 0
        if (cache[i][mask][time] != -1) return cache[i][mask][time]

        // maximize all available options
        var res = 0
        for (j in 0 until VALVES_WITH_FLOW_RATE) {
            // we can only open valves we previously have not opened
            val mId = 1 shl j
            if ((mask and mId) == 0) continue

            val m = mask xor mId // remove valve-id from set
            val t = time - distance[i][j] - 1 // time - travel time to j - time to open valve
            val pressure = t * nodes[j] // amount of pressure released after the valve is opened
            res = max(res, dynamicProgramming(j, m, t) + pressure)
        }

        cache[i][mask][time] = res
        return res
    }

    override fun a1() {
        init()
        // AA = start point
        // MASK = set of all valves we can open
        // 30 = time left
        println(dynamicProgramming(AA, MASK, 30))
    }

    override fun a2() {
        init()
        var res = 0
        // we only have to iterate over half, because (mask <-> MASK xor mask) is symmetric
        for (mask in 0..(MASK / 2)) {
            val a = dynamicProgramming(AA, mask, 26)
            val b = dynamicProgramming(AA, MASK xor mask, 26)
            res = max(res, a + b)
        }
        println(res)
    }
}