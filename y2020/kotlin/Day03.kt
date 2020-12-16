package y2020

object Day03 : Day() {

    override val day = 3
    override val name = "Toboggan Trajectory"

    override fun a1() {
        println(slope(3, 1))
    }

    override fun a2() {
        val a = slope(1, 1)
        val b = slope(3, 1)
        val c = slope(5, 1)
        val d = slope(7, 1)
        val e = slope(1, 2)
        println(a * b * c * d * e)
    }

    fun slope(right: Int, down: Int): Int {
        val input = INPUT.readLines()
        var trees = 0

        var x = 0
        for (y in input.indices step down) {
            if (input[y][x] == '#') trees++
            x = (x + right) % input[y].length
        }

        return trees
    }
}