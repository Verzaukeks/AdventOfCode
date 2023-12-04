package y2023

import general.Day

object Day04 : Day() {
    override val name = "Scratchcards"

    override fun a1() {
        println(
            INPUT.readLines()
                .map { it
                    .substringAfter(":")
                    .split("|")
                    .map {
                        it.split(" ")
                            .filter { it.isNotBlank() }
                            .map { it.toInt() }
                    }
                    .let { Pair(it[0], it[1]) }
                }
                .map { (a, b) ->
                    (1 shl a.count { it in b }) / 2
                }
                .sum()
        )
    }

    override fun a2() {
        var sum = 0
        val count = IntArray(INPUT.readLines().size) { 1 }
        INPUT.readLines()
            .map { it
                .substringAfter(":")
                .split("|")
                .map {
                    it.split(" ")
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
                }
                .let { Pair(it[0], it[1]) }
            }
            .map { (a, b) -> a.count { it in b } }
            .forEachIndexed { idx, cnt ->
                sum += count[idx]
                for (j in (idx+1)..(idx+cnt))
                    count[j] += count[idx]
            }
        println(sum)
    }
}