package y2020

import general.Day

object Day01 : Day() {
    override val name = "Report Repair"

    override fun a1() {
        val input = INPUT
                .readLines()
                .map { s -> s.toInt() }
                .sorted()

        a@for (a in input)
            b@for (b in input) {
                if (a + b > 2020) break@b
                if (a + b == 2020) return println(a * b)
            }
    }

    override fun a2() {
        val input = INPUT
                .readLines()
                .map { s -> s.toInt() }
                .sorted()

        a@for (a in input)
            b@for (b in input) {
                if (a + b > 2020) break@b
                c@for (c in input) {
                    if (a + b + c > 2020) break@c
                    if (a + b + c == 2020) return println(a * b * c)
                }
            }
    }
}
