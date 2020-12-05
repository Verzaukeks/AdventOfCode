package y2020.inputs

import java.io.File

object Files {

    operator fun get(path: Int) = get("d$path")
    operator fun get(path: String) = File("src/main/kotlin/y2020/inputs/$path")

}
