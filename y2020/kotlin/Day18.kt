package y2020

import general.Day

object Day18 : Day() {
    override val name = "Operation Order"

    override fun a1() {
        val input = INPUT.readLines()
        val sum = input.sumOf { parseA1(it) }
        println(sum)
    }

    private fun parseA1(_content: String): Long {
        var content = _content

        // as long as parentheses exist, take the first parenthesis that does not contain parentheses itself and evaluate it
        var regex = """\([^()]+\)""".toRegex()
        while (true) {
            val match = regex.find(content) ?: break
            val value = parseA1(match.value.substring(1, match.value.length-1))
            content = content.replaceRange(match.range, "$value")
        }

        // number +/* number          +/* indifferent, first come first serve
        regex = """\d+.[+*].\d+""".toRegex()
        while (true) {
            val match = regex.find(content) ?: break
            val split = match.value.split(" + ", " * ")

            when {
                "+" in match.value -> {
                    val value = split[0].toLong() + split[1].toLong()
                    content = content.replaceRange(match.range, "$value")
                }
                "*" in match.value -> {
                    val value = split[0].toLong() * split[1].toLong()
                    content = content.replaceRange(match.range, "$value")
                }
            }
        }

        return content.toLong()
    }

    override fun a2() {
        val input = INPUT.readLines()
        val sum = input.sumOf { parseA2(it) }
        println(sum)
    }

    private fun parseA2(_content: String): Long {
        var content = _content

        // as long as parentheses exist, take the first parenthesis that does not contain parentheses itself and evaluate it
        var regex = """\([^()]+\)""".toRegex()
        while (true) {
            val match = regex.find(content) ?: break
            val value = parseA2(match.value.substring(1, match.value.length-1))
            content = content.replaceRange(match.range, "$value")
        }

        // number + number          +/* not indifferent, now + afterwards *
        regex = """\d+.\+.\d+""".toRegex()
        while (true) {
            val match = regex.find(content) ?: break
            val split = match.value.split(" + ")

            val value = split[0].toLong() + split[1].toLong()
            content = content.replaceRange(match.range, "$value")
        }

        // number * number          +/* not indifferent, before + now *
        regex = """\d+.\*.\d+""".toRegex()
        while (true) {
            val match = regex.find(content) ?: break
            val split = match.value.split(" * ")

            val value = split[0].toLong() * split[1].toLong()
            content = content.replaceRange(match.range, "$value")
        }

        return content.toLong()
    }

}