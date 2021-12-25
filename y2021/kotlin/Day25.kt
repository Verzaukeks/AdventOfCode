package y2021

import general.Day

object Day25 : Day() {
    override val name = "Sea Cucumber"

    override fun a1() {
        val input = INPUT.readLines()

        var from = Array(input.size) { y -> CharArray(input[0].length) { x ->  input[y][x] } }
        var to = Array(input.size) { CharArray(input[0].length) { '.' } }

        var steps = 0
        var changed = true
        while (changed) {

            steps += 1
            changed = false

            // >>>>>>>>>>>>>>>>>>>>>>>>>
            for (y in from.indices)
                for (x in from[y].indices)
                    when (from[y][x]) {
                        '>' -> {
                            val toX = (x + 1) % from[y].size

                            if (from[y][toX] == '.') {
                                to[y][toX] = '>'
                                changed = true
                            }
                            else to[y][x] = '>'
                        }
                        'v' -> to[y][x] = 'v'
                    }

            // swap & clear
            from = to.also { to = from }
            for (y in to.indices) for (x in to[y].indices) to[y][x] = '.'

            // vvvvvvvvvvvvvvvvvvvvvvvvv
            for (y in from.indices)
                for (x in from[y].indices)
                    when (from[y][x]) {
                        'v' -> {
                            val toY = (y + 1) % from.size

                            if (from[toY][x] == '.') {
                                to[toY][x] = 'v'
                                changed = true
                            }
                            else to[y][x] = 'v'
                        }
                        '>' -> to[y][x] = '>'
                    }

            // swap & clear
            from = to.also { to = from }
            for (y in to.indices) for (x in to[y].indices) to[y][x] = '.'
        }


//        for (line in from)
//            println(line.concatToString())

        println(steps)
    }

    override fun a2() {}
}