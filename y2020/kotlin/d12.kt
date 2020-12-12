package y2020

import y2020.inputs.Files
import kotlin.math.absoluteValue

object d12 {
    data class Command(val action: String, val amount: Int)

    fun a1() {
        val input = Files[12].readLines()
                .map {
                    Command(it.substring(0, 1),
                            it.substring(1).toInt())
                }

        var directionNorth = 0
        var directionEast = 1

        var north = 0
        var east = 0

        for (command in input)
            when (command.action) {
                "N" -> north += command.amount
                "S" -> north -= command.amount
                "E" ->  east += command.amount
                "W" ->  east -= command.amount
                "F" -> {
                    north += command.amount * directionNorth
                     east += command.amount * directionEast
                }
                "L" -> {
                    var a = (command.amount % 360) / 90
                    while (a-- > 0) {
                        /*
                             N  E -> N  E
                             0  1    1  0 (east -> north)
                             0 -1   -1  0 (west -> south)
                             1  0    0 -1 (north -> west)
                            -1  0    0  1 (south -> east)
                         */
                        val tmp = directionNorth
                        directionNorth = directionEast
                        directionEast = -tmp
                    }
                }
                "R" -> {
                    var a = (command.amount % 360) / 90
                    while (a-- > 0) {
                        // same as L, swap N and E
                        val tmp = directionEast
                        directionEast = directionNorth
                        directionNorth = -tmp
                    }
                }
                else -> error("unknown command: ${command.action}")
            }

        println(north.absoluteValue + east.absoluteValue)
    }

    fun a2() {
        val input = Files[12].readLines()
                .map {
                    Command(it.substring(0, 1),
                            it.substring(1).toInt())
                }

        var north = 0
        var east = 0
        var waypointNorthRelative = 1
        var waypointEastRelative = 10

        for (command in input)
            when (command.action) {
                "N" -> waypointNorthRelative += command.amount
                "S" -> waypointNorthRelative -= command.amount
                "E" ->  waypointEastRelative += command.amount
                "W" ->  waypointEastRelative -= command.amount
                "F" -> {
                    north += command.amount * waypointNorthRelative
                     east += command.amount * waypointEastRelative
                }
                "L" -> {
                    var a = (command.amount % 360) / 90
                    while (a-- > 0) {
                        val tmp = waypointNorthRelative
                        waypointNorthRelative = waypointEastRelative
                        waypointEastRelative = -tmp
                    }
                }
                "R" -> {
                    var a = (command.amount % 360) / 90
                    while (a-- > 0) {
                        val tmp = waypointEastRelative
                        waypointEastRelative = waypointNorthRelative
                        waypointNorthRelative = -tmp
                    }
                }
                else -> error("unknown command: ${command.action}")
            }

        println(north.absoluteValue + east.absoluteValue)
    }
}