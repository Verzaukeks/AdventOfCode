package y2021

import general.Day

object Day13 : Day() {
    override val name = "Transparent Origami"

    data class Dot(var x: Int, var y: Int)

    override fun a1() = println(run(true).size)
    override fun a2() = printDots(run(false))

    private fun run(once: Boolean): List<Dot> {
        var dots = ArrayList<Dot>()
        var folded = ArrayList<Dot>()
        var dotting = true

        for (line in INPUT.readLines()) {
            if (dotting) {

                if (line.isEmpty()) {
                    dotting = false
                    continue
                }

                val (x, y) = line.split(",")
                dots += Dot(x.toInt(), y.toInt())

                continue
            }
            else if ('x' in line) {

                val xSplit = line.substringAfter("=").toInt()

                for (dot in dots) {
                    if (dot.x > xSplit) dot.x = xSplit - (dot.x - xSplit)
                    if (dot !in folded) folded += dot
                }
            }
            else if ('y' in line) {

                val ySplit = line.substringAfter("=").toInt()

                for (dot in dots) {
                    if (dot.y > ySplit) dot.y = ySplit - (dot.y - ySplit)
                    if (dot !in folded) folded += dot
                }
            }

            dots = folded.also { folded = dots }
            folded.clear()

            if (once) break
        }

        return dots
    }

    private fun printDots(dots: List<Dot>) {
        val maxY = dots.maxOf { it.y } + 1
        val maxX = dots.maxOf { it.x } + 1

        val map = Array(maxY) { CharArray(maxX) { ' ' } }
        for (dot in dots) map[dot.y][dot.x] = '#'

        for (line in map) {
            for (char in line)
                print("$char$char")
            println()
        }
    }
}