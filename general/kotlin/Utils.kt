package general

import kotlin.math.absoluteValue

operator fun String.times(times: Int): String {
    val string = StringBuilder(length * times)
    repeat(times) { string.append(this) }
    return string.toString()
}

fun Iterable<Long>.product(): Long {
    var product = 1L
    for (element in this)
        product *= element
    return product
}

inline fun <T> Array<out T>.productOf(selector: (T) -> Int): Int {
    var product = 1
    for (element in this)
        product *= selector(element)
    return product
}

fun CharSequence.indexOf(startIndex: Int, vararg chars: Char): Int {

    for (index in startIndex until length)
        if (get(index) in chars)
            return index

    return -1
}

fun String.len(len: Int): String {
    if (length < len) return " " + len(len - 1)
    if (length > len) return substring(length - len)
    return this
}

fun gcd(a: Long, b: Long): Long {
    if (a == 0L) return b.absoluteValue
    if (b == 0L) return a.absoluteValue

    var num1 = a
    var num2 = b
    do {
        num1 = num2.also { num2 = num1 % num2 }
    } while (num2 != 0L)

    return num1.absoluteValue
}

fun lcm(a: Long, b: Long): Long {
    return (a / gcd(a, b) * b).absoluteValue
}
