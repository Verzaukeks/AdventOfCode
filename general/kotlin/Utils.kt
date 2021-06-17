package general

operator fun String.times(times: Int): String {
    val string = StringBuilder(length * times)
    repeat(times) { string.append(this) }
    return string.toString()
}