package general

import java.io.File
import java.net.URI
import java.nio.file.Files

abstract class Day {

    val year = javaClass.packageName.substring(1).toInt()
    val day = javaClass.simpleName.substring(3).toInt()
    val dayS: String ; get() = if (day < 10) "0$day" else "$day"
    val INPUT by lazy {
        val file = File("y$year/inputs/d$dayS")
        if (!file.exists() || file.length() == 0L) downloadFile(file)
        file
    }

    abstract fun a1()
    abstract fun a2()

    private fun downloadFile(file: File) {
        val agent = System.getenv("aoc-agent") ?: return
        val session = System.getenv("aoc-session") ?: return

        val url = URI("https://adventofcode.com/$year/day/$day/input").toURL()
        val conn = url.openConnection()

        conn.setRequestProperty("User-Agent", agent)
        conn.setRequestProperty("Cookie", "session=$session")

        conn.getInputStream().use {
            if (file.exists()) file.delete()
            Files.copy(it, file.toPath())
        }
    }

}