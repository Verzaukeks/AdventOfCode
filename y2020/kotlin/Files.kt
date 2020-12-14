package y2020

import java.io.File

object Files {

    operator fun get(path: Int) = get("d${if (path < 10) "0" else ""}$path")
    operator fun get(path: String) = File("y2020/inputs/$path")

}
