package y2021

import general.Day
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt

object Day17 : Day() {
    override val name = "Trick Shot"

    override fun a1() {
        val input = INPUT.readText()
        val (targetToY, targetFromY) = input.substringAfter("y=").split("..").map { it.toInt() }
        var highest = 0

        for (velocity in 0..200) {

            val minSteps = ceil(calcStep(velocity, targetFromY)).toInt()
            val maxSteps = floor(calcStep(velocity, targetToY)).toInt()
            if (minSteps <= 0 || maxSteps <= 0) continue
            if (minSteps > maxSteps) continue

            // y = steps * velocity - 0.5 * steps * (steps - 1)
            // => y = -0.5 * steps^2 + (velocity + 0.5) * steps
            // => y' = -steps + velocity + 0.5
            // => 0 = y' => steps = velocity + 0.5
            // => over the integers:
            //      y(velocity) = highest
            //      y(velocity + 1) = highest
            //    turning point where vy (velocity during step) changes from 1 to 0,
            //    so in other words: 'y += vy' => 'y += 0'

            highest = (0.5 * velocity * (velocity + 1)).toInt()
        }

        println(highest)
    }

    private fun calcStep(v: Int, y: Int): Double {
        // y = steps * velocity - 0.5 * steps * (steps - 1)
        // => steps = velocity + 0.5 + sqrt(velocity^2 + velocity + 0.25 - 2y)
        val temp = v * v + v + 0.25 - 2 * y
        if (temp < 0) return -1.0
        return v + 0.5 + sqrt(temp)
    }

    override fun a2() {
        val input = INPUT.readText()
        val (targetFromX, targetToX) = input.substringAfter("x=").substringBefore(",").split("..").map { it.toInt() }
        val (targetToY, targetFromY) = input.substringAfter("y=").split("..").map { it.toInt() }
        val distinct = ArrayList<Pair<Int, Int>>()

        val targetRangeX = targetFromX..targetToX

        for (yVelocity in targetToY..200) {

            val minSteps = ceil(calcStep(yVelocity, targetFromY)).toInt()
            val maxSteps = floor(calcStep(yVelocity, targetToY)).toInt()
            if (minSteps <= 0 || maxSteps <= 0) continue
            if (minSteps > maxSteps) continue


            for (xVelocity in 0..targetToX) {

                val min = min(minSteps, xVelocity)
                val max = min(maxSteps, xVelocity)
                val minX = (min * (xVelocity - 0.5 * min + 0.5)).toInt()
                val maxX = (max * (xVelocity - 0.5 * max + 0.5)).toInt()

                if (minX in targetRangeX || maxX in targetRangeX) {

                    val p = Pair(xVelocity, yVelocity)
                    if (p !in distinct)
                        distinct += p
                }
            }
        }

        println(distinct.size)
    }
}
