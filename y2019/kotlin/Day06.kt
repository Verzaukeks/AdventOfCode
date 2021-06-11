package y2019

import general.Day

object Day06 : Day() {
    override val name = "Universal Orbit Map"

    override fun a1() {
        var orbits = 0

        val toCheck = INPUT.readLines().map { it.split(")") } as ArrayList
        val checked = ArrayList<String>().apply { add("COM") }
        val toAdd = ArrayList<String>()
        var wave = 1

        while (toCheck.isNotEmpty()) {


            val it = toCheck.iterator()
            while (it.hasNext()) {
                val pair = it.next()

                if (pair[0] in checked) {
                    orbits += wave
                    toAdd += pair[1]
                    it.remove()
                }
            }

            checked += toAdd
            toAdd.clear()

            wave += 1
        }

        println(orbits)
    }

    override fun a2() {
        val chainMap = INPUT.readLines().associate { it.split(")").let { Pair(it[1], it[0]) } }

        val youList = ArrayList<String>()
        val sanList = ArrayList<String>()

        var current = "YOU"
        while (true) {
            val next = chainMap[current] ?: break
            youList += next
            current = next
        }

        current = "SAN"
        while (true) {
            val next = chainMap[current] ?: break
            sanList += next
            current = next
        }

        for (planet in youList)
            if (planet in sanList) {

                val jumps = youList.indexOf(planet) + sanList.indexOf(planet)
                return println(jumps)
            }
    }
}