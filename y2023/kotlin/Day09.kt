package y2023

import general.Day

object Day09 : Day() {
    override val name = "Mirage Maintenance"

    private fun parse() = INPUT.readLines().map { it.split(" ").map { it.toInt() } }

    private fun seq(nums: List<Int>): Long {
        if (nums.all { it == 0 }) return 0
        val s = nums.drop(1).zip(nums).map { (a, b) -> a - b }
        val n = seq(s)
        return nums.last() + n
    }

    override fun a1() {
        println(parse().sumOf { seq(it) })
    }

    override fun a2() {
        println(parse().sumOf { seq(it.asReversed()) })
    }
}