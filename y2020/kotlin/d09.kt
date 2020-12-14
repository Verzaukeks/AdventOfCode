package y2020

import kotlin.math.max
import kotlin.math.min

object d09 {
    fun a1() { println(a1_()) }
    fun a1_(
            input: List<Long> = Files[9].readLines().map { it.toLong() },
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
                return n
            }

            for (k in 1 until preamble.size)
                preamble[k-1] = preamble[k]
            preamble[preamble.size-1] = n

            i ++
        }

        return -1L
    }

    fun a2() {
        val input = Files[9].readLines().map { it.toLong() }
        val n = a1_(input)
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