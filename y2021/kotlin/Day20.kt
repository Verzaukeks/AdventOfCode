package y2021

import general.Day

object Day20 : Day() {
    override val name = "Trench Map"

    enum class EmptyType { ALWAYS_DARK, ALWAYS_LIGHT, SWAP }

    class Image(val size: Int) {
        private val data = Array(size) { BooleanArray(size) { false } }

        fun set(x: Int, y: Int, lit: Boolean) { data[y][x] = lit }
        fun get(x: Int, y: Int) = data[y][x]

        fun count(predicate: (Int, Int, Boolean) -> Boolean): Int {
            var sum = 0
            for (y in 0 until size)
                for (x in 0 until size)
                    if (predicate(x, y, data[y][x]))
                        sum += 1
            return sum
        }

        fun countLit() = count { _, _, lit -> lit }

        fun print() {
            for (line in data) {
                for (lit in line)
                    print(if (lit) "# " else ". ")
                println()
            }
        }
    }

    override fun a1() = a(2)
    override fun a2() = a(50)

    private fun a(rounds: Int) {
        val input = INPUT.readLines()

        val algo = input[0].toCharArray()
        var img = Image(input.size - 2)
        var to: Image

        // magic ;)
        val infiniteType = when (algo.first()) {
            '.' -> EmptyType.ALWAYS_DARK
            '#' -> when (algo.last()) {
                '.' -> EmptyType.SWAP
                '#' -> EmptyType.ALWAYS_LIGHT
                else -> throw Error()
            }
            else -> throw Error()
        }

        // read first image
        for (y in 2 until input.size)
            for (x in 0 until img.size)
                img.set(x, y - 2, input[y][x] == '#')

        repeat(rounds) {
            // only pixels around image can change "unexpectedly"
            to = Image(img.size + 2)

            // for every pixel to draw
            for (y in 0 until to.size)
                for (x in 0 until to.size) {

                    var number = 0

                    // for every pixel around
                    for (dy in -1..1)
                        for (dx in -1..1) {

                            // input image has an offset of 1|1 to the output image
                            val iy = y + dy - 1
                            val ix = x + dx - 1
                            val range = 0 until img.size

                            // is pixel lit (in image or around?)
                            val lit = when {
                                ix in range && iy in range -> img.get(ix, iy)
                                else -> when (infiniteType) {
                                    EmptyType.ALWAYS_DARK -> false
                                    EmptyType.ALWAYS_LIGHT -> true
                                    EmptyType.SWAP -> it and 1 == 1
                                }
                            }

                            // append bit to number
                            number = (number shl 1) + if (lit) 1 else 0
                        }

                    // get enhanced pixel
                    val lit = (algo[number] == '#')
                    to.set(x, y, lit)
                }

            // swap
            img = to.also { to = img }
        }

        // count
        println(img.countLit())
    }
}