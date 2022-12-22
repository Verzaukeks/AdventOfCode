package y2022

import general.Day

object Day22 : Day() {
    override val name = "Monkey Map"

    enum class Direction(val ox: Int, val oy: Int) {
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0),
        UP(0, -1);
        fun clockwise() = values()[(ordinal + 1) % 4]
        fun counterclockwise() = values()[(ordinal + 3) % 4]
    }

    override fun a1() {
        val map = INPUT.readLines()
            .takeWhile { it.isNotEmpty() }
            .map { it.toCharArray() }

        val sequence = INPUT.readLines()
            .last()
            .replace("L", ",L,")
            .replace("R", ",R,")
            .split(",")

        var x = map[0].indexOf('.')
        var y = 0
        var dir = Direction.RIGHT
        for (seq in sequence)
            when (seq) {
                "R" -> dir = dir.clockwise()
                "L" -> dir = dir.counterclockwise()
                else -> repeat(seq.toInt()) {
                    var nx = x + dir.ox
                    var ny = y + dir.oy

                    val overflow = when (dir) {
                        Direction.RIGHT -> map[y].size <= nx || map[y][nx] == ' '
                        Direction.DOWN -> map.size <= ny || map[ny].size <= x || map[ny][x] == ' '
                        Direction.LEFT -> nx < 0 || map[y][nx] == ' '
                        Direction.UP -> ny < 0 || map[ny].size <= x || map[ny][x] == ' '
                    }
                    if (overflow) {
                        val w = map[y].size - 1
                        val h = map.size - 1
                        val rx = when (dir) {
                            Direction.RIGHT -> 0..w
                            Direction.DOWN -> x..x
                            Direction.LEFT -> w downTo 0
                            Direction.UP -> x..x
                        }
                        val ry = when (dir) {
                            Direction.RIGHT -> y..y
                            Direction.DOWN -> 0..h
                            Direction.LEFT -> y..y
                            Direction.UP -> h downTo 0
                        }

                        search@for (sy in ry) for (sx in rx)
                            if (sx < map[sy].size && map[sy][sx] != ' ') {
                                nx = sx
                                ny = sy
                                break@search
                            }
                    }

                    if (map[ny][nx] == '#') return@repeat
                    x = nx
                    y = ny
                }
            }

//        println("x: ${x + 1}")
//        println("y: ${y + 1}")
//        println("dir: $dir")
        println(1000 * (y + 1) + 4 * (x + 1) + dir.ordinal)
    }

    override fun a2() {
        val map = INPUT.readLines()
            .takeWhile { it.isNotEmpty() }
            .map { it.toCharArray() }

        val sequence = INPUT.readLines()
            .last()
            .replace("L", ",L,")
            .replace("R", ",R,")
            .split(",")

        var x = map[0].indexOf('.')
        var y = 0
        var dir = Direction.RIGHT
        for (seq in sequence)
            when (seq) {
                "R" -> dir = dir.clockwise()
                "L" -> dir = dir.counterclockwise()
                else -> repeat(seq.toInt()) {
                    var nx = x + dir.ox
                    var ny = y + dir.oy
                    var nd = dir

                    val overflow = when (dir) {
                        Direction.RIGHT -> map[y].size <= nx || map[y][nx] == ' '
                        Direction.DOWN -> map.size <= ny || map[ny].size <= x || map[ny][x] == ' '
                        Direction.LEFT -> nx < 0 || map[y][nx] == ' '
                        Direction.UP -> ny < 0 || map[ny].size <= x || map[ny][x] == ' '
                    }
                    if (overflow) {
                        // magic numbers -> change to match your input ;)
                        val w = 50 * 3 - 1
                        val h = 50 * 4 - 1
                        val rx = when (dir) {
                            Direction.RIGHT -> listOf(
                                w downTo 0,
                                (50+y)..(50+y),
                                w downTo 0,
                                (y-100)..(y-100),
                            )[y / 50]
                            Direction.DOWN -> listOf(
                                (x+100)..(x+100),
                                w downTo 0,
                                w downTo 0,
                            )[x / 50]
                            Direction.LEFT -> listOf(
                                0..w,
                                (y-50)..(y-50),
                                0..w,
                                (y-100)..(y-100),
                            )[y / 50]
                            Direction.UP -> listOf(
                                0..w,
                                0..w,
                                (x-100)..(x-100),
                            )[x / 50]
                        }
                        val ry = when (dir) {
                            Direction.RIGHT -> listOf(
                                (149-y)..(149-y),
                                h downTo 0,
                                (149-y)..(149-y),
                                h downTo 0,
                            )[y / 50]
                            Direction.DOWN -> listOf(
                                0..h,
                                (x+100)..(x+100),
                                (x-50)..(x-50),
                            )[x / 50]
                            Direction.LEFT -> listOf(
                                (149-y)..(149-y),
                                0..h,
                                (149-y)..(149-y),
                                0..h,
                            )[y / 50]
                            Direction.UP -> listOf(
                                (x+50)..(x+50),
                                (x+100)..(x+100),
                                h downTo 0,
                            )[x / 50]
                        }
                        nd = when (dir) {
                            Direction.RIGHT -> listOf(Direction.LEFT, Direction.UP, Direction.LEFT, Direction.UP)[y / 50]
                            Direction.DOWN -> listOf(Direction.DOWN, Direction.LEFT, Direction.LEFT)[x / 50]
                            Direction.LEFT -> listOf(Direction.RIGHT, Direction.DOWN, Direction.RIGHT, Direction.DOWN)[y / 50]
                            Direction.UP -> listOf(Direction.RIGHT, Direction.RIGHT, Direction.UP)[x / 50]
                        }
                        // magic numbers for example
                        /*
                        val w = 4 * 4 - 1
                        val h = 4 * 3 - 1
                        val rx = when (dir) {
                            Direction.RIGHT -> listOf(
                                w downTo 0,
                                (19-y)..(19-y),
                                w downTo 0,
                            )[y / 4]
                            Direction.DOWN -> listOf(
                                (11-x)..(11-x),
                                0..w,
                                (11-x)..(11-x),
                                0..w,
                            )[x / 4]
                            Direction.LEFT -> listOf(
                                (4+y)..(4+y),
                                (19-y)..(19-y),
                                (15-y)..(15-y),
                            )[y / 4]
                            Direction.UP -> listOf(
                                (11-x)..(11-x),
                                0..w,
                                (11-x)..(11-x),
                                w downTo 0,
                            )[x / 4]
                        }
                        val ry = when (dir) {
                            Direction.RIGHT -> listOf(
                                (11-y)..(11-y),
                                0..h,
                                (11-y)..(11-y),
                            )[y / 4]
                            Direction.DOWN -> listOf(
                                h downTo 0,
                                (15-x)..(15-x),
                                h downTo 0,
                                (19-x)..(19-x),
                            )[x / 4]
                            Direction.LEFT -> listOf(
                                0..h,
                                h downTo 0,
                                h downTo 0,
                            )[y / 4]
                            Direction.UP -> listOf(
                                0..h,
                                (-4+x)..(-4+x),
                                0..h,
                                (19-x)..(19-x)
                            )[x / 4]
                        }
                        nd = when (dir) {
                            Direction.RIGHT -> listOf(Direction.LEFT, Direction.DOWN, Direction.LEFT)[y / 4]
                            Direction.DOWN -> listOf(Direction.UP, Direction.RIGHT, Direction.UP, Direction.RIGHT)[x / 4]
                            Direction.LEFT -> listOf(Direction.DOWN, Direction.UP, Direction.UP)[y / 4]
                            Direction.UP -> listOf(Direction.DOWN, Direction.RIGHT, Direction.DOWN, Direction.LEFT)[x / 4]
                        }
                         */

                        search@for (sy in ry) for (sx in rx)
                            if (sx < map[sy].size && map[sy][sx] != ' ') {
                                nx = sx
                                ny = sy
                                break@search
                            }
                    }

                    if (map[ny][nx] == '#') return@repeat
                    x = nx
                    y = ny
                    dir = nd
                }
            }

//        println("x: ${x + 1}")
//        println("y: ${y + 1}")
//        println("dir: $dir")
        println(1000 * (y + 1) + 4 * (x + 1) + dir.ordinal)
    }
}