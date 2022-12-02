package y2022

import general.Day

object Day02 : Day() {
    override val name = "Rock Paper Scissors"

    override fun a1() {
        println(INPUT
            .readText()
            .replace("\r", "")
            .split("\n")
            .sumOf {
                when (it[2]) {
                    'X' -> 1 + if (it[0] == 'A') 3 else if (it[0] == 'B') 0 else 6
                    'Y' -> 2 + if (it[0] == 'A') 6 else if (it[0] == 'B') 3 else 0
                    'Z' -> 3 + if (it[0] == 'A') 0 else if (it[0] == 'B') 6 else 3
                    else -> 0
                }
            })
    }

    override fun a2() {
        println(INPUT
            .readText()
            .replace("\r", "")
            .split("\n")
            .sumOf {
                when (
                    when (it[2]) {
                        'X' -> if (it[0] == 'A') 'Z' else if (it[0] == 'B') 'X' else 'Y'
                        'Y' -> if (it[0] == 'A') 'X' else if (it[0] == 'B') 'Y' else 'Z'
                        'Z' -> if (it[0] == 'A') 'Y' else if (it[0] == 'B') 'Z' else 'X'
                        else -> it[2]
                    }
                ) {
                    'X' -> 1 + if (it[0] == 'A') 3 else if (it[0] == 'B') 0 else 6
                    'Y' -> 2 + if (it[0] == 'A') 6 else if (it[0] == 'B') 3 else 0
                    'Z' -> 3 + if (it[0] == 'A') 0 else if (it[0] == 'B') 6 else 3
                    else -> 0
                }
            })
    }
}