package y2020

import java.io.File

fun main(args: Array<String>) {
    val map = mapOf(
            "1" to arrayOf(d01::a1, d01::a2),
            "2" to arrayOf(d02::a1, d02::a2),
            "3" to arrayOf(d03::a1, d03::a2),
            "4" to arrayOf(d04::a1, d04::a2),
            "5" to arrayOf(d05::a1, d05::a2),
            "6" to arrayOf(d06::a1, d06::a2),
            "7" to arrayOf(d07::a1, d07::a2),
            "8" to arrayOf(d08::a1, d08::a2),
            "9" to arrayOf(d09::a1, d09::a2),
            "10" to arrayOf(d10::a1, d10::a2),
            "11" to arrayOf(d11::a1, d11::a2),
            "12" to arrayOf(d12::a1, d12::a2),
            "13" to arrayOf(d13::a1, d13::a2),
            "14" to arrayOf(d14::a1, d14::a2),
            "15" to arrayOf(d15::a1, d15::a2),
            "16" to arrayOf(d16::a1, d16::a2),
    )

    val time = { func: () -> Any ->
        val start = System.nanoTime()
        func()
        val stop = System.nanoTime()
        stop - start
    }

    val trim = { nanoTime: Long ->
        (nanoTime + 50000) / 100000 / 10.0
    }

    var content = ""
    content += "| Day | a1 | a2 | a1+a2 |\n"
    content += "| :---: | ---: | ---: | ---: |\n"

    for ((day, funcs) in map.entries) {
        println("\nday $day")

        funcs[0]() // make functions faster ;)
        val a1 = time(funcs[0])
        val a2 = time(funcs[1])

        content += "| $day | ${trim(a1)} ms | ${trim(a2)} ms | ${trim(a1+a2)} ms |\n"
    }

    File("timings.md").writeText(content)
}