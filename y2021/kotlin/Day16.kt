package y2021

import general.Day
import general.product

object Day16 : Day() {
    override val name = "Packet Decoder"

    private class Bits {
        private var dataIndex = 0
        private var data = INPUT.readText().
            replace("0", "0000").
            replace("1", "0001").
            replace("2", "0010").
            replace("3", "0011").
            replace("4", "0100").
            replace("5", "0101").
            replace("6", "0110").
            replace("7", "0111").
            replace("8", "1000").
            replace("9", "1001").
            replace("A", "1010").
            replace("B", "1011").
            replace("C", "1100").
            replace("D", "1101").
            replace("E", "1110").
            replace("F", "1111")

        fun bitsRead() = dataIndex
        fun readBit() = data[dataIndex++] - '0'
        fun readInt(size: Int): Int {
            var n = 0
            repeat(size) {
                n = (n shl 1) + readBit()
            }
            return n
        }
    }

    override fun a1() {
        val bits = Bits()
        var versionSum = 0

        fun getPacket() {
            val version = bits.readInt(3)
            val typeID = bits.readInt(3)

            versionSum += version

            if (typeID == 4) {
                var read: Boolean

                do {
                    read = (bits.readBit() == 1)
                    bits.readInt(4)
                } while (read)

                return
            }

            val lengthTypeID = bits.readBit()

            if (lengthTypeID == 0) {
                val length = bits.readInt(15)
                val start = bits.bitsRead()

                while (true) {
                    getPacket()

                    val end = bits.bitsRead()
                    if (end - start == length) return
                }
            }
            else {
                val amount = bits.readInt(11)

                repeat(amount) {
                    getPacket()
                }
            }
        }

        getPacket()
        println(versionSum)
    }

    override fun a2() {
        val bits = Bits()

        val number = readPacket(bits)
        println(number)
    }

    private fun readPacket(bits: Bits): Long {
        val version = bits.readInt(3)
        val typeID = bits.readInt(3)

        if (typeID == 4)
            return readLiteral(bits)

        val outputs = readSubPackets(bits)
        when (typeID) {
            0 -> return outputs.sum()
            1 -> return outputs.product()
            2 -> return outputs.minOrNull()!!
            3 -> return outputs.maxOrNull()!!
            5 -> return if (outputs[0] > outputs[1]) 1 else 0
            6 -> return if (outputs[0] < outputs[1]) 1 else 0
            7 -> return if (outputs[0] == outputs[1]) 1 else 0
        }

        throw Error("Unknown typeID: $typeID")
    }

    private fun readLiteral(bits: Bits): Long {
        var read: Boolean
        var number = 0L

        do {
            read = (bits.readBit() == 1)
            number = (number shl 4) + bits.readInt(4)
        } while (read)

        return number
    }

    private fun readSubPackets(bits: Bits): List<Long> {
        val outputs = ArrayList<Long>()
        val typeLengthID = bits.readBit()

        when (typeLengthID) {
            0 -> {
                val length = bits.readInt(15)
                val start = bits.bitsRead()

                while (true) {
                    outputs += readPacket(bits)

                    val end = bits.bitsRead()
                    if (end - start == length) break
                }
            }
            1 -> {
                val amount = bits.readInt(11)

                repeat(amount) {
                    outputs += readPacket(bits)
                }
            }
        }

        return outputs
    }
}