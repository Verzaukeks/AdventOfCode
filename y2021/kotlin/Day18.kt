package y2021

import general.Day
import general.indexOf
import kotlin.math.max

object Day18 : Day() {
    override val name = "Snailfish"

    private class CharReader(private val string: String) {
        private var index = 0
        fun read() = string[index++]
        fun readNumber(reset: Int = 0): Int {
            val start = index - reset
            val end = string.indexOf(start, ',', '[', ']')
            val num = string.substring(start, end)

            index = end
            return num.toInt()
        }
    }

    private enum class Position { LEFT, RIGHT }
    private abstract class Element(var parent: Element? = null, var position: Position = Position.LEFT) {
        abstract fun magnitude(): Int
        abstract fun clone(): Element
    }
    private class Number(var number: Int) : Element() {
        override fun magnitude() = number
        override fun clone() = Number(number)
    }
    private class Pair(var left: Element, var right: Element, var depth: Int) : Element() {
        init {
            left.parent = this
            right.parent = this
            left.position = Position.LEFT
            right.position = Position.RIGHT
        }
        fun increaseDepth() {
            depth += 1
            if (left is Pair) (left as Pair).increaseDepth()
            if (right is Pair) (right as Pair).increaseDepth()
        }
        fun insert(element: Element, position: Position) =
            when (position) {
                Position.LEFT -> {
                    left = element
                    element.parent = this
                    element.position = Position.LEFT
                }
                Position.RIGHT -> {
                    right = element
                    element.parent = this
                    element.position = Position.RIGHT
                }
            }
        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
        override fun clone() = Pair(left.clone(), right.clone(), depth).also {
            left.parent = it
            left.position = Position.LEFT
            right.parent = it
            right.position = Position.RIGHT
        }
    }

    private fun interpret(reader: CharReader, depth: Int): Element {

        if (reader.read() == '[') {
            val left = interpret(reader, depth + 1)
            if (reader.read() != ',') throw Error()

            val right = interpret(reader, depth + 1)
            if (reader.read() != ']') throw Error()

            return Pair(left, right, depth)
        }

        val number = reader.readNumber(1)
        return Number(number)
    }

    override fun a1() {
        val input = INPUT.readLines()
        var main = interpret(CharReader(input[0]), 1) as Pair

        for (i in 1 until input.size) {
            val add = interpret(CharReader(input[i]), 2)

            main.increaseDepth()
            main = Pair(main, add, 1)

            reduce(main)
        }

        println(main.magnitude())
    }

    private fun reduce(element: Element) {
        while (true) {
            if (explode(element)) continue
            if (split(element)) continue
            return
        }
    }

    private fun explode(element: Element): Boolean {
        if (element !is Pair) return false

        if (explode(element.left)) return true
        if (explode(element.right)) return true

        if (element.depth < 5) return false
        if (element.left !is Number || element.right !is Number) return false


        addLeft(element, true, (element.left as Number).number, false)
        addRight(element, true, (element.right as Number).number, false)

        val parent = (element.parent as Pair)
        parent.insert(Number(0), element.position)

        return true
    }

    private fun split(element: Element): Boolean {

        if (element is Pair) {
            if (split(element.left)) return true
            if (split(element.right)) return true
        }

        if (element !is Number) return false
        if (element.number < 10) return false


        val parent = (element.parent as Pair)

        val left = Number(element.number / 2)
        val right = Number(left.number + (element.number and 1))
        val depth = parent.depth + 1

        val pair = Pair(left, right, depth)
        parent.insert(pair, element.position)

        return true
    }

    private fun addLeft(scope: Pair, currentLeft: Boolean, number: Int, goingUp: Boolean) {
        if (goingUp) {
            if (scope.right is Number) (scope.right as Number).number += number
            if (scope.right is Pair) addLeft(scope.right as Pair, false, number, true)
        }
        else if (currentLeft) {
            val parent = (scope.parent ?: return) as Pair
            addLeft(parent, scope.position == Position.LEFT, number, false)
        }
        else {
            if (scope.left is Number) (scope.left as Number).number += number
            if (scope.left is Pair) addLeft(scope.left as Pair, false, number, true)
        }
    }

    private fun addRight(scope: Pair, currentRight: Boolean, number: Int, goingUp: Boolean) {
        if (goingUp) {
            if (scope.left is Number) (scope.left as Number).number += number
            if (scope.left is Pair) addRight(scope.left as Pair, false, number, true)
        }
        else if (currentRight) {
            val parent = (scope.parent ?: return) as Pair
            addRight(parent, scope.position == Position.RIGHT, number, false)
        }
        else {
            if (scope.right is Number) (scope.right as Number).number += number
            if (scope.right is Pair) addRight(scope.right as Pair, false, number, true)
        }
    }

    override fun a2() {
        val inputs = INPUT.readLines().map { interpret(CharReader(it), 2) }
        var maxMag = 0

        for (i in inputs.indices)
            for (ii in inputs.indices)
                if (i != ii) {

                    val x = inputs[i].clone()
                    val y = inputs[ii].clone()

                    val sum = Pair(x, y, 1)
                    reduce(sum)

                    val mag = sum.magnitude()
                    maxMag = max(mag, maxMag)
                }

        println(maxMag)
    }
}