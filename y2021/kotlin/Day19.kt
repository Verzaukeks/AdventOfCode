package y2021

import general.Day
import kotlin.math.abs

object Day19 : Day() {
    override val name = "Beacon Scanner"

    private const val ORIENTATION_DISTANCE = 1_000_000
    enum class Axis { Y_AXIS, Z_AXIS, DOUBLE_Z_AXIS }
    data class Position(var x: Int, var y: Int, var z: Int)

    class Scanner(val beacons: MutableList<Position>) {
        val pos = Position(0, 0, 0)
        val ori1 = Position(ORIENTATION_DISTANCE, ORIENTATION_DISTANCE, ORIENTATION_DISTANCE)
        val ori2 = Position(-ORIENTATION_DISTANCE, -ORIENTATION_DISTANCE, -ORIENTATION_DISTANCE)

        fun move(mx: Int, my: Int, mz: Int) {
            for (beacon in beacons) {
                beacon.x += mx
                beacon.y += my
                beacon.z += mz
            }

            ori1.x += mx
            ori1.y += my
            ori1.z += mz

            ori2.x += mx
            ori2.y += my
            ori2.z += mz
        }
    }

    private fun readScanners() = INPUT.readText()
        .replace("--- scanner [0-9]+ ---\r?\n".toRegex(), "")
        .split("\r?\n\r?\n".toRegex())
        .map {
            Scanner(it
                .split("\r?\n".toRegex())
                .map {
                    it.split(",")
                        .map { it.toInt() }
                        .let {
                            Position(it[0], it[1], it[2])
                        }
                }
                .toMutableList())
        }

    override fun a1() {
        println(calculate().first.size)
    }

    override fun a2() {
        val scanners = calculate().second
        var max = 0

        for (sc in scanners) {
            val dx = (sc.ori2.x - sc.ori1.x) / 2
            val dy = (sc.ori2.y - sc.ori1.y) / 2
            val dz = (sc.ori2.z - sc.ori1.z) / 2

            sc.pos.x = sc.ori1.x + dx
            sc.pos.y = sc.ori1.y + dy
            sc.pos.z = sc.ori1.z + dz
        }

        for (s1 in scanners)
            for (s2 in scanners)
                if (s1 != s2) {

                    val dx = abs(s1.pos.x - s2.pos.x)
                    val dy = abs(s1.pos.y - s2.pos.y)
                    val dz = abs(s1.pos.z - s2.pos.z)
                    val distance = dx + dy + dz

                    if (distance > max)
                        max = distance
                }

        println(max)
    }

    private fun calculate(): Pair<ArrayList<Position>, ArrayList<Scanner>> {
        val scanners = readScanners()

        val sorted = ArrayList<Scanner>()
        val unsorted = ArrayList<Scanner>()

        unsorted += scanners
        sorted += unsorted.removeFirst()

        while (unsorted.isNotEmpty()) {
            val toSort = unsorted.removeFirst()
            var couldSort = true

            run {

                // each side
                repeat(6) {

                    // each rotation
                    repeat(4) {
                        if (sorted.any { s -> check(s, toSort) }) return@run
                        rotateAxis(toSort, Axis.Z_AXIS)
                    }

                    when (it) {
                        3 -> rotateAxis(toSort, Axis.Z_AXIS)
                        4 -> rotateAxis(toSort, Axis.DOUBLE_Z_AXIS)
                        else -> rotateAxis(toSort, Axis.Y_AXIS)
                    }
                }

                couldSort = false
            }

            if (couldSort) sorted += toSort
            else unsorted += toSort
        }


        val beacons = ArrayList<Position>()

        for (s in sorted)
            for (b in s.beacons)
                if (b !in beacons)
                    beacons += b

        return Pair(beacons, sorted)
    }

    private fun check(s1: Scanner, s2: Scanner): Boolean {
        for (b1 in s1.beacons)
            for (b2 in s2.beacons) {

                val mx = b1.x - b2.x
                val my = b1.y - b2.y
                val mz = b1.z - b2.z
                s2.move(mx, my, mz)

                var match = 0
                for (b in s1.beacons)
                    if (b in s2.beacons && ++match >= 12)
                        return true
            }

        return false
    }

    private fun rotateAxis(s: Scanner, axis: Axis) {
        when (axis) {
            Axis.Y_AXIS -> {

                for (beacon in s.beacons) {
                    val (x, y, z) = beacon

                    beacon.x = z
//                    beacon.y = y
                    beacon.z = -x
                }

                val (x1, y1, z1) = s.ori1
                val (x2, y2, z2) = s.ori2
                s.ori1.x = z1
                s.ori1.z = -x1
                s.ori2.x = z2
                s.ori2.z = -x2

            }
            Axis.Z_AXIS -> {

                for (beacon in s.beacons) {
                    val (x, y, z) = beacon

                    beacon.x = y
                    beacon.y = -x
//                    beacon.z = z
                }

                val (x1, y1, z1) = s.ori1
                val (x2, y2, z2) = s.ori2
                s.ori1.x = y1
                s.ori1.y = -x1
                s.ori2.x = y2
                s.ori2.y = -x2

            }
            Axis.DOUBLE_Z_AXIS -> {

                for (beacon in s.beacons) {
                    val (x, y, z) = beacon

                    beacon.x = -x
                    beacon.y = -y
//                    beacon.z = z
                }

                s.ori1.x = -s.ori1.x
                s.ori1.y = -s.ori1.y
                s.ori2.x = -s.ori2.x
                s.ori2.y = -s.ori2.y

            }
        }
    }
}