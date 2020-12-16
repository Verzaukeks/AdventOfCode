package y2020

import java.io.File

abstract class Day {

    abstract val day: Int
    abstract val name: String
    val INPUT by lazy { File("y2020/inputs/d" + (if (day < 10) "0" else "") + day) }

    abstract fun a1()
    abstract fun a2()

}