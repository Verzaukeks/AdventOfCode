package y2022

import general.Day
import java.util.*

object Day07 : Day() {
    override val name = "No Space Left On Device"

    private fun read(): Pair<Set<String>, List<Pair<Int, String>>> {
        val dirs = TreeSet<String>()
        val files = ArrayList<Pair<Int, String>>()

        var cwd = ""
        for (line in INPUT.readLines()) {
            if (line == "$ cd /") cwd = ""
            else if (line == "$ cd ..") cwd = cwd.substringBeforeLast("/", "")
            else if (line.startsWith("$ cd ")) cwd += "/" + line.substring(5)
            else if (line.startsWith("$ ls")) continue
            else if (line.startsWith("dir ")) continue
            else {
                val size = line.split(" ")[0].toInt()
                val name = line.split(" ")[1]
                files.add(Pair(size, "$cwd/$name"))
            }
            dirs.add("$cwd/")
        }

        return Pair(dirs, files)
    }

    override fun a1() {
        val (dirs, files) = read()

        println(dirs.sumOf { dir ->
            val size = files.filter { it.second.startsWith(dir) }.sumOf { it.first }
            if (size <= 100000) size else 0
        })
    }

    override fun a2() {
        val (dirs, files) = read()

        val removeAtLeast = 30000000 - (70000000 - files.sumOf { it.first })

        println(dirs.minOf { dir ->
            val size = files.filter { it.second.startsWith(dir) }.sumOf { it.first }
            if (size >= removeAtLeast) size else 0x42424242
        })
    }
}