package y2019

import general.Day
import java.lang.Long.min
import kotlin.math.ceil

object Day14 : Day() {
    override val name = "Space Stoichiometry"

    class Combo(var amount: Long, val chemical: String) {
        fun copy() = Combo(amount, chemical)

        override fun toString() = "$amount $chemical"
    }

    class Recipe(val from: List<Combo>, val to: Combo) {
        override fun toString() = from.joinToString() + " => $to"
    }

    class Inventory {
        private val inventory = ArrayList<Combo>()

        fun next() = inventory.firstOrNull { it.amount > 0 }
//        fun next() = inventory.filter { it.amount > 0 }.randomOrNull()

        fun subtract(combo: Combo) {
            val item = inventory.firstOrNull { it.chemical == combo.chemical } ?: return
            val subtract = min(combo.amount, item.amount)

            item.amount -= subtract
            combo.amount -= subtract
        }

        fun add(combo: Combo) {
            val item = inventory.firstOrNull { it.chemical == combo.chemical }

            if (item == null) {
                inventory += combo.copy()
                combo.amount = 0
                return
            }

            item.amount += combo.amount
            combo.amount = 0
        }

        override fun toString() = inventory.filter { it.amount > 0 }.joinToString()
    }

    override fun a1() {
        val recipes = INPUT.readLines()
            .map {
                val (from, to) = it.split(" => ")
                Recipe(
                    from.split(", ").map {
                        it.split(" ").let { Combo(it[0].toLong(), it[1]) }
                    },
                    to.split(" ").let { Combo(it[0].toLong(), it[1]) }
                )
            }

        var ores = 0L
        val invNeeds = Inventory()
        val invHas = Inventory()

        invNeeds.add(Combo(1, "FUEL"))

        // while we still need something to be made out of ore
        while (true) {
            val need = invNeeds.next() ?: break

            // reduce everything that we have in the inventory
            invHas.subtract(need)

            // while we still need more do a reaction
            val recipe = recipes.first { it.to.chemical == need.chemical }
            while (need.amount > 0) {

                need.amount -= recipe.to.amount

                // get from inventory or add to needed list
                for (fromRaw in recipe.from) {
                    val from = fromRaw.copy()

                    if (from.chemical == "ORE") {
                        ores += from.amount
                        continue
                    }

                    invHas.subtract(from)
                    if (from.amount > 0)
                        invNeeds.add(from)
                }
            }

            // if we made more than we needed add overflow to inventory
            if (need.amount < 0) {
                need.amount = -need.amount
                invHas.add(need)
            }
        }

        // print the amount of ores we need
        println(ores)
    }

    override fun a2() {
        /*
        This solution works within a reasonable amount of time,
        but another attempt to solve this may be a1() with doubles
        and without inventory to calculate an exact ores/fuel. (Didn't check.)
         */

        val recipes = INPUT.readLines()
            .map {
                val (from, to) = it.split(" => ")
                Day14.Recipe(
                    from.split(", ").map {
                        it.split(" ").let { Combo(it[0].toLong(), it[1]) }
                    },
                    to.split(" ").let { Combo(it[0].toLong(), it[1]) }
                )
            }

        var ores = 1000000000000L
        var fuels = 0L
        val invNeeds = Inventory()
        val invHas = Inventory()

        val oresNeededForOneFuel = 720484
        val fuelsCanSafeMake = ores / oresNeededForOneFuel
        invNeeds.add(Combo(fuelsCanSafeMake, "FUEL"))
        fuels += fuelsCanSafeMake

        while (ores > 0) {
            invNeeds.add(Combo(1, "FUEL"))
            fuels += 1

            while (true) {
                val need = invNeeds.next() ?: break

                invHas.subtract(need)

                val recipe = recipes.first { it.to.chemical == need.chemical }
                val times = ceil(need.amount.toDouble() / recipe.to.amount).toInt()

                need.amount -= recipe.to.amount * times

                for (fromRaw in recipe.from) {
                    val from = fromRaw.copy()
                    from.amount *= times

                    if (from.chemical == "ORE") {
                        ores -= from.amount

                        if (ores < 0)
                            return println("${fuels-1} FUEL")
                        continue
                    }

                    invHas.subtract(from)
                    if (from.amount > 0)
                        invNeeds.add(from)
                }

                if (need.amount < 0) {
                    need.amount = -need.amount
                    invHas.add(need)
                }
            }
        }
    }
}