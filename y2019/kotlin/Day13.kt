package y2019

import general.Day
//import lc.kra.system.keyboard.GlobalKeyboardHook
//import lc.kra.system.keyboard.event.GlobalKeyEvent
//import lc.kra.system.keyboard.event.GlobalKeyListener

object Day13 : Day() {
    override val name = "Care Package"

    override fun a1() {
        val blockTiles = ArrayList<String>()

        var x = 0
        var y = 0
        var outputState = 0

        Day11.intCodeProgram(
            dataFile = INPUT,
            output = {
                when (outputState++) {
                    0 -> x = it.toInt()
                    1 -> y = it.toInt()
                    2 -> {
                        if (it == 2L) {
                            val pos = "$x,$y"
                            if (pos !in blockTiles)
                                blockTiles += pos
                        }
                        outputState = 0
                    }
                }
            }
        )

        println(blockTiles.size)
    }

//    class Display {
//        private val data = ArrayList<ArrayList<Char>>()
//
//        init {
//            ProcessBuilder("cmd", "/c", "mode con COLS=48 LINES=25").inheritIO().start().waitFor()
//        }
//
//        fun set(x: Int, y: Int, char: Char) {
//            while (y >= data.size) data += ArrayList<Char>()
//            while (x >= data[y].size) data[y] += ' '
//            data[y][x] = char
//        }
//
//        fun print() {
////            ProcessBuilder("clear").inheritIO().start().waitFor()
//            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
//
//            println(data
//                .joinToString("\n") {
//                    it.joinToString("") { "$it" }
//                })
//        }
//    }

    override fun a2() {
        /* Just some SystemHook stuff to catch global key presses for joystick input */
//        val hook = GlobalKeyboardHook()
//
//        var isLeftKeyDown = false
//        var isRightKeyDown = false
//
//        hook.addKeyListener(object : GlobalKeyListener {
//            override fun keyPressed(e: GlobalKeyEvent) {
//                when (e.virtualKeyCode) {
//                    GlobalKeyEvent.VK_LEFT -> isLeftKeyDown = true
//                    GlobalKeyEvent.VK_RIGHT -> isRightKeyDown = true
//                }
//            }
//            override fun keyReleased(e: GlobalKeyEvent) {
//                when (e.virtualKeyCode) {
//                    GlobalKeyEvent.VK_LEFT -> isLeftKeyDown = false
//                    GlobalKeyEvent.VK_RIGHT -> isRightKeyDown = false
//                }
//            }
//        })
        /* */

//        val display = Display()

        var cursorX = 0
        var ballX = 0

        var x = 0
        var y = 0
        var score = 0L
        var outputState = 0

        Day11.intCodeProgram(
            dataFile = INPUT,
            input = {
//                display.print()
//                print("Score: $score")

//                Thread.sleep(50)

//                (if (isLeftKeyDown) -1L else 0L) + (if (isRightKeyDown) 1L else 0L)
                if (cursorX < ballX) 1L
                else if (cursorX > ballX) -1L
                else 0L
            },
            output = {
                when (outputState++) {
                    0 -> x = it.toInt()
                    1 -> y = it.toInt()
                    2 -> {
                        if (x == -1 && y == 0)
                            score = it
                        else
//                            display.set(x, y, when (it) {
//                                0L -> ' '
//                                1L -> '#'
//                                2L -> 'O'
//                                3L -> { cursorX = x ; '=' }
//                                4L -> { ballX = x ; '.' }
//                                else -> '?'
//                            })
                            when (it) {
                                3L -> cursorX = x
                                4L -> ballX = x
                            }
                        outputState = 0
                    }
                }
            },
            onLoad = {
                it[0] = 2
            }
        )

        println("Final Score: $score")
        // SystemHook cleanup
//        hook.shutdownHook()
    }
}