package y2020

import general.Day

object Day02 : Day() {
    override val name = "Password Philosophy"

    override fun a1() {
        val input = INPUT.readLines()

        var valid = 0

        for (line in input) {
            val min = line.substringBefore("-").toInt()
            val max = line.substringAfter("-").substringBefore(" ").toInt()
            val policy = line.substringAfter(" ").substringBefore(": ")[0]
            val password = line.substringAfter(": ")

            val count = password.count { c -> c == policy }
            if (count in min..max)
                valid ++
        }

        println("valid: $valid")
    }

    override fun a2() {
        val input = INPUT.readLines()

        var valid = 0

        for (line in input) {
            val pos1 = line.substringBefore("-").toInt()
            val pos2 = line.substringAfter("-").substringBefore(" ").toInt()
            val policy = line.substringAfter(" ").substringBefore(": ")[0]
            val password = line.substringAfter(": ")

            val a = password.length >= pos1 && password[pos1 - 1] == policy
            val b = password.length >= pos2 && password[pos2 - 1] == policy
            if (a xor b)
                valid ++
        }

        println("valid: $valid")
    }
}
