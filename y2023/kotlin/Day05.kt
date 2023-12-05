package y2023

import general.Day
import kotlin.math.max
import kotlin.math.min

object Day05 : Day() {
    override val name = "If You Give A Seed A Fertilizer"

    override fun a1() {
        val chunks = INPUT.readText().replace("\r", "").split("\n\n")
        val seeds = chunks[0].substringAfter(": ").split(" ").map { it.toLong() }
        val maps = chunks.drop(1).map {
            it.substringAfter("\n")
                .split("\n")
                .map {
                    val (a, b, c) = it.split(" ").map { it.toLong() }
                    Pair(a, b until b+c)
                }
        }

        println(
            seeds.minOf {
                var id = it
                for (map in maps)
                    for ((a, range) in map)
                        if (id in range) {
                            id += a - range.first
                            break
                        }
                id
            })
    }

    override fun a2() {
        val chunks = INPUT.readText().replace("\r", "").split("\n\n")
        val seeds = chunks[0].substringAfter(": ").split(" ").map { it.toLong() }
            .chunked(2).map { (a, b) -> a until a+b }
        val maps = chunks.drop(1).map {
            it.substringAfter("\n")
                .split("\n")
                .map {
                    val (a, b, c) = it.split(" ").map { it.toLong() }
                    Pair(a, b until b+c)
                }
        }

        fun intersect(ra: LongRange, rb: LongRange): LongRange {
            return max(ra.first, rb.first)..min(ra.last, rb.last)
        }

        fun getMin(range: LongRange, mapIdx: Int, mapJdx: Int): Long {
            if (range.isEmpty()) return Long.MAX_VALUE
            if (mapIdx >= maps.size) return range.first
            if (mapJdx >= maps[mapIdx].size) return getMin(range, mapIdx+1, 0)
            val (a, bc) = maps[mapIdx][mapJdx]
            val off = a - bc.first

            val ra = intersect(range, LongRange(Long.MIN_VALUE, bc.first-1))
            val rb = intersect(range, bc)
            val rc = intersect(range, LongRange(bc.last+1, Long.MAX_VALUE))

            return listOf(
                getMin(ra, mapIdx, mapJdx+1),
                getMin(rb.first+off..rb.last+off, mapIdx+1, 0),
                getMin(rc, mapIdx, mapJdx+1),
            ).min()
        }

        println(seeds.minOf { getMin(it, 0, 0) })
    }
}