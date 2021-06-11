package y2019

import general.Day

object Day07 : Day() {
    override val name = "Amplification Circuit"

    override fun a1() {
        val data = INPUT.readText().split(",").map { it.toInt() }
        var output = 0

        for (A in 0..4) {
            val a = code1(data, A, 0)

            for (B in 0..4) if (B != A) {
                val b = code1(data, B, a)

                for (C in 0..4) if (C != A && C != B) {
                    val c = code1(data, C, b)

                    for (D in 0..4) if (D != A && D != B && D != C) {
                        val d = code1(data, D, c)

                        for (E in 0..4) if (E != A && E != B && E != C && E != D) {
                            val e = code1(data, E, d)

                            if (e > output)
                                output = e
        }}}}}

        println(output)
    }

    fun code1(data: List<Int>, phase: Int, input: Int): Int {
        val ram = data.toIntArray()
        var ci = 0 // currentInput
        var pc = 0

        while (true) {
            val opcode = ram[pc]
            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10
//            val mode3 = opcode / 10000 % 10

            val value1 by lazy { if (mode1 == 1) ram[pc + 1] else if (mode1 == 0) ram[ram[pc + 1]] else throw Error() }
            val value2 by lazy { if (mode2 == 1) ram[pc + 2] else if (mode2 == 0) ram[ram[pc + 2]] else throw Error() }
//            val value3 by lazy { if (mode3 == 1) ram[pc + 3] else if (mode3 == 0) ram[ram[pc + 3]] else throw Error() }

            when (opcode % 100) {
                1 -> { ram[ram[pc + 3]] = value1 + value2; pc += 4 }
                2 -> { ram[ram[pc + 3]] = value1 * value2; pc += 4 }
                3 -> { when(ci) {
                        0 -> ram[ram[pc + 1]] = phase
                        1 -> ram[ram[pc + 1]] = input
                        else -> throw Error("${opcode % 100}: $pc -> ${ram.joinToString(",")}")
                    } ;  pc += 2 ; ci += 1 }
                4 -> return value1
                5 -> { if (value1 != 0) pc = value2 else pc += 3 }
                6 -> { if (value1 == 0) pc = value2 else pc += 3 }
                7 -> { ram[ram[pc + 3]] = if (value1 < value2) 1 else 0 ; pc += 4 }
                8 -> { ram[ram[pc + 3]] = if (value1 == value2) 1 else 0 ; pc += 4 }
//                99 -> break
                else -> throw Error("${opcode % 100}: $pc -> ${ram.joinToString(",")}")
            }
        }
    }

    override fun a2() {
        val data = INPUT.readText().split(",").map { it.toInt() }
        var output = 0

        for (A in 5..9)
        for (B in 5..9) if (B != A)
        for (C in 5..9) if (C != A && C != B)
        for (D in 5..9) if (D != A && D != B && D != C)
        for (E in 5..9) if (E != A && E != B && E != C && E != D) {
            val stateA = CodeState(A, data.toIntArray()) ; code2(stateA, A, true)
            val stateB = CodeState(B, data.toIntArray()) ; code2(stateB, B, true)
            val stateC = CodeState(C, data.toIntArray()) ; code2(stateC, C, true)
            val stateD = CodeState(D, data.toIntArray()) ; code2(stateD, D, true)
            val stateE = CodeState(E, data.toIntArray()) ; code2(stateE, E, true)

            var value = 0
            while (true) {
                value = code2(stateA, value) ?: break
                value = code2(stateB, value) ?: break
                value = code2(stateC, value) ?: break
                value = code2(stateD, value) ?: break
                value = code2(stateE, value) ?: break
            }

            if (value > output)
                output = value
        }

        println(output)
    }


    class CodeState(var phase: Int?, val ram: IntArray, var pc: Int = 0)

    fun code2(state: CodeState, input: Int, phaseInput: Boolean = false): Int? {
        while (true) {
            val opcode = state.ram[state.pc]
            val mode1 = opcode / 100 % 10
            val mode2 = opcode / 1000 % 10

            val value1 by lazy { if (mode1 == 1) state.ram[state.pc + 1] else if (mode1 == 0) state.ram[state.ram[state.pc + 1]] else throw Error() }
            val value2 by lazy { if (mode2 == 1) state.ram[state.pc + 2] else if (mode2 == 0) state.ram[state.ram[state.pc + 2]] else throw Error() }

            when (opcode % 100) {
                1 -> { state.ram[state.ram[state.pc + 3]] = value1 + value2; state.pc += 4 }
                2 -> { state.ram[state.ram[state.pc + 3]] = value1 * value2; state.pc += 4 }
                3 -> { state.ram[state.ram[state.pc + 1]] = input ; state.pc += 2 ; if (phaseInput) return null }
                4 -> { return value1.also { state.pc += 2 } }
                5 -> { if (value1 != 0) state.pc = value2 else state.pc += 3 }
                6 -> { if (value1 == 0) state.pc = value2 else state.pc += 3 }
                7 -> { state.ram[state.ram[state.pc + 3]] = if (value1 < value2) 1 else 0 ; state.pc += 4 }
                8 -> { state.ram[state.ram[state.pc + 3]] = if (value1 == value2) 1 else 0 ; state.pc += 4 }
                99 -> return null
                else -> throw Error("${opcode % 100}: ${state.pc} -> ${state.ram.joinToString(",")}")
            }
        }
    }
}