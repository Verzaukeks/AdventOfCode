package general

import y2021.*
import java.io.File
import java.io.OutputStream
import java.io.PrintStream

fun main(args: Array<String>) {
    val y2021 = arrayOf(
        Day01, Day02, Day03, Day04, Day05,
        Day06, Day07, Day08, Day09, Day10,
        Day11, Day12, Day13, Day14, Day15,
        Day16, Day17, Day18, Day19, Day20,
        Day21, Day22)

    recordTimes(y2021)
}

private fun recordTimes(days: Array<Day>) {
    val time = { func: () -> Any ->
        val start = System.nanoTime() ; func()
        val stop = System.nanoTime() ; stop - start
    }
    val trim = { nanoTime: Long -> (nanoTime + 50000) / 100000 / 10.0 }

    val delimiter = "### Possible Timings"
    var content = File("README.md").readText()

    var after = content.substringAfter(delimiter)
    val i1 = after.indexOf("\n\n")
    val i2 = after.indexOf("\n\r\n")
    if (i1 != -1) after = after.substring(i1 + 2)
    else if (i2 != -1) after = after.substring(i2 + 3)

    content = content.substringBefore(delimiter)
    content += delimiter + "\n"
    content += "| Day | | a1 | a2 | a1+a2 |\n"
    content += "| ---: | :--- | ---: | ---: | ---: |\n"

    System.setOut(PrintStream(object : OutputStream() {
        override fun write(b: Int) {}
    }))

    days.forEach { day ->
//        println("Day ${day.day} - ${day.name}")

        val timings = LongArray(2)
        day.a1() ; repeat(3) { timings[0] += time(day::a1) }
        day.a2() ; repeat(3) { timings[1] += time(day::a2) }

        val a1 = trim(timings[0] / 3)
        val a2 = trim(timings[1] / 3)
        val a12 = trim((timings[0] + timings[1]) / 3)

        val name = "[${day.name}](https://adventofcode.com/${day.year}/day/${day.day})"
        val link = "y${day.year}/kotlin/${day.javaClass.simpleName}.kt"
        val (f1, f2) = find(day)

        val link1 = "[$a1 ms]($link#L$f1)"
        val link2 = "[$a2 ms]($link#L$f2)"
        val link12 = "[$a12 ms]($link)"

        content += "| ${day.day} | $name | $link1 | $link2 | $link12 |\n"
    }

    File("README.md").writeText(content + "\n" + after)
}

private fun find(day: Day): Pair<Int, Int> {
    val file = File("y${day.year}/kotlin/${day.javaClass.simpleName}.kt")
    val content = file.readText()

    val f1 = content.indexOf("fun a1")
    val f2 = content.indexOf("fun a2")

    return Pair(
        content.substring(0, f1).count { it == '\n' } + 1,
        content.substring(0, f2).count { it == '\n' } + 1,
    )
}