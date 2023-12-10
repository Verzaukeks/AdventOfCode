package y2023

import general.Day

object Day10 : Day() {
    override val name = "Pipe Maze"

    private fun mapping(): Triple<Int, List<CharArray>, Array<BooleanArray>> {
        val map = INPUT.readLines().map { it.toCharArray() }
        val vis = Array(map.size) { BooleanArray(map[0].size) }

        val q = ArrayDeque<Pair<Int, Int>>()
        for (y in map.indices)
            for (x in map[y].indices)
                if (map[y][x] == 'S') {
                    vis[y][x] = true
                    val t = map.getOrNull(y-1)?.get(x) ?: ' '
                    val b = map.getOrNull(y+1)?.get(x) ?: ' '
                    val l = map[y].getOrNull(x-1) ?: ' '
                    val r = map[y].getOrNull(x+1) ?: ' '
                    if (t == '|' || t == '7' || t == 'F') q.add(Pair(y-1, x))
                    if (b == '|' || b == 'L' || b == 'J') q.add(Pair(y+1, x))
                    if (l == '-' || l == 'L' || l == 'F') q.add(Pair(y, x-1))
                    if (r == '-' || r == '7' || r == 'J') q.add(Pair(y, x+1))
                    for ((ny, nx) in q) vis[ny][nx] = true
                }

        var steps = 2
        while (!q.isEmpty()) {
            val (y, x) = q.removeFirst(); steps++
            val add = when (map[y][x]) {
                '|' -> listOf(-1 to 0, 1 to 0)
                '-' -> listOf(0 to -1, 0 to 1)
                'L' -> listOf(-1 to 0, 0 to 1)
                'J' -> listOf(-1 to 0, 0 to -1)
                '7' -> listOf(1 to 0, 0 to -1)
                'F' -> listOf(1 to 0, 0 to 1)
                else -> listOf()
            }
            for ((ay, ax) in add) {
                if (y + ay !in map.indices) continue
                if (x + ax !in map[y + ay].indices) continue
                if (vis[y + ay][x + ax]) continue
                vis[y + ay][x + ax] = true
                q.add(Pair(y + ay, x + ax))
            }
        }

        return Triple(steps / 2, map, vis)
    }

    override fun a1() {
        println(mapping().first)
    }

    override fun a2() {
        val (_, map, vis) = mapping()
        val map2 = Array(map.size * 2) { CharArray(map[0].size * 2) { '.' } }

        for (y in map.indices)
            for (x in map[y].indices) {
                if (!vis[y][x]) continue
                map2[2*y][2*x] = map[y][x]
                when (map[y][x]) {
                    'S' -> {
                        if (map[y+1][x] == '|' || map[y+1][x] == 'L' || map[y+1][x] == 'J') map2[2*y+1][2*x] = '|'
                        if (map[y][x+1] == '-' || map[y][x+1] == '7' || map[y][x+1] == 'J') map2[2*y][2*x+1] = '-'
                    }
                    '|' -> { map2[2*y+1][2*x] = '|' }
                    '-' -> { map2[2*y][2*x+1] = '-' }
                    'L' -> { map2[2*y][2*x+1] = '-' }
                    'J' -> { }
                    '7' -> { map2[2*y+1][2*x] = '|' }
                    'F' -> { map2[2*y+1][2*x] = '|' ; map2[2*y][2*x+1] = '-' }
                }
            }

        val q = ArrayDeque<Pair<Int, Int>>()
        for (y in map2.indices)
            if (map2[y][0] == '.') {
                q.add(Pair(y, 0))
                map2[y][0] = ' '
            }
        for (x in map2[0].indices)
            if (map2[0][x] == '.') {
                q.add(Pair(0, x))
                map2[0][x] = ' '
            }
        while (!q.isEmpty()) {
            val (y, x) = q.removeFirst();
            for ((oy, ox) in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
                if (y + oy !in map2.indices) continue
                if (x + ox !in map2[y + oy].indices) continue
                if (map2[y + oy][x + ox] != '.') continue
                map2[y + oy][x + ox] = ' '
                vis[(y + oy) / 2][(x + ox) / 2] = true
                q.add(Pair(y + oy, x + ox))
            }
        }

        println(map.size * map[0].size - vis.sumOf { it.count { it } })
    }
}