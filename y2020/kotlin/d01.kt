package y2020

import y2020.inputs.Files

object d01 {
    fun a1() {
        val input = Files[1]
                .readLines()
                .map { s -> s.toInt() }
                .sorted()

        a@for (a in input) {
            b@for (b in input) {
                if (a + b > 2020) break@b
                if (a + b == 2020) return println(a * b)
            }
        }
    }

    fun a2() {
        val input = Files[1]
                .readLines()
                .map { s -> s.toInt() }
                .sorted()

        a@for (a in input) {
            b@for (b in input) {
                if (a + b > 2020) break@b
                c@for (c in input) {
                    if (a + b + c > 2020) break@c
                    if (a + b + c == 2020) return println(a * b * c)
                }
            }
        }
    }
}
