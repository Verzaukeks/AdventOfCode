package y2019

import general.Day
import java.lang.Thread.sleep
import kotlin.system.exitProcess

object Day17 : Day() {
    override val name = "Set and Forget"

    override fun a1() {
        val height = 39
        val width = 47
        val map = Array(height) { Array(width) {'.'} }
        var posY = 0
        var posX = 0

        Day11.intCodeProgram(
            dataFile = INPUT,
            output = {
//                print(it.toInt().toChar())
                if (it == '\n'.toLong()) { posY++ ; posX = 0 }
                else map[posY][posX++] = it.toInt().toChar()
            }
        )

        var sum = 0

        for (y in 1..height-2) for (x in 1..width-2)
            if (
                map[y][x] == '#' &&
                map[y + 1][x] == '#' &&
                map[y - 1][x] == '#' &&
                map[y][x + 1] == '#' &&
                map[y][x - 1] == '#'
            ) {
                sum += y * x
            }

        println(sum)
    }

    override fun a2() {
        val height = 39
        val width = 47
        val map = Array(height) { Array(width) {'.'} }
        var posY = 0
        var posX = 0

        var rules = ""
        var inputPointer = 0
        var ySync = 0
        var lastOutput = 0L

        Day11.intCodeProgram(
            dataFile = INPUT,
            input = {
                rules[inputPointer++].toLong()
            },
            output = {
                lastOutput = it
                print(it.toInt().toChar())

                if (rules.isEmpty())
                    if (it == '\n'.toLong()) { posY++ ; posX = 0 }
                    else map[posY][posX++] = it.toInt().toChar()

                if (it == '\n'.toLong()) ySync++
                else ySync = 0

                if (ySync == 2) {
                    sleep(100)
                    ySync = 0

                    if (rules.isEmpty())
                        rules = generateRules(map) + "y\n"      // Change to "n\n" to disable live feed (faster)
                }
            },
            onLoad = {
                it[0] = 2
            }
        )

        println()
        println("Solution: $lastOutput")
    }

/*
    Tried to think of another solution, but through this code
    I found out that my first/current solution had a bug.
    Fixed it, and now it works.
    Letting this garbage stay here so I can look back
    at my stupid flaws.
*/
/*
    private fun generateRules(map: Array<Array<Char>>): String {
        val rootRule = createRootRule(map)

        println(rootRule)
        exitProcess(0)
    }

    private fun createRootRule(map: Array<Array<Char>>): String {
        var rule = ""

        val HEIGHT = map.size
        val WIDTH  = map[0].size
        val NORTH = 0
        val EAST  = 1
        val SOUTH = 2
        val WEST  = 3


        var posX = 0
        var posY = 0
        var rot = NORTH
        locate@for (y in map.indices) for (x in map[y].indices)
            if (map[y][x] == '^') {
                posX = x
                posY = y
                break@locate
            }

        while (true) {
            var disNorth = 0
            var disSouth = 0
            var disWest = 0
            var disEast = 0

            while (posY - disNorth >        0 && map[posY - disNorth - 1][posX] == '#') disNorth++
            while (posY + disSouth < HEIGHT-1 && map[posY + disSouth + 1][posX] == '#') disSouth++
            while (posX - disWest  >        0 && map[posY][posX - disWest - 1]  == '#') disWest++
            while (posX + disEast  <  WIDTH-1 && map[posY][posX + disEast + 1]  == '#') disEast++

            if (rot == NORTH) disSouth = 0
            if (rot == SOUTH) disNorth = 0
            if (rot == WEST) disEast = 0
            if (rot == EAST) disWest = 0

            when {
                disNorth != 0 -> {
                    if (rot == EAST) rule += ",L"
                    if (rot == WEST) rule += ",R"
                    rule += ",$disNorth"
                    posY -= disNorth
                    rot = NORTH
                }
                disSouth != 0 -> {
                    if (rot == EAST) rule += ",R"
                    if (rot == WEST) rule += ",L"
                    rule += ",$disSouth"
                    posY += disSouth
                    rot = SOUTH
                }
                disWest != 0 -> {
                    if (rot == NORTH) rule += ",L"
                    if (rot == SOUTH) rule += ",R"
                    rule += ",$disWest"
                    posX -= disWest
                    rot = WEST
                }
                disEast != 0 -> {
                    if (rot == NORTH) rule += ",R"
                    if (rot == SOUTH) rule += ",L"
                    rule += ",$disEast"
                    posX += disEast
                    rot = EAST
                }
                else -> break
            }
        }

        return rule.substring(1)
    }
*/

    enum class Direction {
        NONE, NORTH, EAST, SOUTH, WEST;

        fun intersects(dir: Direction): String {
            if (ordinal + 1 == dir.ordinal || (this == WEST && dir == NORTH)) return "R"
            if (ordinal - 1 == dir.ordinal || (this == NORTH && dir == WEST)) return "L"
            return "?"
        }
    }

    private fun generateRules(map: Array<Array<Char>>): String {
        val traceList = toTraceList(map)
        var pos = 0
        var calls = ""

        var ra = ""
        var rb = ""
        var rc = ""
        var rPos = 0
        var rCalls = ""

        for (lengthA in 1 until traceList.size - pos) {
            val tempPosA = pos
            val tempCallsA = calls
            val ruleA = generateRuleSet(traceList, pos, pos + lengthA) ?: break

            can@while (true) {
                while (canApplyRuleset(ruleA, traceList, pos)) { pos += lengthA ; calls += ",A" ; continue@can }
                break
            }

            if (pos > rPos) {
                ra = ruleA
                rb = ""
                rc = ""
                rPos = pos
                rCalls = calls
            }

            for (lengthB in 1 until traceList.size - pos) {
                val tempPosB = pos
                val tempCallsB = calls
                val ruleB = generateRuleSet(traceList, pos, pos + lengthB) ?: break

                can@while (true) {
                    while (canApplyRuleset(ruleA, traceList, pos)) { pos += lengthA ; calls += ",A" ; continue@can }
                    while (canApplyRuleset(ruleB, traceList, pos)) { pos += lengthB ; calls += ",B" ; continue@can }
                    break
                }

                if (pos > rPos) {
                    ra = ruleA
                    rb = ruleB
                    rc = ""
                    rPos = pos
                    rCalls = calls
                }

                for (lengthC in 1 until traceList.size - pos) {
                    val tempPosC = pos
                    val tempCallsC = calls
                    val ruleC = generateRuleSet(traceList, pos, pos + lengthC) ?: break

                    can@while (true) {
                        while (canApplyRuleset(ruleA, traceList, pos)) { pos += lengthA ; calls += ",A" ; continue@can }
                        while (canApplyRuleset(ruleB, traceList, pos)) { pos += lengthB ; calls += ",B" ; continue@can }
                        while (canApplyRuleset(ruleC, traceList, pos)) { pos += lengthC ; calls += ",C" ; continue@can }
                        break
                    }

                    if (pos > rPos) {
                        ra = ruleA
                        rb = ruleB
                        rc = ruleC
                        rPos = pos
                        rCalls = calls
                    }

                    if (pos == traceList.size) {
//                        println(calls)
//                        println(ruleA)
//                        println(ruleB)
//                        println(ruleC)
//                        println("SUCCESS")
//                        exitProcess(0)
                        return "$calls\n$ruleA\n$ruleB\n$ruleC\n".substring(1)
                    }

                    pos = tempPosC
                    calls = tempCallsC
                }
                pos = tempPosB
                calls = tempCallsB
            }
            pos = tempPosA
            calls = tempCallsA
        }

        println(rCalls)
        println(ra)
        println(rb)
        println(rc)
        println(rPos)
        println("Failure")
        exitProcess(0)
    }

    private fun toTraceList(map: Array<Array<Char>>): ArrayList<Direction> {
        val list = ArrayList<Direction>()
        val height = map.size
        val width = map[0].size

        var posX = 0
        var posY = 0
        var rot = Direction.NONE
        locate@for (y in map.indices) for (x in map[y].indices)
            if (map[y][x] == '^') {
                posX = x
                posY = y
                break@locate
            }

        while (true) {
            var disNorth = 0
            var disSouth = 0
            var disWest = 0
            var disEast = 0

            while (posY - disNorth >        0 && map[posY - disNorth - 1][posX] == '#') disNorth++
            while (posY + disSouth < height-1 && map[posY + disSouth + 1][posX] == '#') disSouth++
            while (posX - disWest  >        0 && map[posY][posX - disWest - 1]  == '#') disWest++
            while (posX + disEast  <  width-1 && map[posY][posX + disEast + 1]  == '#') disEast++

            if (rot == Direction.NORTH) disSouth = 0
            if (rot == Direction.SOUTH) disNorth = 0
            if (rot == Direction.WEST) disEast = 0
            if (rot == Direction.EAST) disWest = 0

            when {
                disNorth != 0 -> {
                    posY -= disNorth
                    rot = Direction.NORTH
                    repeat(disNorth) { list += rot }
                }
                disSouth != 0 -> {
                    posY += disSouth
                    rot = Direction.SOUTH
                    repeat(disSouth) { list += rot }
                }
                disWest != 0 -> {
                    posX -= disWest
                    rot = Direction.WEST
                    repeat(disWest) { list += rot }
                }
                disEast != 0 -> {
                    posX += disEast
                    rot = Direction.EAST
                    repeat(disEast) { list += rot }
                }
                else -> break
            }
        }

        return list
    }

    private fun generateRuleSet(traceList: List<Direction>, from: Int, to: Int): String? {
        var rule = ""

        var dir = if (from == 0) Direction.NORTH else traceList[from - 1]
        var size = -1

        for (index in from until to) {
            val d = traceList[index]

            if (d != dir || size == -1) {
                if (size > 0) rule += ",$size"
                rule += "," + dir.intersects(d)
                dir = d
                size = 0
            }

            size += 1
        }

        rule += ",$size"

        if ('?' in rule) return null
        if (rule.length > 21) return null
        return rule.substring(1)
    }

    private fun canApplyRuleset(ruleset: String, traceList: List<Direction>, from: Int): Boolean {
        var pos = from
        var offset = 0

        while (offset < ruleset.length) {
            val a = ruleset.indexOf(",", offset)
            var b = ruleset.indexOf(",", a + 1)
            if (b == -1) b = ruleset.length

            val ruleDir = ruleset.substring(offset, a)
            val ruleSize = ruleset.substring(a + 1, b).toInt()

            if (ruleSize > traceList.size - pos) return false

            val traceDir = (if (pos == 0) Direction.NORTH else traceList[pos - 1]).intersects(traceList[pos])
            if (traceDir != ruleDir) return false

            val hasToRepeat = traceList[pos]
            repeat(ruleSize) {
                if (traceList[pos + it] != hasToRepeat)
                    return false
            }

            pos += ruleSize
            offset = b + 1
        }

        return true
    }
}