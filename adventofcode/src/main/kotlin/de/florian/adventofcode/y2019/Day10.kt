package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


fun main() {
    Day10().exec()
}

class Day10 : AoCDay() {
    private fun convertInput(input: String) = input.trim().split("\n").map { it.chunked(1).toTypedArray() }.toTypedArray()

    data class Point(val y: Int, val x: Int) {
        fun dist(p: Point) : Double{
            return sqrt(p.x.toDouble().pow(2) + p.y.toDouble().pow(2))
        }
    }

    override fun part1(): String {
        var map = convertInput(
            """
.#..#
.....
#####
....#
...##
""")
        var asteroidsPerMonitoringStation = mutableMapOf<Point, MutableMap<Point, Boolean>>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if(map[y][x] == "#") {
                    val curPoint = Point(y, x)
                    asteroidsPerMonitoringStation[curPoint] = mutableMapOf()
                    for (sY in map.indices) {
                        for (sX in map[sY].indices) {
                            if (y == sY && x == sX) {
                                continue
                            }
                            if (x != sX) {
                                //y = m*x+b
                                var m = (sY - y).toDouble() / (sX - x)
                                var f: (Int) -> (Double) = { _x -> m * _x - m * sX - sY }
                                var points = mutableListOf<Point>()
                                for (lineY in map.indices) {
                                    for (lineX in map[lineY].indices) {
                                        if (y == lineY && x == lineX) {
                                            continue
                                        }
                                        if (map[lineY][lineX] == "#") {
                                            if (abs(-f(lineX) - lineY.toDouble()) < 0.00001) {
                                                points.add(Point(lineY, lineX))
                                            }
                                        }
                                    }
                                }
                                if (points.isNotEmpty()) {
                                    points.minBy { it.dist(curPoint) }
                                        ?.let { asteroidsPerMonitoringStation[curPoint]!![it] = true }
                                }
                            } else {
                                //HORIZONTAL LINE
                                for (lineY in y downTo 0) {
                                    if (map[lineY][x] == "#") {
                                        asteroidsPerMonitoringStation[curPoint]!![Point(lineY, x)]
                                        break
                                    }
                                }
                                for (lineY in y until map.size) {
                                    if (map[lineY][x] == "#") {
                                        asteroidsPerMonitoringStation[curPoint]!![Point(lineY, x)]
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (mutableEntry in asteroidsPerMonitoringStation) {
            map[mutableEntry.key.y][mutableEntry.key.x] = mutableEntry.value.size.toString()
        }
        for (y in map.indices) {
            for (x in map[y].indices) {
                print(map[y][x])
            }
            println()
        }
        return asteroidsPerMonitoringStation.values.map { it.keys.size }.max().toString()
    }


    override fun part2(): String {
        return ""
    }
}
