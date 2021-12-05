package y2021

import general.Day
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min

object Day05 : Day() {
    override val name = "Hydrothermal Venture"

    data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
        fun draw(pixels: IntArray, width: Int) {
            val (fromX, toX) = Pair(min(x1, x2), max(x1, x2))
            val (fromY, toY) = Pair(min(y1, y2), max(y1, y2))

            if (x1 == x2) {
                for (y in fromY..toY)
                    pixels[y * width + x1] += 1
            }
            else if (y1 == y2) {
                for (x in fromX..toX)
                    pixels[y1 * width + x] += 1
            }
            else if ((y2 - y1) / (x2 - x1) > 0) {
                var x = fromX
                for (y in fromY..toY)
                    pixels[y * width + (x++)] += 1
            }
            else {
                var x = toX
                for (y in fromY..toY)
                    pixels[y * width + (x--)] += 1
            }
        }
    }

    private fun readLines() = INPUT
        .readLines()
        .map { it
            .replace("->", ",")
            .split(",")
            .map { it.trim().toInt() } }
        .map { Line(it[0], it[1], it[2], it[3]) }

    override fun a1() = a(readLines().filter { it.x1 == it.x2 || it.y1 == it.y2 })

    override fun a2() = a(readLines())

    private fun a(lines: List<Line>) {
        val width = lines.maxOf { max(it.x1, it.x2) } + 1
        val height = lines.maxOf { max(it.y1, it.y2) } + 1
        val pixels = IntArray(width * height)

        for (line in lines)
            line.draw(pixels, width)

        val count = pixels.count { it > 1 }
        println(count)


//        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
//        val data = (img.raster.dataBuffer as DataBufferInt).data
//
//        for (xy in pixels.indices)
//            data[xy] = when (pixels[xy]) {
//                0 -> 0
//                1 -> 0x777777
//                else -> 0xcccccc
//            }
//
//        ImageIO.write(img, "PNG", File("img.png"))
    }
}