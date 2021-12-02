package general

import y2021.*
import java.io.File

fun main(args: Array<String>) {
    val y2021 = arrayOf(
        Day01, Day02)

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
    val after = content.substringAfter(delimiter).substringAfter("\n\n")
    content = content.substringBefore(delimiter)
    content += delimiter + "\n"
    content += "| Day | | a1 | a2 | a1+a2 |\n"
    content += "| ---: | :--- | ---: | ---: | ---: |\n"

    days.forEach { day ->
        println()
        println("Day ${day.day} - ${day.name}")

        val timings = LongArray(2)
        day.a1() ; repeat(3) { timings[0] += time(day::a1) }
        day.a2() ; repeat(3) { timings[1] += time(day::a2) }

        val a1 = trim(timings[0] / 3)
        val a2 = trim(timings[1] / 3)
        val a12 = trim((timings[0] + timings[1]) / 3)

        if (day.day == 25) content += "| ${day.day} | ${day.name} | $a1 ms | / ms | $a12 ms |\n"
        else content += "| ${day.day} | ${day.name} | $a1 ms | $a2 ms | $a12 ms |\n"
    }

    File("README.md").writeText(content + "\n" + after)
}