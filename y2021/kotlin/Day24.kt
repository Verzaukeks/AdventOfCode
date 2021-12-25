package y2021

import general.Day

object Day24 : Day() {
    override val name = "Arithmetic Logic Unit"

    private fun readAABBCC(): Triple<IntArray, IntArray, IntArray> {
        val aa = IntArray(14)
        val bb = IntArray(14)
        val cc = IntArray(14)

        var index = 0
        INPUT.readText()
            .replace("\r", "")
            .replace("\n", " ")
            .replaceFirst("inp w ", "")
            .split("inp w ")
            .forEach {
                val operands = it.split(" ")
                aa[index] = operands[11].toInt()
                bb[index] = operands[14].toInt()
                cc[index] = operands[44].toInt()
                index += 1
            }

        return Triple(aa, bb, cc)
    }

//  var z = incomeZ
//  if (keyCode != z % 26 + bb[keyPosition]) {
//      z /= aa[keyPosition]
//      z = z * 26 + keyCode + cc[keyPosition]
//  } else {
//      z /= aa[keyPosition]
//  }

    override fun a1() {
        val (aa, bb, cc) = readAABBCC()

        val lastOffset = ArrayList<Int>()
        val key = IntArray(14)

        for (position in 0..13)
            when (aa[position]) {
                1 -> {
                    lastOffset += position
                }
                26 -> {
                    val pos = lastOffset.removeLast()
                    val offset = cc[pos] + bb[position]

                    when {
                        offset >= 0 -> {
                            key[position] = 9
                            key[pos] = 9 - offset
                        }
                        else -> {
                            key[position] = 9 + offset
                            key[pos] = 9
                        }
                    }
                }
            }

        println(key.joinToString(""))
    }

    override fun a2() {
        val (aa, bb, cc) = readAABBCC()

        val lastOffset = ArrayList<Int>()
        val key = IntArray(14)

        for (position in 0..13)
            when (aa[position]) {
                1 -> {
                    lastOffset += position
                }
                26 -> {
                    val pos = lastOffset.removeLast()
                    val offset = cc[pos] + bb[position]

                    when {
                        offset >= 0 -> {
                            key[position] = 1 + offset
                            key[pos] = 1
                        }
                        else -> {
                            key[position] = 1
                            key[pos] = 1 - offset
                        }
                    }
                }
            }

        println(key.joinToString(""))
    }
}