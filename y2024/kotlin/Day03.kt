package y2024

import general.Day

object Day03 : Day() {
    override fun a1() {
        "mul\\((\\d+),(\\d+)\\)".toRegex()
            .findAll(INPUT.readText())
            .sumOf {
                it.groupValues[1].toInt() *
                it.groupValues[2].toInt()
            }
            .also(::println)
    }

    override fun a2() {
        var enabled = true

        "mul\\((\\d+),(\\d+)\\)|(don't\\(\\))|(do\\(\\))".toRegex()
            .findAll(INPUT.readText())
            .sumOf {
                if (it.groups[3] != null) { enabled = false ; 0 }
                else if (it.groups[4] != null) { enabled = true ; 0 }
                else if (enabled)
                    it.groupValues[1].toInt() *
                    it.groupValues[2].toInt()
                else 0
            }
            .also(::println)
    }
}