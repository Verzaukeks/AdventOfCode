package y2020

import y2020.inputs.Files

object d02 {
    fun a1() {
        val input = Files[2]
                .readLines()

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

    fun a2() {
        val input = Files[2]
                .readLines()

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
