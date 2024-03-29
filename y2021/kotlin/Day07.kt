package y2021

import general.Day
import kotlin.math.abs

object Day07 : Day() {
    override val name = "The Treachery of Whales"

    override fun a1() {
        val input = INPUT.readText()
            .split(",")
            .map { it.toInt() }
            .sorted()

        val median = input[input.size / 2]
        val fuel = input.sumOf { abs(it - median) }
        println(fuel)
    }

    override fun a2() {
        val input = INPUT.readText().split(",").map { it.toInt() }
        var fuel = Integer.MAX_VALUE
        var prev = Integer.MAX_VALUE

        var pos = input.sum() / input.size // approx guess, near correct position
        var direction = 1

        while (true) {
            val distance = input.sumOf {
                val n = abs(it - pos)
                // n*(n+1)/2 = sum(1+...+n)
                n * (n + 1) / 2
            }

            if (distance < fuel) fuel = distance
            if (distance > prev) {
                if (direction == -1) break

                direction = -1 // search into other direction
                pos -= 2 // skip previous calculation of fuel (=prev)
                continue // prevent override: prev = distance
            }

            pos += direction
            prev = distance
        }

        println(fuel)
    }
}