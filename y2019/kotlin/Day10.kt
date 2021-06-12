package y2019

import general.Day
import kotlin.math.absoluteValue
import kotlin.math.atan
import kotlin.math.sqrt

object Day10 : Day() {
    override val name = "Monitoring Station"

    data class Vector(var x: Int, var y: Int) {
        private fun gcd(): Int {
            if (x == 0) return y.absoluteValue
            if (y == 0) return x.absoluteValue

            var h: Int
            var a = x
            var b = y
            do {
                h = a % b
                a = b
                b = h
            } while (b != 0)

            return a.absoluteValue
        }
        fun normalize(): Vector {
            val gcd = gcd()
            x /= gcd
            y /= gcd
            return this
        }
        fun angle(): Double {
            if (x == 0) {
                if (-y > 0) return 0.0
                if (-y < 0) return 180.0 }
            if (y == 0) {
                if (x > 0) return 90.0
                if (x < 0) return 270.0 }

            if (x > 0) {
                val slope = -y.toDouble() / x.toDouble()
                val angle = Math.toDegrees(atan(slope))
                return -angle + 90
            } else {
                val slope = -y.toDouble() / -x.toDouble()
                val angle = Math.toDegrees(atan(slope))
                return (angle + 90 + 180) % 360
            }
        }
        fun distance() = sqrt(1.0 * x*x + y*y)
    }

    override fun a1() {
        val data = INPUT.readLines().map { it.toCharArray() }

        var finalVisible = 0
        var finalX = 0
        var finalY = 0

        for (y in data.indices) for (x in data[y].indices) {
            if (data[y][x] == '.') continue

            val visibles = ArrayList<Vector>()

            for (cy in data.indices) for (cx in data[cy].indices) {
                if (data[cy][cx] == '.' || (cy == y && cx == x)) continue

                val slope = Vector(cx - x, cy - y).normalize()
                if (slope !in visibles)
                    visibles += slope
            }

            if (visibles.size > finalVisible) {
                finalVisible = visibles.size
                finalX = x
                finalY = y
            }
        }

        println("($finalX,$finalY): $finalVisible")
    }

    override fun a2() {
        val data = INPUT.readLines().map { it.toCharArray() }
        val laserX = 20
        val laserY = 18
        data[laserY][laserX] = '.'

        var laserAngle = 0.0
        var destroyCount = 0

        val asteroids = ArrayList<Triple<Int,Int,Vector>>()
        for (cy in data.indices) for (cx in data[cy].indices) {
            if (data[cy][cx] == '.') continue
            asteroids += Triple(cx, cy, Vector(cx - laserX, cy - laserY))
        }
        asteroids.sortBy { it.third.angle() }

        while (true) {
            val asteroid = asteroids
                .filter { it.third.angle() == laserAngle }
                .minByOrNull { it.third.distance() }!!

            asteroids -= asteroid
            destroyCount += 1

            if (destroyCount == 200) {
                println(asteroid.first * 100 + asteroid.second)
                return
            }

            laserAngle = asteroids
                .filter { it.third.angle() > laserAngle }
                .minOfOrNull { it.third.angle() }
                ?: asteroids.minOf { it.third.angle() }
        }
    }
}