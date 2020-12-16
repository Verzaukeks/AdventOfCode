package y2020

object Day15 : Day() {

    override val day = 15
    override val name = "Rambunctious Recitation"

    override fun a1() {
        val input = INPUT.readText().split(",").map { it.toInt() }

        val array = ArrayList<Int>()
        array += input

        for (i in array.size-1 until (2020 - 1)) {
            val ai = array[i]
            var n = 0

            for (k in array.size-2 downTo 0)
                if (array[k] == ai) {
                    n = i - k
                    break
                }

            array += n
        }

        println(array.last())
    }

    override fun a2() {
        val input = INPUT.readText().split(",").map { it.toInt() }

        // gimme that memory
        val map = IntArray(30000000)
        var last = 0

        input.forEachIndexed { index, number -> map[number] = index+1 }

        for (i in input.size until (30000000 - 1)) {

            var x = map[last]
            x = if (x == 0) 0 else i - (x-1)

            map[last] = i+1
            last = x
        }

        println(last)
    }
}