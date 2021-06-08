package y2020

import general.Day

object Day21 : Day() {
    override val name = "Allergen Assessment"

    override fun a1() {
        val input = INPUT.readLines()
        val map = HashMap<String, ArrayList<String>>()
        val ingredientsCounter = HashMap<String, Int>()

        for (line in input) {
            val ingredients = line.substringBefore(" (").split(" ")
            val allergens = line.substringAfter("contains ").substringBefore(")").split(", ")

            for (ingredient in ingredients)
                if (ingredient !in ingredientsCounter) ingredientsCounter[ingredient] = 1
                else ingredientsCounter[ingredient] = ingredientsCounter[ingredient]!! + 1

            for (allergen in allergens)
                if (allergen !in map) {
                    map[allergen] = ArrayList<String>().apply { addAll(ingredients) }
                } else {
                    val iterator = map[allergen]!!.iterator()
                    while (iterator.hasNext())
                        if (iterator.next() !in ingredients)
                            iterator.remove()
                }
        }

        var finished = false
        while (!finished) {
            finished = true

            for ((allergen, ingredients) in map.entries)
                if (ingredients.size == 1) {
                    for (entry in map.entries)
                        if (entry.key != allergen)
                            entry.value -= ingredients
                }
                else finished = false
        }

        for (ingredients in map.values)
            for (ingredient in ingredients)
                ingredientsCounter -= ingredient

        println(ingredientsCounter.values.sum())
    }

    override fun a2() {
        val input = INPUT.readLines()
        val map = HashMap<String, ArrayList<String>>()

        for (line in input) {
            val ingredients = line.substringBefore(" (").split(" ")
            val allergens = line.substringAfter("contains ").substringBefore(")").split(", ")

            for (allergen in allergens)
                if (allergen !in map) {
                    map[allergen] = ArrayList<String>().apply { addAll(ingredients) }
                } else {
                    val iterator = map[allergen]!!.iterator()
                    while (iterator.hasNext())
                        if (iterator.next() !in ingredients)
                            iterator.remove()
                }
        }

        var finished = false
        while (!finished) {
            finished = true

            for ((allergen, ingredients) in map.entries)
                if (ingredients.size == 1) {
                    for (entry in map.entries)
                        if (entry.key != allergen)
                            entry.value -= ingredients
                }
                else finished = false
        }

        map.keys.sorted().joinToString(",") { map[it]!![0] }.also(::println)
    }
}