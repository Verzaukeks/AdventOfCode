package y2020

object Day23 : Day() {

    override val day = 23
    override val name = "Crab Cups"

    override fun a1() {
        val cups = INPUT.readText().toCharArray().map { it - '0' }.toMutableList()
        val pickedUp = ArrayList<Int>()
        val min = cups.minOrNull() ?: return
        val max = cups.maxOrNull() ?: return
        var current = 0

        repeat(100) {

            repeat(3) {
                val index = (current + 1) % cups.size
                if (index < current) current --
                pickedUp += cups.removeAt(index)
            }

            var destination: Int
            var number = cups[current]
            do {
                if (--number < min) number = max
                destination = cups.indexOf(number)
            } while (destination == -1)

            repeat(3) {
                val index = (destination + 1)
                if (index <= current) current ++
                cups.add(index, pickedUp.removeLast())
            }

            current = (current + 1) % cups.size
        }

        var circle = cups.joinToString("")
        circle = circle.substringAfter("1") + circle.substringBefore("1")
        println(circle)
    }

    data class Item(var number: Int, var next: Int)

    override fun a2() {
        val circle = Array(1000000) { Item(it+1, it+1) }
        var current = circle[0]

        var index = 0
        INPUT.readText().toCharArray().forEach { circle[index++].number = it - '0' }
        circle[circle.lastIndex].next = 0

        var loop = 0
        while (true) {

            var destNumber = current.number
            do {
                destNumber --
                if (destNumber < 1) destNumber = circle.size
            } while (
                destNumber == circle[current.next].number ||
                destNumber == circle[circle[current.next].next].number ||
                destNumber == circle[circle[circle[current.next].next].next].number
            )

            val dest = findFast(destNumber, circle)
            val swapStart = current.next
            val swapEnd = circle[circle[swapStart].next].next

            current.next = circle[swapEnd].next
            circle[swapEnd].next = dest.next
            dest.next = swapStart

            current = circle[current.next]
            if (++loop == 10000000) break
        }

        current = findFast(1, circle)
        val a = circle[current.next].number.toLong()
        val b = circle[circle[current.next].next].number.toLong()
        println(a * b)
    }

    private fun findFast(number: Int, circle: Array<Item>): Item {
        var i = if (number < 10) 0 else return circle[number-1]
        while (circle[i].number != number) i ++
        return circle[i]
    }
}