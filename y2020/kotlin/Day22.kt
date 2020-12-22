package y2020

object Day22 : Day() {

    override val day = 22
    override val name = "Crab Combat"

    override fun a1() {
        val input = INPUT.readText().replace("\r", "")

        val player1 = input.substringAfter("Player 1:\n").substringBefore("\n\n")
                .split("\n").map { it.toInt() }.toMutableList()
        val player2 = input.substringAfter("Player 2:\n").substringBefore("\n\n")
                .split("\n").map { it.toInt() }.toMutableList()

        while (true) {
            val card1 = player1.firstOrNull() ?: break
            val card2 = player2.firstOrNull() ?: break
            player1.removeFirst()
            player2.removeFirst()

            if (card1 > card2) {
                player1 += card1
                player1 += card2
            } else {
                player2 += card2
                player2 += card1
            }
        }

        val player = if (player1.isEmpty()) player2 else player1
        val size = player.size

        player.foldRightIndexed(0) { index, item, accu ->
            accu + (item * (size - index))
        }.let(::println)
    }

    override fun a2() {
        val input = INPUT.readText().replace("\r", "")

        val player1 = input.substringAfter("Player 1:\n").substringBefore("\n\n")
                .split("\n").map { it.toInt() }.let { ArrayList(it) }
        val player2 = input.substringAfter("Player 2:\n").substringBefore("\n\n")
                .split("\n").map { it.toInt() }.let { ArrayList(it) }

        val game = game(player1, player2)
        val player = if (game) player1 else player2
        val size = player.size

        player.foldRightIndexed(0) { index, item, accu ->
            accu + (item * (size - index))
        }.let(::println)
    }

    private fun game(player1: ArrayList<Int>, player2: ArrayList<Int>): Boolean {
        // slow: val states = ArrayList<String>()
        val states = ArrayList<Long>()

        while (true) {
            /* slow: val state =
                    player1.joinToString("") { if (it < 10) " $it" else "$it" } + "|" +
                    player2.joinToString("") { if (it < 10) " $it" else "$it" }*/
            val state =
                    player1.foldRightIndexed(0L) { index, item, accu -> accu + (item * (index + 1)) } *
                    player2.foldRightIndexed(0L) { index, item, accu -> accu + (item * (index + 1)) }
            if (state in states) return true
            states += state

            val card1 = player1.first()
            val card2 = player2.first()
            player1.removeFirst()
            player2.removeFirst()

            var playerOneWin = card1 > card2

            if (player1.size >= card1 && player2.size >= card2) {
                val ply1 = player1.copy(card1)
                val ply2 = player2.copy(card2)
                playerOneWin = game(ply1,  ply2)
            }

            if (playerOneWin) {
                player1 += card1
                player1 += card2
            } else {
                player2 += card2
                player2 += card1
            }

            if (player1.isEmpty()) return false
            if (player2.isEmpty()) return true
        }
    }
    
}

fun ArrayList<Int>.copy(size: Int): ArrayList<Int> {
    val out = ArrayList<Int>(size)
    for(i in 0 until size) out += this[i]
    return out
}