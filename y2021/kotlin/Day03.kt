package y2021

import general.Day
import kotlin.math.pow

object Day03 : Day() {
    override val name = "Binary Diagnostic"

    override fun a1() {
        val input = INPUT.readLines()

        val size = input.size
        val length = input[0].length
        val count = IntArray(length)

        for (line in input)
            line.forEachIndexed { index, char ->
                if (char == '1')
                    count[index] += 1
            }

        val gammaRaw = count.joinToString("") { if (it >= size / 2) "1" else "0" }
        val gamma = gammaRaw.toInt(2)
        val epsilon = 2.0.pow(length).toInt() - gamma - 1

        println(gamma * epsilon)
    }

    override fun a2() {
        val input = INPUT.readLines()
        val length = input[0].length

        var oxygen = ""
        var co2 = ""

        for (bit in 0 until length) {
            var countOxygen = 0
            var sizeOxygen = 0
            var countCO2 = 0
            var sizeCO2 = 0

            for (number in input) {
                if (number.startsWith(oxygen)) {
                    if (number[bit] == '1') countOxygen += 1
                    sizeOxygen += 1
                }
                if (number.startsWith(co2)) {
                    if (number[bit] == '1') countCO2 += 1
                    sizeCO2 += 1
                }
            }

            if (sizeOxygen == 1) oxygen = input.first { it.startsWith(oxygen) }
            else if (sizeOxygen != 0) oxygen += if (countOxygen >= sizeOxygen / 2.0) "1" else "0"

            if (sizeCO2 == 1) co2 = input.first { it.startsWith(co2) }
            else if (sizeCO2 != 0) co2 += if (countCO2 >= sizeCO2 / 2.0) "0" else "1"
        }

        println(oxygen.toInt(2) * co2.toInt(2))
    }
}