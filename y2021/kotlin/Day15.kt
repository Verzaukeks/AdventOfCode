package y2021

import general.Day
import java.util.*

object Day15 : Day() {
    override val name = "Chiton"

    data class Pos(var x: Int, var y: Int, var risk: Int) {
        override fun equals(other: Any?) = other != null && other is Pos && other.x == x && other.y == y
        override fun hashCode() = 31 * x + y
    }

    override fun a1() = a(1) { map, x, y -> map[y][x] }

    override fun a2() = a(5) { map, x, y ->
        val height = map.size
        val width = map[0].size

        val origin = map[y % height][x % width]
        val addY = y / height
        val addX = x / width

        val value = (origin + addX + addY)
        /*return*/ (value - 1) % 9 + 1
    }

    private fun a(
        scale: Int,
        getRisk: (map: List<List<Int>>, x: Int, y: Int) -> Int,
    ) {
        val map = INPUT.readLines().map { it.toCharArray().map { it - '0' } }

        val width = map[0].size * scale
        val height = map.size * scale
        var pos = Pos(0, 0, 0)

        val locations = ArrayList<Pos>()
        val visited = Array(height) { BooleanArray(width) { false } }
        val dxyList = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))

        locations += pos

        while (locations.isNotEmpty()) {
            pos = locations.removeLast()

            if (pos.x == width - 1 && pos.y == height - 1) break
            visited[pos.y][pos.x] = true

            dx@for ((dx, dy) in dxyList) {
                val nx = pos.x + dx
                val ny = pos.y + dy
                val nPos = Pos(nx, ny, pos.risk)

                if (nx < 0 || nx >= width) continue
                if (ny < 0 || ny >= height) continue
                if (visited[ny][nx]) continue
                if (nPos in locations) continue

                nPos.risk += getRisk(map, nx, ny)

                for (index in locations.indices)
                    if (locations[index].risk < nPos.risk) {
                        locations.add(index, nPos)
                        continue@dx
                    }

                locations += nPos
            }
        }

        println(pos.risk)
    }
}