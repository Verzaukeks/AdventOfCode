package y2023

import general.Day

object Day07 : Day() {
    override val name = "Camel Cards"

    data class Cnt(val ls: List<Int>,
                   val sz: Int = ls.size,
                   val sum: Int = ls.sum(),
                   val fst: Int = ls.firstOrNull() ?: 0,
                   val lst: Int = ls.lastOrNull() ?: 0)
    private fun count(str: String): Cnt {
        return Cnt(str.toCharArray().toSet().map { c -> str.count { it == c  } }.sortedDescending())
    }

    enum class Kind(val check: (Cnt) -> Boolean) {
        FIVE_OF_A_KIND ({ (_, sz, sum, fst, lst) -> sz == 0 || fst == sum }),
        FOUR_OF_A_KIND ({ (_, sz, sum, fst, lst) -> fst == sum - 1 && lst == 1 }),
        FULL_HOUSE     ({ (_, sz, sum, fst, lst) -> fst == sum - 2 && lst == 2 }),
        THREE_OF_A_KIND({ (_, sz, sum, fst, lst) -> fst == sum - 2 && lst == 1 }),
        TWO_PAIR       ({ (_, sz, sum, fst, lst) -> sz == 3 }),
        ONE_PAIR       ({ (_, sz, sum, fst, lst) -> sum == 4 || fst == 2 }),
        HIGH_CARD      ({ (_, sz, sum, fst, lst) -> true }),
    }

    private fun String.kind1() = Kind.values().first { it.check(count(this)) }
    private fun String.kind2() = Kind.values().first { it.check(count(this.filter { it != 'J' })) }

    private fun Char.strength1() = "23456789TJQKA".indexOf(this)
    private fun Char.strength2() = "J23456789TQKA".indexOf(this)

    override fun a1() {
        println(
            INPUT.readLines().asSequence().map { it.split(" ") }
                .sortedBy { it[0][4].strength1() }
                .sortedBy { it[0][3].strength1() }
                .sortedBy { it[0][2].strength1() }
                .sortedBy { it[0][1].strength1() }
                .sortedBy { it[0][0].strength1() }
                .sortedBy { 10 - it[0].kind1().ordinal }
                .withIndex()
                .sumOf { (i, h) -> (i + 1) * h[1].toInt() }
        )
    }

    override fun a2() {
        println(
            INPUT.readLines().asSequence().map { it.split(" ") }
                .sortedBy { it[0][4].strength2() }
                .sortedBy { it[0][3].strength2() }
                .sortedBy { it[0][2].strength2() }
                .sortedBy { it[0][1].strength2() }
                .sortedBy { it[0][0].strength2() }
                .sortedBy { 10 - it[0].kind2().ordinal }
                .withIndex()
                .sumOf { (i, h) -> (i + 1) * h[1].toInt() }
        )
    }
}