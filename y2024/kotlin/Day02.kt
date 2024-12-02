package y2024

import general.Day
import kotlin.math.absoluteValue

object Day02 : Day() {
    override val name = "Red-Nosed Reports"

    override fun a1() {
        INPUT
            .readLines()
            .count { line ->
                val ls = line
                    .split(" ")
                    .map(String::toInt)
                    .zipWithNext()
                    .map { (a, b) -> Pair(a < b, (a - b).absoluteValue in 1..3) }
                ls.all { it == Pair(true, true) } || ls.all { it == Pair(false, true) }
            }
            .also(::println)
    }

    override fun a2() {
        INPUT
            .readLines()
            .map { it.split(" ").map(String::toInt) }
            .count { ls -> ls.indices
                .map { ix -> ls.take(ix) + ls.drop(ix + 1) }
                .any { cut ->
                    val pairs = cut.zipWithNext().map { (a, b) -> Pair(a < b, (a - b).absoluteValue in 1..3) }
                    pairs.all { it == Pair(true, true) } || pairs.all { it == Pair(false, true) }
                }
            }
            .also(::println)
    }
}