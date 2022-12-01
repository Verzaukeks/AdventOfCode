package y2022

import general.Day

object Day01 : Day() {
    override val name = "Calorie Counting"

    override fun a1() {
        println(INPUT
            .readText()
            .replace("\r", "")
            .split("\n\n")
            .maxOfOrNull {
                it.split("\n")
                    .sumOf { it.toInt() }
            })
    }

    override fun a2() {
        println(INPUT
            .readText()
            .replace("\r", "")
            .split("\n\n")
            .map {
                it.split("\n")
                    .sumOf { it.toInt() }
            }
            .sorted()
            .reversed()
            .subList(0, 3)
            .sum())
    }
}