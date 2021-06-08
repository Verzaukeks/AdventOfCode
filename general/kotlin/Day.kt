package general

import java.io.File

abstract class Day {

    abstract val name: String
    val year = javaClass.packageName.substring(1).toInt()
    val day = javaClass.simpleName.substring(3).toInt()
    val INPUT by lazy { File("y$year/inputs/d" + (if (day < 10) "0" else "") + day) }

    abstract fun a1()
    abstract fun a2()

}