package y2021

import general.Day

object Day01 : Day() {
    override val name = "Sonar Sweep"

    override fun a1() {
        val reports = INPUT.readLines().map { it.toInt() }

        var index = 0
        val increases = reports.count {
            (++index < reports.size && it < reports[index])
        }

        println("$increases measurements")
    }

    override fun a2() {
        val reports = INPUT.readLines().map { it.toInt() }

        var increases = 0
        var prev = reports[0] + reports[1] + reports[2]

        for (index in 3 until reports.size) {
            val next = prev - reports[index - 3] + reports[index]
            if (next > prev) increases += 1
            prev = next
        }

        println("$increases measurements")
    }
}