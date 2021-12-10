package y2021

import general.Day

object Day10 : Day() {
    override val name = "Syntax Scoring"

    override fun a1() {
        val input = INPUT.readLines()
        var score = 0

        val sequence = CharArray(128)
        var index: Int

        for (line in input) {
            index = 0

            for (c in line.toCharArray())
                when (c) {
                    '(' -> sequence[index++] = '('
                    '[' -> sequence[index++] = '['
                    '{' -> sequence[index++] = '{'
                    '<' -> sequence[index++] = '<'
                    ')' -> {
                        if (sequence[--index] == '(') continue
                        score += 3 ; break
                    }
                    ']' -> {
                        if (sequence[--index] == '[') continue
                        score += 57 ; break
                    }
                    '}' -> {
                        if (sequence[--index] == '{') continue
                        score += 1197 ; break
                    }
                    '>' -> {
                        if (sequence[--index] == '<') continue
                        score += 25137 ; break
                    }
                    else -> throw Error("Unexpected character given: '$c'")
                }
        }

        println(score)
    }

    override fun a2() {
        val input = INPUT.readLines()
        val scores = ArrayList<Long>()

        val sequence = CharArray(128)
        var index: Int

        line@for (line in input) {
            index = 0

            // sort out corrupted lines
            for (c in line.toCharArray())
                when (c) {
                    '(' -> sequence[index++] = '('
                    '[' -> sequence[index++] = '['
                    '{' -> sequence[index++] = '{'
                    '<' -> sequence[index++] = '<'
                    ')' -> if (sequence[--index] != '(') continue@line
                    ']' -> if (sequence[--index] != '[') continue@line
                    '}' -> if (sequence[--index] != '{') continue@line
                    '>' -> if (sequence[--index] != '<') continue@line
                    else -> throw Error("Unexpected character given: '$c'")
                }

            // from here on the lines are only incomplete
            var score = 0L

            while (index > 0)
                score = when (sequence[--index]) {
                    '(' -> score * 5 + 1
                    '[' -> score * 5 + 2
                    '{' -> score * 5 + 3
                    '<' -> score * 5 + 4
                    else -> throw Error("Unexpected character given: '${sequence[index]}'")
                }

            scores += score
        }

        scores.sort()
        println(scores[scores.size / 2])
    }
}