package y2019

import general.Day
import general.lcm
import kotlin.math.absoluteValue

object Day12 : Day() {
    override val name = "The N-Body Problem"

    data class Moon(
        var posX: Int, var posY: Int, var posZ: Int,
        var velX: Int, var velY: Int, var velZ: Int,
    ) {
        val originalPosX = posX
        val originalPosY = posY
        val originalPosZ = posZ
    }

    override fun a1() {
        val moons = INPUT.readText().replace("[^,\\-0-9\n]".toRegex(), "").split("\n")
            .map { val (x, y, z) = it.split(",").map { it.toInt() } ; Moon(x, y, z, 0, 0, 0) }

        // time step
        repeat(1000) {

            // apply gravity
            for (moonA in moons) for (moonB in moons) if (moonA != moonB) {
                if (moonA.posX < moonB.posX) moonA.velX += 1
                if (moonA.posX > moonB.posX) moonA.velX -= 1
                if (moonA.posY < moonB.posY) moonA.velY += 1
                if (moonA.posY > moonB.posY) moonA.velY -= 1
                if (moonA.posZ < moonB.posZ) moonA.velZ += 1
                if (moonA.posZ > moonB.posZ) moonA.velZ -= 1
            }

            // update position
            for (moon in moons) {
                moon.posX += moon.velX
                moon.posY += moon.velY
                moon.posZ += moon.velZ
            }
        }

        // calculate total energy
        var energy = 0
        for (moon in moons) {
            val potential = moon.posX.absoluteValue + moon.posY.absoluteValue + moon.posZ.absoluteValue
            val kinetic = moon.velX.absoluteValue + moon.velY.absoluteValue + moon.velZ.absoluteValue
            energy += potential * kinetic
        }

        println(energy)
    }

    override fun a2() {
        /*
        you can calculate each axis on their own,
        because they are independent from each other
         */

        val moons = INPUT.readText().replace("[^,\\-0-9\n]".toRegex(), "").split("\n")
            .map { val (x, y, z) = it.split(",").map { it.toInt() } ; Moon(x, y, z, 0, 0, 0) }

        var intervalX = 0L
        var intervalY = 0L
        var intervalZ = 0L

        loop@while (true) {
            for (moonA in moons) for (moonB in moons) if (moonA != moonB) {
                if (moonA.posX < moonB.posX) moonA.velX += 1
                if (moonA.posX > moonB.posX) moonA.velX -= 1
            }
            for (moon in moons) moon.posX += moon.velX
            intervalX += 1L
            for (moon in moons) if (moon.posX != moon.originalPosX || moon.velX != 0) continue@loop
            break
        }
        loop@while (true) {
            for (moonA in moons) for (moonB in moons) if (moonA != moonB) {
                if (moonA.posY < moonB.posY) moonA.velY += 1
                if (moonA.posY > moonB.posY) moonA.velY -= 1
            }
            for (moon in moons) moon.posY += moon.velY
            intervalY += 1L
            for (moon in moons) if (moon.posY != moon.originalPosY || moon.velY != 0) continue@loop
            break
        }
        loop@while (true) {
            for (moonA in moons) for (moonB in moons) if (moonA != moonB) {
                if (moonA.posZ < moonB.posZ) moonA.velZ += 1
                if (moonA.posZ > moonB.posZ) moonA.velZ -= 1
            }
            for (moon in moons) moon.posZ += moon.velZ
            intervalZ += 1L
            for (moon in moons) if (moon.posZ != moon.originalPosZ || moon.velZ != 0) continue@loop
            break
        }

        val interval = lcm(intervalX, lcm(intervalY, intervalZ))
        println(interval)
    }
}