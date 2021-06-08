package y2020

import general.Day

object Day08 : Day() {
    override val name = "Handheld Halting"

    override fun a1() {
        val input = INPUT.readLines()

        runCatching {
            program(input)
        }.onFailure {
            println(it.message)
        }
    }

    // brute force :D
    override fun a2() {
        val input = INPUT.readText().replace("\r", "")

        val unit = { fromOp: String, toOp: String ->
            var i = 0
            a@while (i < input.length) {
                val foundAt = input.indexOf(fromOp, i)
                if (foundAt == -1) break

                val newInput = input.replaceRange(foundAt..foundAt+2, toOp)
                i = newInput.indexOf("\n", foundAt) + 1
                if (i == 0) break // no more \n found

                val r = runCatching {
                    val accu = program(newInput.split("\n"))
                    println("accu = $accu")
                }
                if (r.isSuccess) break@a
            }
        }

        unit("jmp", "nop")
        unit("nop", "jmp")
        //println("nothing found")
    }

    fun program(input: List<String>): Int {
        val run = ArrayList<Int>()
        var prg = 0
        var accu = 0

        while (prg < input.size) {
            if (prg in run)
                error("infinite loop: accu = $accu")
            run += prg

            val line = input[prg]

            val op = line.substringBefore(" ")
            val arg = line.substringAfter(" ").toInt()

            when (op) {
                "nop" -> {
                    prg ++
                }
                "acc" -> {
                    accu += arg
                    prg ++
                }
                "jmp" -> {
                    prg += arg
                    if ((prg < 0 || prg >= input.size) && arg != 1)
                        error("jumped out of boot")
                }
            }
        }

        return accu
    }
}