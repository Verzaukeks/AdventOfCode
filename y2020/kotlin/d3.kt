package y2020

import y2020.inputs.Files

object d3 {
    fun a1() {
        println(slope(3, 1))
    }

    fun a2() {
        val a = slope(1, 1)
        val b = slope(3, 1)
        val c = slope(5, 1)
        val d = slope(7, 1)
        val e = slope(1, 2)
        println(a * b * c * d * e)
    }

    fun slope(right: Int, down: Int): Int {
        var trees = 0
        val input = Files[3].readLines()

        var x = 0
        for (y in input.indices step down) {
            if (input[y][x] == '#') trees++
            x = (x + right) % input[y].length
        }

        return trees
    }
}