package y2023

import general.Day

object Day02 : Day() {
    override val name = "Cube Conundrum"

    private fun List<String>.parse() =
        map {
            Pair(
                it.substringBefore(":").substringAfter(" ").toInt(),
                it.substringAfter(":"))
        }
        .map { g ->
            "0 red, 0 green, 0 blue; ${g.second}"
                .split(",", ";").map {
                    it.trim().split(" ")
                }.map {
                    Pair(it[0].toInt(), it[1])
                }
                .groupBy { it.second }
                .let { Pair(g.first, it) }
        }

    override fun a1() {
        println(
            INPUT.readLines()
                .parse()
                .filter {
                    it.second["red"]!!.maxOf { it.first } <= 12 &&
                    it.second["green"]!!.maxOf { it.first } <= 13 &&
                    it.second["blue"]!!.maxOf { it.first } <= 14
                }
                .sumOf { it.first }
        )
    }

    override fun a2() {
        println(
            INPUT.readLines()
                .parse()
                .sumOf {
                    it.second["red"]!!.maxOf { it.first } *
                    it.second["green"]!!.maxOf { it.first } *
                    it.second["blue"]!!.maxOf { it.first }
                }
        )
    }
}