package y2022

import general.Day
import kotlin.math.max

object Day19 : Day() {
    override val name = "Not Enough Minerals"

    class Blueprint(list: List<Int>) {
        val oreCostOre = list[0]
        val clayCostOre = list[1]
        val obsidianCostOre = list[2]
        val obsidianCostClay = list[3]
        val geodeCostOre = list[4]
        val geodeCostObsidian = list[5]
    }

    data class State(
        val oreRobots: Int,
        val clayRobots: Int,
        val obsidianRobots: Int,
        val geodeRobots: Int,
        val ore: Int,
        val clay: Int,
        val obsidian: Int,
        val geode: Int,
        val timeLeft: Int,
    ) {
        fun shouldBuildOre(bp: Blueprint) = oreRobots * timeLeft + ore < timeLeft * max(bp.oreCostOre, max(bp.clayCostOre, max(bp.obsidianCostOre, bp.geodeCostOre)))
        fun shouldBuildClay(bp: Blueprint) = clayRobots * timeLeft + clay < timeLeft * bp.obsidianCostClay
        fun shouldBuildObsidian(bp: Blueprint) = obsidianRobots * timeLeft + obsidian < timeLeft * bp.geodeCostObsidian

        fun canBuildOre(bp: Blueprint) = ore >= bp.oreCostOre
        fun canBuildClay(bp: Blueprint) = ore >= bp.clayCostOre
        fun canBuildObsidian(bp: Blueprint) = ore >= bp.obsidianCostOre && clay >= bp.obsidianCostClay
        fun canBuildGeode(bp: Blueprint) = ore >= bp.geodeCostOre && obsidian >= bp.geodeCostObsidian

        fun buildOre(bp: Blueprint) = copy(ore = ore - bp.oreCostOre, oreRobots = oreRobots + 1)
        fun buildClay(bp: Blueprint) = copy(ore = ore - bp.clayCostOre, clayRobots = clayRobots + 1)
        fun buildObsidian(bp: Blueprint) = copy(ore = ore - bp.obsidianCostOre, clay = clay - bp.obsidianCostClay, obsidianRobots = obsidianRobots + 1)
        fun buildGeode(bp: Blueprint) = copy(ore = ore - bp.geodeCostOre, obsidian = obsidian - bp.geodeCostObsidian, geodeRobots = geodeRobots + 1)

        fun increase() = copy(ore = ore + oreRobots, clay = clay + clayRobots, obsidian = obsidian + obsidianRobots, geode = geode + geodeRobots, timeLeft = timeLeft - 1)
    }

    private fun simulate(bp: Blueprint, time: Int): Int {

        fun dfs(state: State): Int {
            if (state.timeLeft == 0) return state.geode
            var ret = state.geode + state.geodeRobots * state.timeLeft

            if (state.obsidianRobots > 0) {
                var s = state
                while (!s.canBuildGeode(bp)) s = s.increase()
                if (s.timeLeft > 0) ret = max(ret, dfs(s.increase().buildGeode(bp)))
            }

            if (state.clayRobots > 0 && state.shouldBuildObsidian(bp)) {
                var s = state
                while (!s.canBuildObsidian(bp)) s = s.increase()
                if (s.timeLeft > 0) ret = max(ret, dfs(s.increase().buildObsidian(bp)))
            }

            if (state.shouldBuildClay(bp)) {
                var s = state
                while (!s.canBuildClay(bp)) s = s.increase()
                if (s.timeLeft > 0) ret = max(ret, dfs(s.increase().buildClay(bp)))
            }

            if (state.shouldBuildOre(bp)) {
                var s = state
                while (!s.canBuildOre(bp)) s = s.increase()
                if (s.timeLeft > 0) ret = max(ret, dfs(s.increase().buildOre(bp)))
            }

            return ret
        }

        return dfs(State(1, 0, 0, 0, 0, 0, 0, 0, time))
    }

    override fun a1() {
        val blueprints = INPUT.readLines().map {
            it.substringAfter(":").substringBeforeLast(".")
                .replace("and", ".")
                .replace("[^0-9.]".toRegex(), "")
                .split(".")
                .map { it.toInt() }
                .let { Blueprint(it) }
        }

        var sum = 0
        for ((i, bp) in blueprints.withIndex())
            sum += (i + 1) * simulate(bp, 24)

        println(sum)
    }

    override fun a2() {
        val blueprints = INPUT.readLines().map {
            it.substringAfter(":").substringBeforeLast(".")
                .replace("and", ".")
                .replace("[^0-9.]".toRegex(), "")
                .split(".")
                .map { it.toInt() }
                .let { Blueprint(it) }
        }.take(3)

        var mul = 1
        for (bp in blueprints)
            mul *= simulate(bp, 32)

        println(mul)
    }
}