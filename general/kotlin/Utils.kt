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