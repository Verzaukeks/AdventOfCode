package y2023

import general.Day

object Day01 : Day() {
    override val name = "Trebuchet?!"

    override fun a1() {
        println(
            INPUT
                .readLines()
                .asSequence()
                .map { it.filter { it.isDigit() } }
                .map { it.toCharArray() }
                .map { it.first() + "" + it.last() }
                .map { it.toInt() }
                .sum()
        )
    }

    override fun a2() {
        println(
            INPUT
                .readLines()
                .asSequence()
                .map { it
                    .replace("one", "one1one")
                    .replace("two", "two2two")
                    .replace("three", "three3three")
                    .replace("four", "four4four")
                    .replace("five", "five5five")
                    .replace("six", "six6six")
                    .replace("seven", "seven7seven")
                    .replace("eight", "eight8eight")
                    .replace("nine", "nine9nine")
                }
                .map { it.filter { it.isDigit() } }
                .map { it.toCharArray() }
                .map { it.first() + "" + it.last() }
                .map { it.toInt() }
                .sum()
        )
    }
}