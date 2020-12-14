package y2020

object d14 {
    fun a1() {
        val input = Files[14].readLines()

        val memory = HashMap<Long, Long>()
        var maskAndZeros = 0L
        var maskOrOnes = 0L

        for (command in input) {
            val args = command.split(" = ")

            if (args[0] == "mask") {
                maskAndZeros = args[1].replace("X", "1").toLong(2)
                maskOrOnes = args[1].replace("X", "0").toLong(2)

            } else {

                val location = args[0].substringAfter("[").substringBefore("]").toLong()
                val number = args[1].toLong() and maskAndZeros or maskOrOnes
                memory[location] = number
            }
        }

        println(memory.values.sum())
    }

    fun a2() {
        val input = Files[14].readLines()

        val memory = HashMap<Long, Long>()
        val maskFloating = ArrayList<Int>()
        var maskAndZeros = 0L
        var maskOrOnes = 0L

        for (command in input) {
            val args = command.split(" = ")

            if (args[0] == "mask") {
                val mask = args[1]
                maskAndZeros = mask.replace("0", "1").replace("X", "0").toLong(2)
                maskOrOnes = mask.replace("X", "0").toLong(2)

                maskFloating.clear()
                for (i in mask.indices)
                    if (mask[i] == 'X')
                        maskFloating += mask.length - i - 1

            } else {

                var location = args[0].substringAfter("[").substringBefore("]").toLong()
                val number = args[1].toLong()
                location = location and maskAndZeros or maskOrOnes

                for (combination in 0 until (1 shl maskFloating.size)) {
                    var loc = location

                    for (i in maskFloating.indices)
                        if (combination shr i and 1 != 0)
                            loc = loc or (1L shl maskFloating[i]) // you want to shift a long! not an integer! >:(

                    memory[loc] = number
                }
            }
        }

        println(memory.values.sum())
    }
}