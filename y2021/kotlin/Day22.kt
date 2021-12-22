package y2021

import general.Day
import java.lang.Integer.max
import java.lang.Integer.min

object Day22 : Day() {
    override val name = "Reactor Reboot"

    override fun a1() {
        val reactor = Array(101) { Array(101) { BooleanArray(101) { false } } }

        val input = INPUT.readLines()
        for (line in input) {

            val toState = line.startsWith("on")
            val rawXYZ = line.substringAfter(" ").split(",")
            val (fromX, toX) = rawXYZ[0].substring(2).split("..").map { it.toInt() }
            val (fromY, toY) = rawXYZ[1].substring(2).split("..").map { it.toInt() }
            val (fromZ, toZ) = rawXYZ[2].substring(2).split("..").map { it.toInt() }

            for (x in max(-50, fromX)..min(50, toX))
            for (y in max(-50, fromY)..min(50, toY))
            for (z in max(-50, fromZ)..min(50, toZ))
                reactor[x + 50][y + 50][z + 50] = toState
        }

        println(reactor.sumOf { it.sumOf { it.count { it } } })
    }

    class Cube(val state: Boolean, val rangeX: IntRange, val rangeY: IntRange, val rangeZ: IntRange) {

        fun volume() =
            (rangeX.last - rangeX.first + 1L) *
            (rangeY.last - rangeY.first + 1L) *
            (rangeZ.last - rangeZ.first + 1L)

        /**
         * @return the cube that is inside both this cube and another cube
         */
        fun cross(other: Cube): Cube? {

            val crossX = crossAxis(rangeX, other.rangeX) ?: return null
            val crossY = crossAxis(rangeY, other.rangeY) ?: return null
            val crossZ = crossAxis(rangeZ, other.rangeZ) ?: return null

            return Cube(false, crossX, crossY, crossZ)
        }

        /**
         * Adds all cubes into a cubes-list that will be created
         * if you subtract another cube from this cube
         */
        fun cut(other: Cube, cubes: ArrayList<Cube>) {

            // make things go fast!!
            // (I really mean it, without this check it will take forever)
            if (cross(other) == null) {
                cubes += this
                return
            }

            for (rX in listOf(
                Int.MIN_VALUE until other.rangeX.first,
                other.rangeX,
                (other.rangeX.last + 1)..Int.MAX_VALUE
            ))
            for (rY in listOf(
                Int.MIN_VALUE until other.rangeY.first,
                other.rangeY,
                (other.rangeY.last + 1)..Int.MAX_VALUE
            ))
            for (rZ in listOf(
                Int.MIN_VALUE until other.rangeZ.first,
                other.rangeZ,
                (other.rangeZ.last + 1)..Int.MAX_VALUE
            ))
            if (!(rX == other.rangeX && rY == other.rangeY && rZ == other.rangeZ)) {

                val cube = cross(Cube(false, rX, rY, rZ))
                if (cube != null)
                    cubes += cube
            }
        }

        companion object {
            /**
             * @return the range that is inside both ranges
             *         or null if otherwise
             */
            private fun crossAxis(range: IntRange, other: IntRange): IntRange? {

                if (other.first in range) {
                    return other.first..min(range.last, other.last)
                }
                else if (other.last in range) {
                    return max(range.first, other.first)..other.last
                }
                else if (other.first < range.first && other.last > range.last) {
                    return range
                }

                return null
            }
        }
    }

    override fun a2() {
        val steps = ArrayList<Cube>()

        // read cubes
        for (line in INPUT.readLines()) {

            val toState = line.startsWith("on")
            val rawXYZ = line.substringAfter(" ").split(",")
            val (fromX, toX) = rawXYZ[0].substring(2).split("..").map { it.toInt() }
            val (fromY, toY) = rawXYZ[1].substring(2).split("..").map { it.toInt() }
            val (fromZ, toZ) = rawXYZ[2].substring(2).split("..").map { it.toInt() }

            steps += Cube(toState, fromX..toX, fromY..toY, fromZ..toZ)
        }

        // handle steps
        val currentCubesOn = ArrayList<Cube>()

        for (cube in steps) {
            if (cube.state) add(currentCubesOn, cube)
            else remove(currentCubesOn, cube)
        }

        // output
        println(currentCubesOn.sumOf { it.volume() })
    }

    /**
     * add origin cube to cubesOn-list
     * and if needed, before adding, cut origin cube into small
     * pieces if specific parts are already inside the cubesOn-list
     */
    private fun add(cubesOn: ArrayList<Cube>, origin: Cube) {
        val cubes = ArrayList<Cube>()
        val tempAdd = ArrayList<Cube>()
        cubes += origin

        for (other in cubesOn) {
            for (cube in cubes)
                cube.cut(other, tempAdd)

            cubes.clear()
            cubes += tempAdd
            tempAdd.clear()
        }

        cubesOn += cubes
    }

    /**
     * cut every cube into small pieces and remove
     * the piece that is equal to the other-cube
     */
    private fun remove(cubes: ArrayList<Cube>, other: Cube) {
        val tempAdd = ArrayList<Cube>()

        for (cube in cubes)
            cube.cut(other, tempAdd)

        cubes.clear()
        cubes += tempAdd
        tempAdd.clear()
    }
}
