package y2019

import general.Day

object Day08 : Day() {
    override val name = "Space Image Format"

    override fun a1() {
        val layerSize = 25 * 6
        val imageData = INPUT.readText()

        var finalZeros = layerSize + 1
        var finalOnes = 0
        var finalTwos = 0

        for (layer in 0 until imageData.length / layerSize) {
            var zeros = 0
            var ones = 0
            var twos = 0

            for (pixelIndex in layerSize * layer until layerSize * (layer + 1)) {
                val pixel = imageData[pixelIndex] - '0'

                when (pixel) {
                    0 -> zeros++
                    1 -> ones++
                    2 -> twos++
                }
            }

            if (zeros < finalZeros) {
                finalZeros = zeros
                finalOnes = ones
                finalTwos = twos
            }
        }

        println(finalOnes * finalTwos)
    }

    override fun a2() {
        val layerSize = 25 * 6
        val layers = INPUT.readText()
            .chunked(layerSize)
            .map { it.toMutableList() }
            .asReversed()

        for (l in 1 until layers.size)
            for (p in layers[0].indices) {

                val pixel = layers[l][p]
                if (pixel != '2')
                    layers[0][p] = pixel
            }

        println(
            layers[0].joinToString("")
                .chunked(25).joinToString("\n")
                .replace("0", "  ")
                .replace("1", "##"))
    }
}