package y2019

import general.Day
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue

object Day03 : Day() {
    override val name = "Crossed Wires"

    data class Line(val stepsTaken: Int, val fx: Int, val fy: Int, val tx: Int, val ty: Int)

    fun path(wire: List<String>): List<Line> {
        var prevX = 0
        var prevY = 0
        var prevStepsTaken = 0
        return wire.map { code ->
            val value = code.substring(1).toInt()
            val toX = prevX + (if (code[0] == 'R') value else if (code[0] == 'L') -value else 0)
            val toY = prevY + (if (code[0] == 'U') value else if (code[0] == 'D') -value else 0)
            Line(prevStepsTaken, prevX, prevY, toX, toY).also {
                prevStepsTaken += value
                prevX = toX
                prevY = toY
            }
        }
    }

    fun cross(l1: Line, l2: Line): Pair<Int, Int>? {
        val ix: Int
        val iy: Int

        if (l1.fy == l1.ty) {
            if (l2.fy == l2.ty || l2.fx != l2.tx) return null
            if (l2.fx > max(l1.fx, l1.tx) || l2.fx < min(l1.fx, l1.tx)) return null
            if (l1.fy > max(l2.fy, l2.ty) || l1.fy < min(l2.fy, l2.ty)) return null
            ix = l2.fx
            iy = l1.fy
        }
        else if (l1.fx == l1.tx) {
            if (l2.fx == l2.tx || l2.fy != l2.ty) return null
            if (l2.fy > max(l1.fy, l1.ty) || l2.fy < min(l1.fy, l1.ty)) return null
            if (l1.fx > max(l2.fx, l2.tx) || l1.fx < min(l2.fx, l2.tx)) return null
            ix = l1.fx
            iy = l2.fy
        }
        else return null

        return Pair(ix, iy)
    }

    override fun a1() {
        val (wire1, wire2) = INPUT.readLines()
        val lines1 = path(wire1.split(","))
        val lines2 = path(wire2.split(","))

        var distance = 987654321

        for (line1 in lines1) for (line2 in lines2) {
            val (ix, iy) = cross(line1, line2) ?: continue
            val dis = ix.absoluteValue + iy.absoluteValue
            if (dis != 0 && dis < distance)
                distance = dis
        }

        println(distance)
    }

    override fun a2() {
        val (wire1, wire2) = INPUT.readLines()
        val lines1 = path(wire1.split(","))
        val lines2 = path(wire2.split(","))

        var stepsTakenSum = 987654321

        for (line1 in lines1) for (line2 in lines2) {
            val (ix, iy) = cross(line1, line2) ?: continue
            if (ix.absoluteValue + iy.absoluteValue == 0) continue

            val a = (line1.fx - ix).absoluteValue + (line1.fy - iy).absoluteValue
            val b = (line2.fx - ix).absoluteValue + (line2.fy - iy).absoluteValue
            val sts = line1.stepsTaken + line2.stepsTaken + a + b

            if (sts < stepsTakenSum)
                stepsTakenSum = sts
        }

        println(stepsTakenSum)
    }
}