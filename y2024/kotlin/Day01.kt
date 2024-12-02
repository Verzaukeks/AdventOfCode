package y2024

import general.Day
import kotlin.math.absoluteValue

object Day01 : Day() {
    override val name = "Historian Hysteria"

    override fun a1() {
        val bs = ArrayList<Int>()
        val cs = ArrayList<Int>()
        INPUT.readText()
            .split("\\W+".toRegex())
            .map(String::toInt)
            .chunked(2)
            .forEach { (b, c) -> bs += b ; cs += c }
        bs.sort()
        cs.sort()
        println(bs.zip(cs).sumOf { (a, b) -> (a - b).absoluteValue })
    }

    override fun a2() {
        val bs = LongArray(1_000_000)
        val cs = LongArray(1_000_000)
        INPUT.readText()
            .split("\\W+".toRegex())
            .map(String::toInt)
            .chunked(2)
            .forEach { (b, c) -> bs[b]++ ; cs[c]++ }
        println(bs.indices.sumOf { it * bs[it] * cs[it] })
    }
}