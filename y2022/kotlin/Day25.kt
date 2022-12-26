package y2022

import general.Day

object Day25 : Day() {
    override val name = "Full of Hot Air"

    private fun String.fromSNAFU(): Long {
        var num = 0L
        var exp = 1L
        for (i in indices.reversed()) {
            num += exp * when (get(i)) {
                '2' -> 2
                '1' -> 1
                '-' -> -1
                '=' -> -2
                else -> 0
            }
            exp *= 5
        }
        return num
    }

    private fun Long.toSNAFU(): String {
        if (this == 0L) return ""
        val div = (this / 5)
        val rem = (this % 5).toInt()
        return (div + (0.4 * rem).toLong()).toSNAFU() + "012=-"[rem]
    }

    override fun a1() {
        val sum = INPUT.readLines().sumOf { it.fromSNAFU() }
        println(sum.toSNAFU())
    }

    override fun a2() {
        println("50 stars!")
    }
}