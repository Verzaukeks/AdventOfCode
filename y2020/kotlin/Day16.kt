package y2020

object Day16 : Day() {

    override val day = 16
    override val name = "Ticket Translation"

    data class Field(val name: String, val range1: IntRange, val range2: IntRange) {
        fun isValid(number: Int) = number in range1 || number in range2
    }

    override fun a1() {
        val input = INPUT.readText().replace("\r", "")

        // parse fields    name: r-r or r-r
        val fields = ArrayList<Field>()
        val rawFields = input.substringBefore("\n\n")

        for (rawField in rawFields.split("\n")) {
            val name = rawField.substringBefore(": ")
            val ranges = rawField.substringAfter(": ")
                    .split("-", " or ").map { it.toInt() }

            val range1 = ranges[0]..ranges[1]
            val range2 = ranges[2]..ranges[3]
            fields += Field(name, range1, range2)
        }

        // iterate over all nearbyTickets
        var errorRate = 0
        val nearbyTickets = input
                .substringAfter("nearby tickets:\n").split("\n")
                .map {
                    it.split(",")
                      .map { n -> n.toInt() }
                }.toMutableList()
        for (ticket in nearbyTickets) {

            num@for (number in ticket) {
                for (field in fields) {
                    if (field.isValid(number))
                        continue@num
                }
                errorRate += number
            }
        }

        // output
        println(errorRate)
    }

    override fun a2() {
        val input = INPUT.readText().replace("\r", "")

        // parse fields    name: r-r or r-r
        val fields = ArrayList<Field>()
        val rawFields = input.substringBefore("\n\n")

        for (rawField in rawFields.split("\n")) {
            val name = rawField.substringBefore(": ")
            val ranges = rawField.substringAfter(": ")
                    .split("-", " or ").map { it.toInt() }

            val range1 = ranges[0]..ranges[1]
            val range2 = ranges[2]..ranges[3]
            fields += Field(name, range1, range2)
        }

        // discard invalid tickets
        val nearbyTickets = input
                .substringAfter("nearby tickets:\n").split("\n")
                .map {
                    it.split(",")
                      .map { n -> n.toInt() }
                }.toMutableList()
        val ntIt = nearbyTickets.iterator()

        while (ntIt.hasNext()) {
            val numbers = ntIt.next()

            num@for (number in numbers) {
                for (field in fields) {
                    if (field.isValid(number))
                        continue@num
                }
                ntIt.remove()
            }
        }

        // map every index to all possible fields
        val indexToField = Array(nearbyTickets[0].size) { ArrayList<Field>() }
        val tmpList = ArrayList<Field>()

        for (index in indexToField.indices) {
            tmpList.clear()
            tmpList += fields

            for (ticket in nearbyTickets) {
                val number = ticket[index]

                val it = tmpList.iterator()
                while (it.hasNext()) {
                    if (!it.next().isValid(number))
                        it.remove()
                }
            }

            indexToField[index].addAll(tmpList)
        }

        //var i = 0
        //println("${i++}:")
        //println(indexToField.withIndex().joinToString("\n") { "${it.index}: ${it.value}" })

        // reduce every index to one field
        var finished = false
        while (!finished) {
            finished = true

            for (array in indexToField)
                if (array.size == 1) {

                    val f = array[0]
                    for (otherArray in indexToField)
                        if (otherArray != array)
                            otherArray -= f
                }
                else finished = false

            //println("${i++}:")
            //println(indexToField.withIndex().joinToString("\n") { "${it.index}: ${it.value}" })
        }

        // output
        val yourTicket = input.substringAfter("your ticket:\n")
                .substringBefore("\n").split(",").map { it.toInt() }

        var depature = 1L // make it loooooooong
        for (index in indexToField.indices)
            if (indexToField[index][0].name.startsWith("departure"))
                depature *= yourTicket[index]

        println(depature)
    }
}