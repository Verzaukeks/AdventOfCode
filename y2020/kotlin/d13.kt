package y2020

object d13 {
    fun a1() {
        val input = Files[13].readLines()

        val timestamp = input[0].toInt()
        var busId = -1
        var arrives = -1

        for (bus in input[1].split(",")) {
            if (bus == "x") continue

            val id = bus.toInt()
            val nextTime = (timestamp / id + 1) * id

            if (busId == -1 || nextTime < arrives) {
                busId = id
                arrives = nextTime
            }
        }

        println(busId * (arrives - timestamp))
    }

    fun a2() {
        val input = Files[13].readLines()[1].split(",")

        // Chinese Remainder Theorem
        // x = (busId-index) (mod busId)   =>   x % busId = busId - index
        // https://www.youtube.com/watch?v=zIFehsBHB8o

        var N = 1L
        var sum = 0L

        for (busId in input)
            if (busId != "x")
                N *= busId.toLong()

        for (index in input.indices) {
            if (input[index] == "x") continue
            val busId = input[index].toLong()

            val bi = busId - index.toLong()
            val Ni = N / busId
            val xi = findXi(Ni, busId)

            sum += bi * Ni * xi
        }

        println(sum % N)
    }

    // returns xi where (a * xi) % b = 1
    // too lazy, just loop over it
    fun findXi(a: Long, b: Long): Long {
        var xi = 0L
        while (a * ++xi % b != 1L);
        return xi
    }
}