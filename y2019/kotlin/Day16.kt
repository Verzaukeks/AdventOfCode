package y2019

import general.Day
import general.times
import kotlin.math.absoluteValue

object Day16 : Day() {
    override val name = "Flawed Frequency Transmission"

    override fun a1() {
        val pattern = intArrayOf(0, 1, 0, -1)
        var input = INPUT.readText().toCharArray().map { it - '0' }.toIntArray()
        var output = IntArray(input.size) {0}

        repeat(100) {

            for (outputIndex in output.indices) {
                var sum = 0

                for (inputIndex in input.indices) {
                    val patternIndex = (inputIndex + 1) / (outputIndex + 1)

                    val indexValue = input[inputIndex]
                    val patternValue = pattern[patternIndex % pattern.size]

                    sum += indexValue * patternValue
                }

                output[outputIndex] = sum.absoluteValue % 10
            }

            output = input.also { input = output }
        }

        println(input.joinToString("").substring(0, 8))
    }

    override fun a2() {
        val data = (INPUT.readText() * 10000).let { it.substring(it.substring(0, 7).toInt())  }
        val array = data.toCharArray().map { it - '0' }.toIntArray()

        // shenanigans ;)
        repeat(100) {
            var sum = 0
            for (index in array.indices) {
                sum += array[array.lastIndex - index]
                array[array.lastIndex - index] = sum % 10
            }
        }

        println(array.joinToString("").substring(0, 8))
    }
}