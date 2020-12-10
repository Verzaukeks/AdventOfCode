package y2020

import y2020.inputs.Files

object d07 {
    data class Contain(val bag: String, val number: Int)

    // overkill?
    fun a1() {
        val input = Files[7].readLines()
        val data = HashMap<String, ArrayList<Contain>>()

        val has = ArrayList<String>()
        val hasNot = ArrayList<String>()
        val unknown = ArrayList<String>()

        for (line in input) {
            val bag = line.substringBefore(" bags")

            when {
                line.startsWith("shiny gold") -> {}
                "shiny gold" in line -> has += bag
                "contain no" in line -> hasNot += bag
                else -> {
                    val contain = ArrayList<Contain>()

                    val args = line.substringAfter("bags contain ").split(", ")
                    for (arg in args) {

                        val number = arg.substringBefore(" ").toInt()
                        val hasBag = arg.substringAfter(" ").substringBefore(" bag")
                        contain += Contain(hasBag, number)
                    }

                    data[bag] = contain
                    unknown += bag
                }
            }
        }

        while (unknown.size > 0) {
            val iterator = unknown.iterator()
            a@while (iterator.hasNext()) {
                val bag = iterator.next()
                var isUnknown = false

                for (contain in data[bag]!!) {

                    if (contain.bag in has) {
                        has += bag
                        iterator.remove()
                        continue@a
                    }

                    if (contain.bag !in hasNot)
                        isUnknown = true
                }

                // when we know for sure that every bag does not contain any shiny gold bag
                if (!isUnknown) {
                    hasNot += bag
                    iterator.remove()
                }
            }
        }

        println(has.size)
    }

    fun a2() {
        val input = Files[7].readLines()
        val data = HashMap<String, ArrayList<Contain>>()

        for (line in input) {
            val bag = line.substringBefore(" bags")
            if ("contain no" in line) {
                data[bag] = ArrayList()
                continue
            }

            val contain = ArrayList<Contain>()

            val args = line.substringAfter("bags contain ").split(", ")
            for (arg in args) {

                val number = arg.substringBefore(" ").toInt()
                val hasBag = arg.substringAfter(" ").substringBefore(" bag")
                contain += Contain(hasBag, number)
            }

            data[bag] = contain
        }

        println(a2_recursive(data, "shiny gold"))
    }

    fun a2_recursive(data: HashMap<String, ArrayList<Contain>>, bag: String): Int {
        var bags = 0
        for (contain in data[bag]!!) {
            bags += contain.number
            bags += contain.number * a2_recursive(data, contain.bag)
        }
        return bags
    }
}