package general

import java.io.File
import java.io.OutputStream
import java.io.PrintStream

fun main(args: Array<String>) {
    val y2023 = ArrayList<Day>()
    for (i in 1..25)
        try {
            val id = if (i < 10) "0$i" else "$i"
            val clazz = Class.forName("y2023.Day$id")
            val field = clazz.getDeclaredField("INSTANCE")
            y2023 += field.get(clazz) as Day
        } catch (e: ClassNotFoundException) { break }
    createTable(y2023)
}

private fun createTable(days: List<Day>) {

    val delimiter = "| Day | | a1 | a2 |"
    var content = File("README.md").readText()

    var after = content.substringAfter(delimiter)
    val i1 = after.indexOf("\n\n")
    val i2 = after.indexOf("\n\r\n")
    if (i1 != -1) after = after.substring(i1 + 2)
    else if (i2 != -1) after = after.substring(i2 + 3)

    content = content.substringBefore(delimiter)
    content += delimiter + "\n"
    content += "| ---: | :--- | ---: | ---: |\n"

    System.setOut(PrintStream(object : OutputStream() {
        override fun write(b: Int) {}
    }))

    days.forEach { day ->
        val name = "[${day.name}](https://adventofcode.com/${day.year}/day/${day.day})"
        val link = "y${day.year}/kotlin/${day.javaClass.simpleName}.kt"
        val (f1, f2) = find(day)

        val link2 = "[_jump to code_]($link#L$f2)"
        val link1 = "[_jump to code_]($link#L$f1)"

        content += "| ${day.day} | $name | $link1 | $link2 |\n"
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