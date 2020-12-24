package y2020

object Day24 : Day() {

    override val day = 24
    override val name = "Lobby Layout"

    data class Pos(val x: Int, val y: Int)

    override fun a1() {
        val tiles = ArrayList<Pos>()

        for (instruction in INPUT.readLines()) {
            var offset = 0
            var x = 0
            var y = 0

            while (offset < instruction.length)
                when {
                    instruction.startsWith( "e", offset) -> { x += 2 ;        ; offset += 1 }
                    instruction.startsWith( "w", offset) -> { x -= 2 ;        ; offset += 1 }
                    instruction.startsWith("ne", offset) -> { x += 1 ; y += 1 ; offset += 2 }
                    instruction.startsWith("nw", offset) -> { x -= 1 ; y += 1 ; offset += 2 }
                    instruction.startsWith("se", offset) -> { x += 1 ; y -= 1 ; offset += 2 }
                    instruction.startsWith("sw", offset) -> { x -= 1 ; y -= 1 ; offset += 2 }
                }

            val pos = Pos(x, y)
            if (pos in tiles) tiles -= pos
            else tiles += pos
        }

        println(tiles.size)
    }

    override fun a2() {
        var tilesIn = ArrayList<Pos>()
        var tilesOut = ArrayList<Pos>()
        val tilesWhite = HashMap<Pos, Int>()

        val masks = ArrayList<Pos>()
        masks += Pos( 2,  0)
        masks += Pos(-2,  0)
        masks += Pos( 1,  1)
        masks += Pos(-1,  1)
        masks += Pos( 1, -1)
        masks += Pos(-1, -1)

        // day 0
        for (instruction in INPUT.readLines()) {
            var offset = 0
            var x = 0
            var y = 0

            while (offset < instruction.length)
                when {
                    instruction.startsWith( "e", offset) -> { x += 2 ;        ; offset += 1 }
                    instruction.startsWith( "w", offset) -> { x -= 2 ;        ; offset += 1 }
                    instruction.startsWith("ne", offset) -> { x += 1 ; y += 1 ; offset += 2 }
                    instruction.startsWith("nw", offset) -> { x -= 1 ; y += 1 ; offset += 2 }
                    instruction.startsWith("se", offset) -> { x += 1 ; y -= 1 ; offset += 2 }
                    instruction.startsWith("sw", offset) -> { x -= 1 ; y -= 1 ; offset += 2 }
                }

            val pos = Pos(x, y)
            if (pos in tilesIn) tilesIn.remove(pos)
            else tilesIn.add(pos)
        }

        // day 1 - day 100
        repeat(100) {

            for (tile in tilesIn) {
                var adjacent = 0

                for (mask in masks) {
                    val pos = Pos(
                            tile.x + mask.x,
                            tile.y + mask.y)

                    when (pos) {
                        in tilesIn -> adjacent ++
                        in tilesWhite -> tilesWhite[pos] = tilesWhite[pos]!! + 1
                        else -> tilesWhite[pos] = 1
                    }
                }

                if (adjacent == 1 || adjacent == 2)
                    tilesOut.add(tile)
            }
            tilesIn.clear()

            for ((tile, adjacent) in tilesWhite.entries) {
                if (adjacent == 2)
                    tilesOut.add(tile)
            }
            tilesWhite.clear()

            val tmp = tilesIn
            tilesIn = tilesOut
            tilesOut = tmp
        }

        // output
        println(tilesIn.size)
    }
}