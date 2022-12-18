package y2022

import general.Day
import java.util.*
import kotlin.math.max
import kotlin.math.min

object Day18 : Day() {
    override val name = "Boiling Boulders"

    private val ADJ = listOf(
        Cube(1, 0, 0), Cube(-1, 0, 0),
        Cube(0, 1, 0), Cube(0, -1, 0),
        Cube(0, 0, 1), Cube(0, 0, -1),
    )

    data class Cube(var x: Int, var y: Int, var z: Int) {
        operator fun plus(o: Cube) = Cube(x + o.x, y + o.y, z + o.z)
        operator fun plusAssign(o: Cube) { x += o.x ; y += o.y ; z += o.z }
        fun minimize(o: Cube) { x = min(x, o.x) ; y = min(y, o.y) ; z = min(z, o.z) }
        fun maximize(o: Cube) { x = max(x, o.x) ; y = max(y, o.y) ; z = max(z, o.z) }
    }

    override fun a1() {
        val cubes = INPUT.readLines()
            .map {
                it.split(",")
                    .map { it.toInt() }
            }.map {
                Cube(it[0], it[1], it[2])
            }.toSet()

        var surface = 0
        for (cube in cubes)
            for (adj in ADJ)
                if (cube + adj !in cubes)
                    surface++

        println(surface)
    }

    override fun a2() {
        val cubes = INPUT.readLines()
            .map {
                it.split(",")
                    .map { it.toInt() }
            }.map {
                Cube(it[0], it[1], it[2])
            }

        val minXYZ = Cube(0, 0, 0)
        val maxXYZ = Cube(0, 0, 0)
        for (cube in cubes) {
            minXYZ.minimize(cube)
            maxXYZ.maximize(cube)
        }
        minXYZ += Cube(-1, -1, -1)
        maxXYZ += Cube(1, 1, 1)

        val rx = minXYZ.x..maxXYZ.x
        val ry = minXYZ.y..maxXYZ.y
        val rz = minXYZ.z..maxXYZ.z

        val air = HashSet<Cube>()
        val q = LinkedList<Cube>()

        air += minXYZ
        q += minXYZ

        while (q.isNotEmpty()) {
            val curr = q.pop()

            for (adj in ADJ) {
                val new = curr + adj
                if (new.x !in rx) continue
                if (new.y !in ry) continue
                if (new.z !in rz) continue
                if (new in air) continue
                if (new in cubes) continue

                air += new
                q += new
            }
        }

        var surface = 0
        for (cube in cubes)
            for (adj in ADJ)
                if (cube + adj in air)
                    surface++

        println(surface)
    }
}