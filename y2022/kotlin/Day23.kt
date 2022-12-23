package y2022

import general.Day
import java.util.*
import kotlin.math.max
import kotlin.math.min

object Day23 : Day() {
    override val name = "Unstable Diffusion"

    data class Elf(val x: Int, val y: Int) : Comparable<Elf> {
        override fun compareTo(other: Elf): Int {
            if (x == other.x) return y - other.y
            return x - other.x
        }
    }
    data class Dir(val ox: Int, val oy: Int, val rx: IntRange, val ry: IntRange)

    // definitely over-engineered
    override fun a1() {
        val map = Array(200) { CharArray(200) { '.' } }
        val count = Array(map.size) { IntArray(map.size) }
        var elves = TreeSet<Elf>()
        var elvesTo = TreeSet<Elf>()

        INPUT.readLines().map { it.toCharArray().withIndex() }.withIndex()
            .forEach { (y, line) ->
                line.forEach { (x, c) ->
                    if (c == '#') Elf(x + 65, y + 65).apply {
                        map[this.y][this.x] = '#'
                        elves.add(this)
                    }
                }
            }

        val adj = listOf(
            Dir(0, -1, -1..1, -1..-1),
            Dir(0, 1, -1..1, 1..1),
            Dir(-1, 0, -1..-1, -1..1),
            Dir(1, 0, 1..1, -1..1),
        )

        repeat(10) { r ->

            // redundancy check

            for ((x, y) in elves) {
                for (oy in -1..1) for (ox in -1..1)
                    count[y + oy][x + ox] = 0
            }

            for ((x, y) in elves) {
                var sum = 0
                for (oy in -1..1) for (ox in -1..1)
                    if (map[y + oy][x + ox] == '#') sum++
                if (sum == 0) throw IllegalStateException("elf did not found themselves")
                if (sum == 1) continue

                rr@for (rr in adj.indices) {
                    val (ox, oy, rx, ry) = adj[(r + rr) % 4]
                    for (oyy in ry) for (oxx in rx)
                        if (map[y + oyy][x + oxx] == '#') continue@rr
                    count[y + oy][x + ox]++
                    break
                }
            }

            for ((x, y) in elves) {
                var sum = 0
                for (oy in -1..1) for (ox in -1..1)
                    if (map[y + oy][x + ox] == '#') sum++
                if (sum == 0) throw IllegalStateException("elf did not found themselves")
                if (sum == 1) {
                    elvesTo.add(Elf(x, y))
                    continue
                }

                var added = false
                rr@for (rr in adj.indices) {
                    val (ox, oy, rx, ry) = adj[(r + rr) % 4]
                    for (oyy in ry) for (oxx in rx)
                        if (map[y + oyy][x + oxx] == '#') continue@rr
                    if (count[y + oy][x + ox] == 1) {
                        elvesTo.add(Elf(x + ox, y + oy))
                        added = true
                    }
                    break
                }
                if (added) continue
                elvesTo.add(Elf(x, y))
            }

            for ((x, y) in elves) map[y][x] = '.'
            for ((x, y) in elvesTo) map[y][x] = '#'

            elves = elvesTo.also { elvesTo = elves }
            elvesTo.clear()
        }

        var minX = elves.first().x
        var maxX = minX
        var minY = elves.first().y
        var maxY = minY

        for ((x, y) in elves) {
            minX = min(minX, x)
            maxX = max(maxX, x)
            minY = min(minY, y)
            maxY = max(maxY, y)
        }

        println((maxX - minX + 1) * (maxY - minY + 1) - elves.size)
    }

    // ah yes, copy paste
    override fun a2() {
        val map = Array(200) { CharArray(200) { '.' } }
        val count = Array(map.size) { IntArray(map.size) }
        var elves = TreeSet<Elf>()
        var elvesTo = TreeSet<Elf>()

        INPUT.readLines().map { it.toCharArray().withIndex() }.withIndex()
            .forEach { (y, line) ->
                line.forEach { (x, c) ->
                    if (c == '#') Elf(x + 65, y + 65).apply {
                        map[this.y][this.x] = '#'
                        elves.add(this)
                    }
                }
            }

        val adj = listOf(
            Dir(0, -1, -1..1, -1..-1),
            Dir(0, 1, -1..1, 1..1),
            Dir(-1, 0, -1..-1, -1..1),
            Dir(1, 0, 1..1, -1..1),
        )

        var r = 0
        var changed = true
        while (changed) {
            changed = false

            // redundancy check

            for ((x, y) in elves) {
                for (oy in -1..1) for (ox in -1..1)
                    count[y + oy][x + ox] = 0
            }

            for ((x, y) in elves) {
                var sum = 0
                for (oy in -1..1) for (ox in -1..1)
                    if (map[y + oy][x + ox] == '#') sum++
                if (sum == 0) throw IllegalStateException("elf did not found themselves")
                if (sum == 1) continue
                changed = true

                rr@for (rr in adj.indices) {
                    val (ox, oy, rx, ry) = adj[(r + rr) % 4]
                    for (oyy in ry) for (oxx in rx)
                        if (map[y + oyy][x + oxx] == '#') continue@rr
                    count[y + oy][x + ox]++
                    break
                }
            }

            for ((x, y) in elves) {
                var sum = 0
                for (oy in -1..1) for (ox in -1..1)
                    if (map[y + oy][x + ox] == '#') sum++
                if (sum == 0) throw IllegalStateException("elf did not found themselves")
                if (sum == 1) {
                    elvesTo.add(Elf(x, y))
                    continue
                }

                var added = false
                rr@for (rr in adj.indices) {
                    val (ox, oy, rx, ry) = adj[(r + rr) % 4]
                    for (oyy in ry) for (oxx in rx)
                        if (map[y + oyy][x + oxx] == '#') continue@rr
                    if (count[y + oy][x + ox] == 1) {
                        elvesTo.add(Elf(x + ox, y + oy))
                        added = true
                    }
                    break
                }
                if (added) continue
                elvesTo.add(Elf(x, y))
            }

            for ((x, y) in elves) map[y][x] = '.'
            for ((x, y) in elvesTo) map[y][x] = '#'

            elves = elvesTo.also { elvesTo = elves }
            elvesTo.clear()

            r++
        }

        println(r)
    }
}