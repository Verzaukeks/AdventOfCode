package y2019

import general.Day

object Day18 : Day() {
    override val name = "Many-Worlds Interpretation"

    data class CharData(
        var char: Char = 0.toChar(),
        var index: Int = -1,
        var x: Int = -1,
        var y: Int = -1,
        var dependencies: ArrayList<Int> = ArrayList(),
        var distances: IntArray = IntArray(53),
    )

    private fun charToIndex(char: Char): Int {
        if (char == '@') return 0
        if (char in 'a'..'z') return char - 'a' + 1
        if (char in 'A'..'Z') return char - 'A' + ('z' - 'a') + 2
        return -1
    }

    override fun a1() {
        val map = INPUT.readLines().map { it.toCharArray() }
        val charData = Array(53) { Day18.CharData() }

        initializeCharData(map, charData)
        initializeDependencies(map, charData)
        initializeDistances(map, charData)

        var distances = IntArray(53) { Int.MAX_VALUE }
        var paths = Array(53) { IntArray(0) }

        distances[0] = 0
        paths[0] = IntArray(1) { 0 }

        while (true) {
            val nextDistances = IntArray(53) { Int.MAX_VALUE }
            val nextPaths = Array(53) { IntArray(0) }

            for (fromIndex in 0..52)
                if (distances[fromIndex] != Int.MAX_VALUE) {

                    for (toIndex in 0..52)
                        if (canAccess(charData, toIndex, paths[fromIndex])) {

                            val steps = distances[fromIndex] + charData[fromIndex].distances[toIndex]
                            if (nextDistances[toIndex] >= steps) {

                                val p = paths[fromIndex]
                                val path = IntArray(p.size + 1)
                                for (i in p.indices) path[i] = p[i]
                                path[path.lastIndex] = toIndex

                                nextDistances[toIndex] = steps
                                nextPaths[toIndex] = path
                            }
                        }
                }

            if (nextDistances.all { it == Int.MAX_VALUE })
                break

            distances = nextDistances
            paths = nextPaths
        }

        for (i in distances.indices)
            if (distances[i] != Int.MAX_VALUE)
                println(distances[i].toString() + ": " + paths[i].joinToString("") {
                    if (it == 0) "@"
                    else if (it in 1..26) ", " + (it - 1 + 'a'.toInt()).toChar().toString()
                    else if (it in 27..52) (it - 27 + 'A'.toInt()).toChar().toString()
                    else ""
                })

//        println(charData.filter { it.index != -1 }.joinToString("\n"))

        println("Shortest path is: " + distances.minOrNull())
    }

    private fun canAccess(charData: Array<Day18.CharData>, toIndex: Int, path: IntArray): Boolean {
        if (toIndex in path) return false
        if (charData[toIndex].index == -1) return false

        for (dependency in charData[toIndex].dependencies)
            if (dependency !in path)
                return false

        return true
    }

    private fun initializeCharData(map: List<CharArray>, charData: Array<Day18.CharData>) {
        for (y in map.indices)
            for (x in map[y].indices) {

                val char = map[y][x]
                val index = charToIndex(char)
                if (index == -1) continue

                val data = charData[index]
                data.char = char
                data.index = index
                data.x = x
                data.y = y
            }
    }

    private fun initializeDependencies(map: List<CharArray>, charData: Array<Day18.CharData>) {

        val startPos = Pair(charData[0].x, charData[0].y)
        val dependencies = ArrayList<Int>()
        val checkList = ArrayList<Pair<Int, Int>>()

        clearRoom(map, charData, startPos, dependencies, checkList)
    }

    private fun clearRoom(map: List<CharArray>, charData: Array<Day18.CharData>, startPos: Pair<Int, Int>, dependencies: ArrayList<Int>, checkList: ArrayList<Pair<Int, Int>>) {

        var checkIndex = checkList.size
        var checkRange: IntRange
        val newAreas = ArrayList<Pair<Int, Int>>()

        checkList += startPos

        while (checkIndex < checkList.size) {
            checkRange = checkIndex until checkList.size
            checkIndex = checkList.size

            for (checkI in checkRange) {
                val toCheck = checkList[checkI]
                val char = map[toCheck.second][toCheck.first]
                val index = charToIndex(char)

                if (char == '#') continue
                if (char in 'a'..'z') charData[index].dependencies += dependencies
                if (char in 'A'..'Z' && toCheck != startPos) {
                    charData[index].dependencies += dependencies
                    charData[index].dependencies += char - 'A' + 1

                    newAreas += toCheck
                    continue
                }

                val a = Pair(toCheck.first + 1, toCheck.second)
                val b = Pair(toCheck.first - 1, toCheck.second)
                val c = Pair(toCheck.first, toCheck.second + 1)
                val d = Pair(toCheck.first, toCheck.second - 1)

                if (a !in checkList) checkList += a
                if (b !in checkList) checkList += b
                if (c !in checkList) checkList += c
                if (d !in checkList) checkList += d
            }
        }


        for (newArea in newAreas)
            clearRoom(map, charData, newArea,
                ArrayList<Int>().apply {
                    addAll(dependencies)
                    add(charToIndex(map[newArea.second][newArea.first]))
                }, ArrayList<Pair<Int, Int>>().apply {
                    addAll(checkList)
                })
    }

    private fun initializeDistances(map: List<CharArray>, charData: Array<Day18.CharData>) {
        for (fromCharData in charData)
            if (fromCharData.index != -1) {

                var distance = 0
                var checkIndex = 0
                val checkList = ArrayList<Pair<Int, Int>>()
                var checkRange: IntProgression

                checkList += Pair(fromCharData.x, fromCharData.y)

                while (checkIndex < checkList.size) {
                    checkRange = checkIndex until checkList.size
                    checkIndex = checkList.size

                    for (checkI in checkRange) {
                        val toCheck = checkList[checkI]
                        val char = map[toCheck.second][toCheck.first]
                        val index = charToIndex(char)

                        if (char == '#') continue
                        if (index != -1) fromCharData.distances[index] = distance

                        val a = Pair(toCheck.first + 1, toCheck.second)
                        val b = Pair(toCheck.first - 1, toCheck.second)
                        val c = Pair(toCheck.first, toCheck.second + 1)
                        val d = Pair(toCheck.first, toCheck.second - 1)

                        if (a !in checkList) checkList += a
                        if (b !in checkList) checkList += b
                        if (c !in checkList) checkList += c
                        if (d !in checkList) checkList += d
                    }

                    distance += 1
                }
            }
    }

    override fun a2() {}
}