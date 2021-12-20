package y2021

import general.Day
import kotlin.math.abs

object Day19 : Day() {
    override val name = "Beacon Scanner"

    private const val ORIENTATION_DISTANCE = 1_000_000
    enum class Axis { Y_AXIS, Z_AXIS, DOUBLE_Z_AXIS }
    data class Position(var x: Int, var y: Int, var z: Int)

    class Scanner(val beacons: MutableList<Position>, addOrientationPoints: Boolean) {
        val pos = Position(0, 0, 0)

        init {
            if (addOrientationPoints) {
                beacons += Position(ORIENTATION_DISTANCE, ORIENTATION_DISTANCE, ORIENTATION_DISTANCE)
                beacons += Position(-ORIENTATION_DISTANCE, -ORIENTATION_DISTANCE, -ORIENTATION_DISTANCE)
            }
        }

        fun move(mx: Int, my: Int, mz: Int) {
            for (beacon in beacons) {
                beacon.x += mx
                beacon.y += my
                beacon.z += mz
            }
        }
    }

    private fun readScanners(addOrientationPoints: Boolean) = INPUT.readText()
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
                .toMutableList(), addOrientationPoints)
        }

    override fun a1() {
        println(calculate(false).first.size)
    }

    override fun a2() {
        val scanners = calculate(true).second
        var max = 0

        for (sc in scanners) {
            val ori1 = sc.beacons[sc.beacons.size - 1]
            val ori2 = sc.beacons[sc.beacons.size - 2]

            val dx = (ori2.x - ori1.x) / 2
            val dy = (ori2.y - ori1.y) / 2
            val dz = (ori2.z - ori1.z) / 2

            sc.pos.x = ori1.x + dx
            sc.pos.y = ori1.y + dy
            sc.pos.z = ori1.z + dz
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

    private fun calculate(addOrientationPoints: Boolean): Pair<ArrayList<Position>, ArrayList<Scanner>> {
        val scanners = readScanners(addOrientationPoints)

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

                val match = s1.beacons.count { it in s2.beacons }
                if (match >= 12)
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

            }
            Axis.Z_AXIS -> {

                for (beacon in s.beacons) {
                    val (x, y, z) = beacon

                    beacon.x = y
                    beacon.y = -x
//                    beacon.z = z
                }

            }
            Axis.DOUBLE_Z_AXIS -> {

                for (beacon in s.beacons) {
                    val (x, y, z) = beacon

                    beacon.x = -x
                    beacon.y = -y
//                    beacon.z = z
                }

            }
        }
    }
}