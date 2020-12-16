package y2020

object Day05 : Day() {

    override val day = 5
    override val name = "Binary Boarding"

    override fun a1() {
        val input  = INPUT
                .readText()
                .replace("F", "0")
                .replace("B", "1")
                .replace("L", "0")
                .replace("R", "1")
                .split("\r\n")
                .map { it.toInt(2) }
                .sorted()

        println(input.last())
    }

    override fun a2() {
        val input = INPUT.readText()
                .replace("F", "0")
                .replace("B", "1")
                .replace("L", "0")
                .replace("R", "1")
                .split("\r\n")
                .map { it.toInt(2) }
                .sorted()

        for (index in 1 until input.size)
            if (input[index - 1] == input[index] - 2) {
                println(input[index] - 1)
                return
            }

        println("no seat found")
    }
}