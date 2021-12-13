package y2019

import general.Day

object Day19 : Day() {
    override val name = "Tractor Beam"

    override fun a1() {
        val data = INPUT.readText().split(",").map { it.toLong() }
        var affected = 0

        for (y in 0..49)
            for (x in 0..49)
                if (iCP(data, x, y))
                    affected += 1

        println(affected)
    }

    override fun a2() {
        val data = INPUT.readText().split(",").map { it.toLong() }

        val (fxy, txy) = findSlopes(data)
//        dx = y / txy - x = 100
//        dy = fxy * x - y = 100

        var x = ((- 100 / txy - 100) / (1 - fxy / txy)).toInt()
        var y = (fxy * x - 100).toInt() - 50

        var offsetFromX = x
        var offsetToX = x

        while (true) {

            y += 1

            x = offsetFromX
            while (!iCP(data, x, y)) x += 1
            val fromX = x
            offsetFromX = fromX - 1

            x = offsetToX.coerceAtLeast(x)
            while (iCP(data, x, y)) x += 1
            val toX = x
            offsetToX = toX - 1

            if (toX - fromX < 100) continue

            xx@for (xx in fromX until toX - 99) {

                for (yy in y until y + 100)
                    if (!iCP(data, xx, yy))
                        continue@xx

                println("$xx / $y")
                println(xx * 10000 + y)
                return
            }
        }
    }

    private fun findSlopes(data: List<Long>): Pair<Double, Double> {
        val y = 1000
        var x = 0

        while (!iCP(data, x, y)) x += 1
        val fromX = x
        while (iCP(data, x, y)) x += 1
        val toX = x

        val fxy = y.toDouble() / fromX
        val txy = y.toDouble() / toX

        return Pair(fxy, txy)
    }

    private fun iCP(data: List<Long>, x: Int, y: Int): Boolean {
        var index = 0
        var output = false

        Day11.intCodeProgram(
            data = data,
            input = {
                if (index++ == 0) x.toLong()
                else y.toLong()
            },
            output = {
                output = it == 1L
            }
        )

        return output
    }
}