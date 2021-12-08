package y2021

import general.Day
import java.util.*

object Day08 : Day() {
    override val name = "Seven Segment Search"

    override fun a1() {
        val input = INPUT.readLines()

        var digits = 0
        for (line in input)
            for (digit in line.substringAfter(" | ").split(" ")) {
                val length = digit.length
                if (length == 2 || length == 4 || length == 3 || length == 7)
                    digits += 1
            }

        println(digits)
    }

    override fun a2() {
        val input = INPUT.readLines()
        var output = 0

        /*
             aaa
            f   b
            f   b
             ggg
            e   c
            e   c
             ddd

             map1[x=?]   abcdefg (=possible solution ; 1=possible ; 0=not possible)
             map1[a=0] 0b1111111
             map1[b=1] 0b1111111
             map1[c=2] 0b1111111
             map1[d=3] 0b1111111
             map1[e=4] 0b1111111
             map1[f=5] 0b1111111
             map1[g=6] 0b1111111

             map2[0] = List of all values with length = 5 (possible numbers are 2,3,5)
             map2[1] = List of all values with length = 6 (possible numbers are 0,6,9)
         */

        for (line in input) {
            val (first, second) = line.split(" | ")
            var entry = 0

            val map1 = IntArray(7) { 0b1111111 }
            val map2 = Array(2) { ArrayList<String>() }

            for (value in first.split(" ")) reduce1(map1, map2, value)
            reduce2(map1, map2)
            reduce3(map1)
            for (value in second.split(" ")) entry = entry * 10 + calculate(map1, value)

            output += entry
        }

        println(output)
    }

    /**
     * takes each value and checks:
     * if value.length == 2
     *  => value digit hast to be a 1
     *  => segments in value must be either at place b or c
     *  => all other segments not in value cannot be b or c
     * if ...
     */
    private fun reduce1(map1: IntArray, map2: Array<ArrayList<String>>, value: String) {
        val chars = value.toCharArray().map { it - 'a' }
        when (chars.size) {
            2 -> {
                for (c in 0..6)
                    if (c in chars) map1[c] = map1[c] and 0b0110000
                    else            map1[c] = map1[c] and 0b1001111
            }
            3 -> {
                for (c in 0..6)
                    if (c in chars) map1[c] = map1[c] and 0b1110000
                    else            map1[c] = map1[c] and 0b0001111
            }
            4 -> {
                for (c in 0..6)
                    if (c in chars) map1[c] = map1[c] and 0b0110011
                    else            map1[c] = map1[c] and 0b1001100
            }
            5 -> { /* 0b1111111 */
                map2[0] += value
            }
            6 -> { /* 0b1111111 */
                map2[1] += value
            }
            7 -> { /* 0b1111111 */ }
            else -> error(value)
        }
    }

    /**
     * all values with length of 5 have to be either a 2,3 or 5
     * => all digits have 3 common segments (a, d, g)
     * => all segments who are in each value can either be a, d or g
     *
     * all values with length of 6 have to be either a 0,6 or 9
     * => all digits have 4 common segments (a, c, d, f)
     * => all segments who are in each value can either be a, c, d or f
     */
    private fun reduce2(map1: IntArray, map2: Array<ArrayList<String>>) {
        // 0 -> 2,3,5 -> abcdefg => a__d__g
        // 1 -> 0,6,9 -> abcdefg => a_cd_f_

        var l5 = "abcdefg"
        var l6 = "abcdefg"

        for (value in map2[0])
            for (char in l5.split(""))
                if (!value.contains(char))
                    l5 = l5.replace(char, "")

        for (value in map2[1])
            for (char in l6.split(""))
                if (!value.contains(char))
                    l6 = l6.replace(char, "")

        val l5c = l5.toCharArray().map { it - 'a' }
        val l6c = l6.toCharArray().map { it - 'a' }

        for (c in 0..6) {
            if (c in l5c) map1[c] = map1[c] and 0b1001001
            if (c in l6c) map1[c] = map1[c] and 0b1011010
        }
    }

    /**
     * if segment a can only be a specific segment x
     * => no other segment can be segment x
     * if ...
     */
    private fun reduce3(map: IntArray) {
        for (c in 0..6)
            if (map[c].toString(2).count { it == '1' } == 1)
                for (i in 0..6) if (i != c)
                    map[i] = map[i] and (0b1111111 xor map[c])
    }

    /**
     * determine how many or which segments are used
     * and conclude/interpret the correct number/digit
     *
     * if no digit can be found => cry
     */
    private fun calculate(map: IntArray, value: String): Int {
        when (value.length) {
            2 -> return 1
            3 -> return 7
            4 -> return 4
            7 -> return 8
        }

        val chars = value.toCharArray().map { it - 'a' }
        var number = 0b0000000

        for (c in 0..6)
            if (c in chars)
                number = number or map[c]

        when (number) {
            0b1111110 -> return 0
            0b1101101 -> return 2
            0b1111001 -> return 3
            0b1011011 -> return 5
            0b1011111 -> return 6
            0b1111011 -> return 9
        }

        println(map.joinToString { it.toString(2).let { "0000000$it".substring(it.length) } })
        println(value)
        println(number.toString(2).let { "0000000$it".substring(it.length) })
        error("")
    }
}