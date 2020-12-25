package y2020

object Day25 : Day() {

    override val day = 25
    override val name = "Combo Breaker"

    override fun a1() {
        val (publicKeyCard,  publicKeyDoor) = INPUT.readLines().map { it.toInt() }
        val loopSizeDoor = crackPublicKey(publicKeyDoor)
        val encryptionKey = transform(publicKeyCard, loopSizeDoor)
        println(encryptionKey)
    }

    private fun crackPublicKey(publicKey: Int): Int {
        var value = 1
        var loops = 0
        while (value != publicKey) {
            value *= 7
            value %= 20201227
            loops ++
        }
        return loops
    }

    private fun transform(subjectNumber: Int, loopSize: Int): Int {
        var value = 1L
        repeat(loopSize) {
            value *= subjectNumber
            value %= 20201227
        }
        return value.toInt()
    }

    override fun a2() {}
}