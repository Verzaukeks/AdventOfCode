import java.io.File

enum class Language(val logo: String, val getFile: (String) -> String, val methods: Pair<String, String>) {
    KOTLIN("https://kotlinlang.org/assets/images/favicon.svg", { "kotlin/Day$it.kt" }, "fun a1" to "fun a2"),
    HASKELL("https://www.haskell.org/img/favicon.ico", { "haskell/day$it.hs" }, "a1 :: IO" to "a2 :: IO"),
}

private val yFolder = File("").listFiles()!!.sortedBy { it.name }.last { it.isDirectory && it.name.startsWith("y") }
private val names = File(yFolder.absoluteFile, "names.txt").readLines()

fun main() {
    val sb = StringBuilder()
    for (day in 1..25) {
        val name = names.getOrNull(day) ?: continue
        val prefix = "$day | [$name](https://adventofcode.com/${yFolder.name.substring(1)}/day/$day)"
        var links1 = ""
        var links2 = ""

        for (lang in Language.entries) {
            val (l1, l2) = find(lang, day)
            val format = "[<img src=\"${lang.logo}\" width=\"32\" height=\"32\"/>]("
            if (l1 != null) links1 += " $format$l1)"
            if (l2 != null) links2 += " $format$l2)"
        }

        sb.appendLine("| $prefix | $links1 | $links2 |")
    }
    File("CONTENT.md").writeText(sb.toString())
}

private fun find(lang: Language, day: Int): Pair<String?, String?> {
    val path = yFolder.name + "/" + lang.getFile(if (day < 10) "0$day" else day.toString())
    val file = File(path)
    if (!file.exists()) return Pair(null, null)
    val content = file.readText()

    val f1 = content.indexOf(lang.methods.first)
    val f2 = content.indexOf(lang.methods.second)

    val lines: (Int) -> Int = { f -> content.substring(0, f).count { it == '\n' } + 1 }
    val transform: (Int) -> String? = { f -> if (f == -1) null else "$path#L${lines(f)}" }

    return Pair(transform(f1), transform(f2))
}
