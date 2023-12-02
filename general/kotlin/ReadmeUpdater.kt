package general

import java.io.File

enum class Language(val logo: String, val getFile: (Day) -> String, val methods: Pair<String, String>) {
    KOTLIN("https://kotlinlang.org/assets/images/favicon.svg", { "kotlin/${it.javaClass.simpleName}.kt" }, "fun a1" to "fun a2"),
    HASKELL("https://www.haskell.org/img/favicon.ico", { "haskell/day${it.dayS}.hs" }, "a1 :: IO" to "a2 :: IO"),
}

fun main(args: Array<String>) {
    val days = ArrayList<Day>()
    for (i in 1..25)
        try {
            val id = if (i < 10) "0$i" else "$i"
            val clazz = Class.forName("y2023.Day$id")
            val field = clazz.getDeclaredField("INSTANCE")
            days += field.get(clazz) as Day
        } catch (e: ClassNotFoundException) { break }
    createTable(days)
}

private fun createTable(days: List<Day>) {

    var content = File("README.md").readText().replace("\r\n", "\n")
    val before = content.substringBefore("\n\n") + "\n"
    val after = "\n" + content.substringAfter("\n\n").substringAfter("\n\n")

    content = "$before\n"
    content += "| Day | | 1. Star | 2. Star |\n"
    content += "| ---: | :--- | ---: | ---: |\n"

    days.forEach { day ->
        val name = "[${day.name}](https://adventofcode.com/${day.year}/day/${day.day})"
        var links1 = ""
        var links2 = ""

        for (lang in Language.values()) {
            val (l1, l2) = find(lang, day)
            val format = "[<img src=\"${lang.logo}\" width=\"32\" height=\"32\"/>]("
            if (l1 != null) links1 += " $format$l1)"
            if (l2 != null) links2 += " $format$l2)"
        }

        content += "| ${day.day} | $name | $links1 | $links2 |\n"
    }

    content += after
    File("README.md").writeText(content)
}

private fun find(lang: Language, day: Day): Pair<String?, String?> {
    val path = "y${day.year}/${lang.getFile(day)}"
    val file = File(path)
    if (!file.exists()) return Pair(null, null)
    val content = file.readText()

    val f1 = content.indexOf(lang.methods.first)
    val f2 = content.indexOf(lang.methods.second)

    val lines: (Int) -> Int = { f -> content.substring(0, f).count { it == '\n' } + 1 }
    val transform: (Int) -> String? = { f -> if (f == -1) null else "$path#L${lines(f)}" }

    return Pair(transform(f1), transform(f2))
}