package y2020

import general.Day

object Day06 : Day() {
    override val name = "Custom Customs"

    override fun a1() {
        val input = INPUT.readText()
        val questions = ArrayList<Char>()
        var sum = 0

        for (group in input.split("\r\n\r\n")) {
            questions.clear()

            for (person in group.split("\r\n"))
                for (question in person.toCharArray()) {

                    if (question !in questions)
                        questions += question
                }

            sum += questions.size
        }

        println(sum)
    }

    override fun a2() {
        val input = INPUT.readText()
        var sum = 0

        for (group in input.split("\r\n\r\n")) {
            val questions = IntArray('z' - 'a' + 1)
            var people = 0

            for (person in group.split("\r\n")) {
                people++
                for (question in person.toCharArray())
                    questions[question - 'a']++
            }

            for (q in questions)
                if (q == people)
                    sum++
        }

        println(sum)
    }
}