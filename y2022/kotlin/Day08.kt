package y2022

import general.Day

object Day08 : Day() {
    override val name = "Treetop Tree House"

    override fun a1() {
        val map = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        var sum = 0
        for (y in map.indices) {
            for (x in map[y].indices) {
                var visible = false
                val num = map[y][x]

                visible = visible || (     0  until y          ).none { map[it][x] >= num }
                visible = visible || ((y + 1) until map.size   ).none { map[it][x] >= num }
                visible = visible || (     0  until x          ).none { map[y][it] >= num }
                visible = visible || ((x + 1) until map[y].size).none { map[y][it] >= num }

                if (visible) sum++
            }
        }

        println(sum)
    }

    override fun a2() {
        val map = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        var maxS = 0
        for (y in 1..(map.size - 2)) {
            for (x in 1..(map[y].size - 2)) {
                var score = 1
                val num = map[y][x]

                score *= y - ((y - 1) downTo 0         ).first { map[it][x] >= num || it == 0 }
                score *=     ((y + 1) until map.size   ).first { map[it][x] >= num || it == map.size - 1 }    - y
                score *= x - ((x - 1) downTo 0         ).first { map[y][it] >= num || it == 0 }
                score *=     ((x + 1) until map[y].size).first { map[y][it] >= num || it == map[y].size - 1 } - x

                if (score > maxS) maxS = score
            }
        }

        println(maxS)
    }
}