package y2020

import y2020.inputs.Files
import kotlin.math.max
import kotlin.math.min

object d9 {
    fun a1(
            input: List<Long> = Files[9].readLines().map { it.toLong() },
            printOutput: Boolean = true
    ): Long {

        val preamble = LongArray(25)
        for (i in preamble.indices)
            preamble[i] = input[i]

        var i = preamble.size
        while (i < input.size) {

            val n = input[i]
            var has = false

            a@for ((ai, a) in preamble.withIndex())
                for ((bi, b) in preamble.withIndex())
                    if (ai != bi && a + b == n) {
                        has = true
                        break@a
                    }

            if (!has) {
                if (printOutput) println(n)
                return n
            }

            for (k in 1 until preamble.size)
                preamble[k-1] = preamble[k]
            preamble[preamble.size-1] = n

            i ++
        }

        if (printOutput) println("-1")
        return -1L
    }

    fun a2() {
        val input = Files[9].readLines().map { it.toLong() }
        val n = a1(input, false)
        if (n == -1L) return

        for (i in input.indices) {
            var min = input[i]
            var max = min
            var sum = min
            var k = i

            while (true) {
                val cur = input[++k]
                sum += cur

                min = min(min, cur)
                max = max(max, cur)

                if (sum > n) break
                if (sum == n) {
                    println(min + max)
                    return
                }
            }
        }
    }
}