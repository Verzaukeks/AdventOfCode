package y2020

import kotlin.math.max

object d11 {
    fun a1() {
        val masks = ArrayList<ArrayList<IntRange>>()
        masks += ArrayList<IntRange>()

        masks[0].addAll(arrayOf(
                -1..-1, 0..-1, 1..-1,
                -1.. 0,/*seat*/1.. 0,
                -1.. 1, 0.. 1, 1.. 1))

        iterate(masks, 4, true)
    }

    fun a2() {
        val input = Files[11].readText().replace("\r", "")
        val width = input.split("\n")[0].length
        val height = input.split("\n").size
        val masks = ArrayList<ArrayList<IntRange>>()
        repeat(8) { masks += ArrayList<IntRange>() }

        for (t in 1 until max(width, height)) {
            masks[0].add(-t.. 0) // left
            masks[1].add( t.. 0) // right
            masks[2].add( 0..-t) // up
            masks[3].add( 0.. t) // down
            masks[4].add(-t..-t) // left+up
            masks[5].add( t..-t) // right+up
            masks[6].add( t.. t) // right+down
            masks[7].add(-t.. t) // left+down
        }

        iterate(masks, 5, false)
    }

    fun iterate(masks: ArrayList<ArrayList<IntRange>>, tolerance: Int, oneMask: Boolean) {
        val input = Files[11].readText().replace("\r", "")
        var ain = input.replace("\n", "").toCharArray()
        var aout = input.replace("\n", "").toCharArray()

        val width = input.split("\n")[0].length
        val height = input.split("\n").size

        var changed = true
        while (changed) {
            changed = false

            for (i in ain.indices) {
                if (ain[i] == '.') continue

                var count = 0
                val x = i % width
                val y = i / width

                m@for (mask in masks)
                    for (offset in mask) {
                        val nx = x + offset.first
                        val ny = y + offset.last
                        val ni = nx + ny * width

                        if (nx < 0 || nx >=  width) if (oneMask) continue else continue@m
                        if (ny < 0 || ny >= height) if (oneMask) continue else continue@m
                        if (ain[ni] == '#') count ++
                        if (ain[ni] != '.' && !oneMask) continue@m
                    }

                     if (ain[i] == 'L' && count == 0)         { aout[i] = '#' ; changed = true }
                else if (ain[i] == '#' && count >= tolerance) { aout[i] = 'L' ; changed = true }
                else aout[i] = ain[i]
            }

            val tmp = ain
            ain = aout
            aout = tmp

            /*println()
            for (i in ain.indices) {
                if (i % width == 0) println()
                print(ain[i])
            }
            println()*/
        }

        println()
        println(ain.count { it == '#' })
    }
}