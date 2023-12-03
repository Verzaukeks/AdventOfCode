package y2023

import general.Day

object Day03 : Day() {
    override val name = "Gear Ratios"

    override fun a1() {
        val map = INPUT.readLines()
        var sum = 0

        fun isSymbol(y: Int, x: Int): Boolean {
            if (y !in map.indices) return false
            if (x !in map[y].indices) return false
            return map[y][x] != '.' && !map[y][x].isDigit()
        }

        for (y in map.indices) {
            var num = 0
            var isEngine = false

            for (x in map[y].indices)
                if (map[y][x].isDigit()) {
                    if (num == 0) {
                        if (isSymbol(y-1, x-1)) isEngine = true
                        if (isSymbol(y  , x-1)) isEngine = true
                        if (isSymbol(y+1, x-1)) isEngine = true
                    }
                    num *= 10
                    num += map[y][x] - '0'
                    if (isSymbol(y-1, x)) isEngine = true
                    if (isSymbol(y+1, x)) isEngine = true
                } else if (num > 0) {
                    if (isSymbol(y-1, x)) isEngine = true
                    if (isSymbol(y  , x)) isEngine = true
                    if (isSymbol(y+1, x)) isEngine = true
                    if (isEngine) sum += num
                    num = 0
                    isEngine = false
                }

            if (isEngine) sum += num
        }

        println(sum)
    }

    override fun a2() {
        val map = INPUT.readLines()
        var sum = 0

        fun isDigit(y: Int, x: Int): Boolean {
            if (y !in map.indices) return false
            if (x !in map[y].indices) return false
            return map[y][x].isDigit()
        }

        fun getNum(y: Int, x: Int): Int {
            var ox = x
            while (isDigit(y, ox-1)) ox--
            var num = 0
            while (isDigit(y, ox)) {
                num *= 10
                num += map[y][ox] - '0'
                ox++
            }
            return num
        }

        for (y in map.indices)
            for (x in map.indices)
                if (map[y][x] == '*') {
                    var prod = 1
                    var nums = 0
                    if (isDigit(y, x-1)) { prod *= getNum(y, x-1); nums++ }
                    if (isDigit(y, x+1)) { prod *= getNum(y, x+1); nums++ }
                    if (isDigit(y-1, x)) { prod *= getNum(y-1, x); nums++ }
                    else {
                        if (isDigit(y-1, x-1)) { prod *= getNum(y-1, x-1); nums++ }
                        if (isDigit(y-1, x+1)) { prod *= getNum(y-1, x+1); nums++ }
                    }
                    if (isDigit(y+1, x)) { prod *= getNum(y+1, x); nums++ }
                    else {
                        if (isDigit(y+1, x-1)) { prod *= getNum(y+1, x-1); nums++ }
                        if (isDigit(y+1, x+1)) { prod *= getNum(y+1, x+1); nums++ }
                    }
                    if (nums == 2) sum += prod
                }

        println(sum)
    }
}