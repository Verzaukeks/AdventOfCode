package y2022

import general.Day
import java.util.*
import kotlin.math.min

object Day13 : Day() {
    override val name = "Distress Signal"

    private fun readLists() = INPUT.readLines()
        .filter { it.isNotBlank() }
        .map { parse(it) }

    private fun parse(raw: String): List<*> {
        var i = 0
        val stack = Stack<LinkedList<Any>>()
        while (true)
            when (raw[i]) {
                '[' -> {
                    val list = LinkedList<Any>()
                    if (stack.size > 0) stack.peek().add(list)
                    stack.add(list)
                    i++
                }
                ',' -> i++
                ']' -> {
                    if (stack.size == 1) return stack.pop()
                    stack.pop()
                    i++
                }
                else -> {
                    val num = raw.substring(i).substringBefore(",").substringBefore("]")
                    stack.peek().add(num.toInt())
                    i += num.length
                }
            }
    }

    private fun check(a: Any?, b: Any?): Int {
        if (a is Int && b is Int) return a - b
        if (a is List<*> && b is List<*>) {
            for (i in 0 until min(a.size, b.size)) {
                val r = check(a[i], b[i])
                if (r != 0) return r
            }
            return a.size - b.size
        }
        if (a is Int && b is List<*>) return check(listOf(a), b)
        if (a is List<*> && b is Int) return check(a, listOf(b))
        return 0
    }

    override fun a1() {
        val pairs = readLists().chunked(2)
        var index = 1
        var sum = 0

        for ((a, b) in pairs) {
            if (check(a, b) < 0)
                sum += index
            index++
        }

        println(sum)
    }

    override fun a2() {
        val a = parse("[[2]]")
        val b = parse("[[6]]")
        val lists = readLists()
        val ai = lists.count { check(it, a) < 0 } + 1
        val bi = lists.count { check(it, b) < 0 } + 2
        println(ai * bi)

//        val lists = readLists().toMutableList()
//        lists.add(a)
//        lists.add(b)
//        lists.sortWith(::check)
//
//        val ai = lists.indexOf(a) + 1
//        val bi = lists.indexOf(b) + 1
//        println(ai * bi)
    }
}