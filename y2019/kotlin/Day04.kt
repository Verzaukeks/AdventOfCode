package y2019

import general.Day

object Day04 : Day() {
    override val name = "Secure Container"

    override fun a1() {
        var valid = 0

        loop@for (i in 382345..843167) {
            var hasDouble = false

            var prevDigit = i % 10
            var number = i / 10

            for (ignore in 1..5) {
                val digit = number % 10

                if (digit == prevDigit) hasDouble = true
                if (digit > prevDigit) continue@loop

                prevDigit = digit
                number /= 10
            }

            if (hasDouble)
                valid += 1
        }

        println(valid)
    }

    override fun a2() {
        var valid = 0

        loop@for (i in 382345..843167) {
            var hasDouble = false
            var doubleCount = 0

            var prevDigit = i % 10
            var number = i / 10

            for (ignore in 1..5) {
                val digit = number % 10

                if (digit == prevDigit) doubleCount++
                if (digit > prevDigit) continue@loop
                if (digit < prevDigit) {
                    hasDouble = hasDouble || doubleCount == 1
                    doubleCount = 0
                }

                prevDigit = digit
                number /= 10
            }

            if (hasDouble || doubleCount == 1)
                valid += 1
        }

        println(valid)
    }
}