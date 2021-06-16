package y2019

import general.Day
import java.lang.Integer.max
import kotlin.system.exitProcess

object Day15 : Day() {
    override val name = "Oxygen System"

    class Map {
        private var offsetX = 0
        private var offsetY = 0
        private val data = ArrayList<ArrayList<Char>>()

        fun set(x: Int, y: Int, char: Char) {
            var finalY = y + offsetY
            var finalX = x + offsetX

            while (finalY < 0) {
                data.add(0, ArrayList())
                offsetY += 1
                finalY += 1
            }
            while (finalX < 0) {
                data.forEach { it.add(0, ' ') }
                offsetX += 1
                finalX += 1
            }

            while (finalY >= data.size) data += ArrayList<Char>()
            while (finalX >= data[finalY].size) data[finalY] += ' '

            data[finalY][finalX] = char
        }

        fun get(x: Int, y: Int): Char {
            val finalY = y + offsetY
            val finalX = x + offsetX

            if (finalY < 0 || finalX < 0) return ' '
            if (finalY >= data.size) return ' '
            if (finalX >= data[finalY].size) return ' '

            return data[finalY][finalX]
        }

        fun clear() {
            for (y in data.indices) for (x in data[y].indices) data[y][x] = ' '
        }

        override fun toString() = data.joinToString("\n") { it.joinToString("") { "$it$it" } }
    }

    override fun a1() {
        val map = Map()
        val route = ArrayList<Pair<Int, Int>>()

        var posX = 0
        var posY = 0
        var moveX = 0
        var moveY = 0
        var retrace = false

        var skips = 0
        map.set(posX, posY, 'D')

        Day11.intCodeProgram(
            dataFile = INPUT,
            input = {
                     if (map.get(posX + 0, posY - 1) == ' ') { moveX =  0 ; moveY = -1 ; 1L }
                else if (map.get(posX + 0, posY + 1) == ' ') { moveX =  0 ; moveY =  1 ; 2L }
                else if (map.get(posX - 1, posY + 0) == ' ') { moveX = -1 ; moveY =  0 ; 3L }
                else if (map.get(posX + 1, posY + 0) == ' ') { moveX =  1 ; moveY =  0 ; 4L }
                else {
                    val (rx, ry) = route.removeLast()
                    moveX = -rx ; moveY = -ry
                    retrace = true
                         if (moveX ==  0 && moveY == -1) 1L
                    else if (moveX ==  0 && moveY ==  1) 2L
                    else if (moveX == -1 && moveY ==  0) 3L
                    else if (moveX ==  1 && moveY ==  0) 4L
                    else throw Exception("No retrace possible")
                }
            },
            output = {
                if (it == 0L) {
                    map.set(posX + moveX, posY + moveY, '#')
                } else {
                    map.set(posX, posY, '.')
                    posX += moveX
                    posY += moveY
                    map.set(posX, posY, 'D')
                    if (retrace) retrace = false
                    else route += Pair(moveX, moveY)
                }

                if (it == 2L || skips++ % 10 == 0) {
                    ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
                    println(map)
                }

                if (it == 2L) {
                    println("Oxygen System reached.")
                    println("${route.size} steps taken.")
                    exitProcess(0)
                }
            }
        )
    }

    override fun a2() {
        val map = Map()
        val route = ArrayList<Pair<Int, Int>>()

        var posX = 0
        var posY = 0
        var moveX = 0
        var moveY = 0
        var retrace = false

        var skips = 0
        var maxDistance = 0
        var oxygenSpreading = false
        map.set(posX, posY, 'D')

        Day11.intCodeProgram(
            dataFile = INPUT,
            input = {
                if (map.get(posX + 0, posY - 1) == ' ') { moveX =  0 ; moveY = -1 ; 1L }
                else if (map.get(posX + 0, posY + 1) == ' ') { moveX =  0 ; moveY =  1 ; 2L }
                else if (map.get(posX - 1, posY + 0) == ' ') { moveX = -1 ; moveY =  0 ; 3L }
                else if (map.get(posX + 1, posY + 0) == ' ') { moveX =  1 ; moveY =  0 ; 4L }
                else {
                    val trace = route.removeLastOrNull()

                    if (trace == null) {
                        if (oxygenSpreading) {
                            ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
                            println(map)

                            println("$maxDistance steps needed to reach every nook and cranny.")
                            exitProcess(0)
                        }
                        throw Exception("No retrace possible")
                    }

                    moveX = -trace.first ; moveY = -trace.second
                    retrace = true
                    if (moveX ==  0 && moveY == -1) 1L
                    else if (moveX ==  0 && moveY ==  1) 2L
                    else if (moveX == -1 && moveY ==  0) 3L
                    else if (moveX ==  1 && moveY ==  0) 4L
                    else throw Exception("No retrace possible")
                }
            },
            output = {
                if (it == 0L) {
                    map.set(posX + moveX, posY + moveY, '#')
                } else {
                    map.set(posX, posY, '.')
                    posX += moveX
                    posY += moveY
                    map.set(posX, posY, 'D')
                    if (retrace) retrace = false
                    else route += Pair(moveX, moveY)

                    maxDistance = max(maxDistance, route.size)
                }

                if (skips++ % 10 == 0) {
                    ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
                    println(map)
                }

                if (it == 2L && !oxygenSpreading) {
                    oxygenSpreading = true

                    map.clear()
                    route.clear()
                    maxDistance = 0
                }
            }
        )
    }
}