package y2020

import kotlin.math.sqrt

object Day20 : Day() {

    /*
        not very efficient,
        and I am too lazy to improve it
     */

    override val day = 20
    override val name = "Jurassic Jigsaw"

    /**
     * @param index index from tiles array
     * @param id tileId given in input
     * @param size tile size (normal tiles = 10 ; from a2 -> big combined tile 8 * 12 (or 8 * 3 with examples))
     */
    data class Tile(val index: Int, val id: Int, val size: Int, var content: String, val face: Face, val adjacent: Adjacent) {
        /**
         * rotate content counterclockwise
         * reevaluate face numbers (only when size = 10)
         */
        fun rotate(): Tile {
            val out = content.toCharArray()
            for (y in 0 until size) for (x in 0 until size) {
                val nx = y
                val ny = size-1 - x
                out[ny * size + nx] = content[y * size + x]
            }
            content = out.concatToString()
            face.update(content)
            return this
        }
        /**
         * flip content horizontally
         * (vertically = rotate / flip / rotate  =>  no need for extra function)
         */
        fun flip(): Tile {
            val out = content.toCharArray()
            for (y in 0 until size) for (x in 0 until size) {
                val nx = x
                val ny = size-1 - y
                out[ny * size + nx] = content[y * size + x]
            }
            content = out.concatToString()
            face.update(content)
            return this
        }
        /**
         * printing the tile in a pretty format for debugging
         */
        fun printTile() {
            println("Tile $id:")
            for (y in 0 until size)
                println(content.substring((y * size) until y * size + size))
        }
        /**
         * insert tile into a bigger one
         * (only needed in a2 for combining)
         */
        fun insert(offX: Int, offY: Int, array: Array<Array<Char>>) {
            for (y in 1 until size-1)
                for (x in 1 until size-1) {
                    val c = content[y * size + x]
                    array[offY * (size-2) + y-1][offX * (size-2) + x-1] = c
                }
        }
    }

    /**
     * borders of a tile represented as numbers for better compare
     * (only available when size = 10)
     */
    data class Face(var top: Int, var left: Int, var bottom: Int, var right: Int) {
        /**
         * update faces from tile
         */
        fun update(content: String) {
            if (content.length != 100) return
            top = slice(content, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
            left = slice(content, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90)
            bottom = slice(content, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99)
            right = slice(content, 9, 19, 29, 39, 49, 59, 69, 79, 89, 99)
        }
        /**
         * sort out specific chars form a string and combine them together
         * (returned in number format: .=0 #=1)
         */
        private fun slice(input: String, vararg indices: Int): Int {
            var string = ""
            for (index in indices) string += input[index]
            return string.replace(".", "0").replace("#", "1").toInt(2)
        }
    }

    /**
     * top, left, bottom, right  are the index numbers of the adjacent piece in the array (=> no need find tile id in array)
     * @param number how many adjacent pieces does this tile have (=> corner piece[2] / side piece[3] / middle piece[4])
     */
    data class Adjacent(var number: Int, var top: Int, var left: Int, var bottom: Int, var right: Int) {
        /**
         * return how many adjacent pieces are already found and set
         */
        fun count() =
                (if (top != -1) 1 else 0) +
                (if (left != -1) 1 else 0) +
                (if (bottom != -1) 1 else 0) +
                (if (right != -1) 1 else 0)
    }

    override fun a1() {
        // read input as an array of tiles (directly assign tile the corresponding index number for easier access)
        var index = 0
        val tiles = INPUT.readText().replace("\r", "").split("\n\n")
                .map {
                    val id = it.substringAfter(" ").substringBefore(":").toInt()
                    val content = it.substringAfter("\n").replace("\n", "")
                    val face = Face(0, 0, 0, 0) ; face.update(content)
                    val adjacent = Adjacent(0, -1, -1, -1, -1)
                    Tile(index++, id, 10, content, face, adjacent)
                }

        // check if two tiles are adjacent in the current orientation
        val check = { a: Tile, b: Tile ->
            a.face.top == b.face.bottom ||
            a.face.left == b.face.right ||
            a.face.bottom == b.face.top ||
            a.face.right == b.face.left
        }

        var product = 1L

        for (tile in tiles) {
            var adjacent = 0

            for (otherTile in tiles)
                if (tile != otherTile)
                    if ( // check every combination available
                            check(tile, otherTile) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate().flip()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate())
                    ) {
                        adjacent ++
                    }

            // if tile is an edge piece
            if (adjacent == 2) {
                // println(tile.id) // debug: should only print 4 times, because of 4 edges
                product *= tile.id
            }
        }

        // output
        println(product)
    }

    override fun a2() {
        // -> read input as an array of tiles (directly assign tile the corresponding index number for easier access)
        var index = 0
        val tiles = INPUT.readText().replace("\r", "").split("\n\n")
                .map {
                    val id = it.substringAfter(" ").substringBefore(":").toInt()
                    val content = it.substringAfter("\n").replace("\n", "")
                    val face = Face(0, 0, 0, 0) ; face.update(content)
                    val adjacent = Adjacent(0, -1, -1, -1, -1)
                    Tile(index++, id, 10, content, face, adjacent)
                }

        // check if two tiles are adjacent in the current orientation
        val check = { a: Tile, b: Tile ->
            a.face.top == b.face.bottom ||
            a.face.left == b.face.right ||
            a.face.bottom == b.face.top ||
            a.face.right == b.face.left
        }

        // -> assign adjacent number
        var edge: Tile? = null
        for (tile in tiles) {
            var adjacent = 0

            for (otherTile in tiles)
                if (tile != otherTile)
                    if ( // check every combination
                            check(tile, otherTile) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate().flip()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate()) ||
                            check(tile, otherTile.rotate())
                    ) {
                        adjacent ++
                    }

            tile.adjacent.number = adjacent

            // i need an edge piece so that my align algorithm will work correctly, just take the first one
            if (adjacent == 2 && edge == null) edge = tile
        }

        // -> align tiles with each other
        align(edge!!, tiles)

        // -> combine tiles to a big one (without borders)
        // start from top left and proceed right (offX++), when the outermost right piece is reached, go onto the next line(offY++)
        val lineSize = 8 * sqrt(tiles.size.toDouble()).toInt()
        val combinedContent = Array(lineSize) { Array(lineSize) { '/' } }
        val topLeft = tiles.first { it.adjacent.top == -1 && it.adjacent.left == -1 }

        var offX = 0
        var offY = 0

        var tileY = topLeft
        while (true) {

            var tileX = tileY
            while (true) {
                tileX.insert(offX++, offY, combinedContent)

                if (tileX.adjacent.right == -1) break
                tileX = tiles[tileX.adjacent.right]
            }

            offX = 0
            offY ++
            if (tileY.adjacent.bottom == -1) break
            tileY = tiles[tileY.adjacent.bottom]
        }

        val tile = Tile(-1, -1, lineSize, combinedContent.joinToString("") { it.joinToString("") } , Face(0, 0, 0, 0), Adjacent(0, 0, 0, 0, 0))

        // -> find and replace monsters with 'O'
        // (monster represented as regex:)
        val monster = "#.{${lineSize-19}}#.{4}##.{4}##.{4}###.{${lineSize-19}}#.{2}#.{2}#.{2}#.{2}#.{2}#".toRegex()
        if ( // check for every combination and use given configuration if a monster was found
                monster.find(tile.content) != null ||
                monster.find(tile.rotate().content) != null ||
                monster.find(tile.rotate().content) != null ||
                monster.find(tile.rotate().content) != null ||
                monster.find(tile.rotate().flip().content) != null ||
                monster.find(tile.rotate().content) != null ||
                monster.find(tile.rotate().content) != null ||
                monster.find(tile.rotate().content) != null
        ); // hehe, empty if -> because it will not rotate further if one condition is already true

        //println("sea waters correctly orientated and combined:")
        //tile.printTile()
        //println()

        // i have to find monsters several times, because regex ignores monsters in the same line
        while (true) {
            val a = tile.content.toCharArray()
            val matches = monster.findAll(tile.content).iterator()

            if (!matches.hasNext()) break
            do {
                val match = matches.next()
                var i = match.range.first

                a[i] = 'O'; i++; i += lineSize - 19
                a[i] = 'O'; i++; i += 4
                a[i] = 'O'; i++; i += 0
                a[i] = 'O'; i++; i += 4
                a[i] = 'O'; i++; i += 0
                a[i] = 'O'; i++; i += 4
                a[i] = 'O'; i++; i += 0
                a[i] = 'O'; i++; i += 0
                a[i] = 'O'; i++; i += lineSize - 19
                a[i] = 'O'; i++; i += 2
                a[i] = 'O'; i++; i += 2
                a[i] = 'O'; i++; i += 2
                a[i] = 'O'; i++; i += 2
                a[i] = 'O'; i++; i += 2
                a[i] = 'O'; i++; i += 0
            } while (matches.hasNext())
            tile.content = a.concatToString()
        }

        // -> output
        //println("sea water filled with sea monsters:")
        //tile.printTile()
        //println()
        println(tile.content.count { it == '#' })

        //tile.content = tile.content.replace("#", ".")
        //println("only sea monsters:")
        //tile.printTile()

    }

    /**
     * find adjacent tiles for given tile and repeat the process for just found tiles
     * (i do not know if it has to start from an edge)
     */
    private fun align(tile: Tile, tiles: List<Tile>) {
        if (tile.adjacent.number == tile.adjacent.count()) return

        // check for tiles to the right
		if (tile.adjacent.right == -1)
            for (otherTile in tiles)
                if (tile != otherTile) {
                    if ( // check every combination (only rotate other tile if it has no adjacent tiles)
                            tile.face.right == otherTile.face.left || (
                            otherTile.adjacent.count() == 0 && (
                            tile.face.right == otherTile.rotate().face.left ||
                            tile.face.right == otherTile.rotate().face.left ||
                            tile.face.right == otherTile.rotate().face.left ||
                            tile.face.right == otherTile.rotate().flip().face.left ||
                            tile.face.right == otherTile.rotate().face.left ||
                            tile.face.right == otherTile.rotate().face.left ||
                            tile.face.right == otherTile.rotate().face.left
                            ))
                    ) {
                        tile.adjacent.right = otherTile.index
                        otherTile.adjacent.left = tile.index

                        align(otherTile, tiles)
                        break
                    }
                }

        // check for tiles to the bottom
		if (tile.adjacent.bottom == -1)
            for (otherTile in tiles)
                if (tile != otherTile) {
                    if ( // check every combination (only rotate other tile if it has no adjacent tiles)
                            tile.face.bottom == otherTile.face.top || (
                            otherTile.adjacent.count() == 0 && (
                            tile.face.bottom == otherTile.rotate().face.top ||
                            tile.face.bottom == otherTile.rotate().face.top ||
                            tile.face.bottom == otherTile.rotate().face.top ||
                            tile.face.bottom == otherTile.rotate().flip().face.top ||
                            tile.face.bottom == otherTile.rotate().face.top ||
                            tile.face.bottom == otherTile.rotate().face.top ||
                            tile.face.bottom == otherTile.rotate().face.top
                            ))
                    ) {
                        tile.adjacent.bottom = otherTile.index
                        otherTile.adjacent.top = tile.index

                        align(otherTile, tiles)
                        break
                    }
                }

        // check for tiles to the left
		if (tile.adjacent.left == -1)
            for (otherTile in tiles)
                if (tile != otherTile) {
                    if ( // check every combination (only rotate other tile if it has no adjacent tiles)
                            tile.face.left == otherTile.face.right || (
                            otherTile.adjacent.count() == 0 && (
                            tile.face.left == otherTile.rotate().face.right ||
                            tile.face.left == otherTile.rotate().face.right ||
                            tile.face.left == otherTile.rotate().face.right ||
                            tile.face.left == otherTile.rotate().flip().face.right ||
                            tile.face.left == otherTile.rotate().face.right ||
                            tile.face.left == otherTile.rotate().face.right ||
                            tile.face.left == otherTile.rotate().face.right
                            ))
                    ) {
                        tile.adjacent.left = otherTile.index
                        otherTile.adjacent.right = tile.index

                        align(otherTile, tiles)
                        break
                    }
                }

        // check for tiles to the top
        if (tile.adjacent.top == -1)
            for (otherTile in tiles)
                if (tile != otherTile) {
                    if ( // check every combination (only rotate other tile if it has no adjacent tiles)
                            tile.face.top == otherTile.face.bottom || (
                            otherTile.adjacent.count() == 0 && (
                            tile.face.top == otherTile.rotate().face.bottom ||
                            tile.face.top == otherTile.rotate().face.bottom ||
                            tile.face.top == otherTile.rotate().face.bottom ||
                            tile.face.top == otherTile.rotate().flip().face.bottom ||
                            tile.face.top == otherTile.rotate().face.bottom ||
                            tile.face.top == otherTile.rotate().face.bottom ||
                            tile.face.top == otherTile.rotate().face.bottom
                            ))
                    ) {
                        tile.adjacent.top = otherTile.index
                        otherTile.adjacent.bottom = tile.index

                        align(otherTile, tiles)
                        break
                    }
                }
    }
}

