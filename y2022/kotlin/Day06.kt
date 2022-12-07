package y2022

import general.Day

object Day06 : Day() {
    override val name = "Tuning Trouble"

    override fun a1() {
        val line = INPUT.readText()

        for (i in 3 until line.length) {
            val arr = BooleanArray(26)
            arr[line[i - 3] - 'a'] = true
            arr[line[i - 2] - 'a'] = true
            arr[line[i - 1] - 'a'] = true
            arr[line[i    ] - 'a'] = true
            if (arr.count { it } == 4) {
                println(i + 1)
                return
            }
        }

        println(-1)
    }

    override fun a2() {
        val line = INPUT.readText()

        for (i in 13 until line.length) {
            val arr = BooleanArray(26)
            for (off in 0..13) arr[line[i - off] - 'a'] = true
            if (arr.count { it } == 14) return println(i + 1)
        }

        println(-1)
    }
}