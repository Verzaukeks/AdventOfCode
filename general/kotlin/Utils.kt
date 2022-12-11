package general

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