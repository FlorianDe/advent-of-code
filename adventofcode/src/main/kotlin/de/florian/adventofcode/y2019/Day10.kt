package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.util.indices2DFiltered


fun main() {
    Day10().exec()
}

class Day10 : AoCDay() {
    private fun convertInput(input: String) = input.trim().split("\n").map { it.chunked(1).toTypedArray() }.toTypedArray()

    fun getBestMonitoringStation(map : Array<Array<String>>): MutableMap<Point, Int> {
        val asteroidsPerMonitoringStation = mutableMapOf<Point, MutableMap<Point, Boolean>>()
        val anglesPerMonitoringStation = mutableMapOf<Point, MutableList<Double>>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == ".") {
                    continue
                }
                val curPoint = Point(x, y)
                anglesPerMonitoringStation[curPoint] = mutableListOf()
                asteroidsPerMonitoringStation[curPoint] = mutableMapOf()
                for (sY in map.indices) {
                    for (sX in map[sY].indices) {
                        if (map[sY][sX] == "." || (y == sY && x == sX)) {
                            continue
                        }
                        //HORIZONTAL & VERTICAL LINES
                        if (y == sY || x == sX) {
                            // HORIZONTAL LINE
                            for (verX in x-1 downTo 0) {
                                if (map[y][verX] == "#") {
                                    asteroidsPerMonitoringStation[curPoint]!![Point(verX, y)] = true
                                    break
                                }
                            }
                            for (verX in x+1 until map[y].size) {
                                if (map[y][verX] == "#") {
                                    asteroidsPerMonitoringStation[curPoint]!![Point(verX, y)] = true
                                    break
                                }
                            }
                            //VERTICAL LINE
                            for (horY in y-1 downTo 0) {
                                if (map[horY][x] == "#") {
                                    asteroidsPerMonitoringStation[curPoint]!![Point(x, horY)] = true
                                    break
                                }
                            }
                            for (horY in y+1 until map.size) {
                                if (map[horY][x] == "#") {
                                    asteroidsPerMonitoringStation[curPoint]!![Point(x, horY)] = true
                                    break
                                }
                            }
                        } else {
                            anglesPerMonitoringStation[curPoint]?.add(curPoint.angle(Point(sX, sY)))
                        }
                    }
                }
            }
        }

        val countPerAsteroid = mutableMapOf<Point, Int>()
        for (mutableEntry in asteroidsPerMonitoringStation) {
            var size = mutableEntry.value.size
            val angles = anglesPerMonitoringStation[mutableEntry.key]?.distinct()?.size
            angles?.let { size += angles }
            countPerAsteroid[mutableEntry.key] = size
        }
        return countPerAsteroid
    }

    override fun part1(): String {
        return getBestMonitoringStation(convertInput(Inputs_2019.DAY_10)).values.max().toString()
    }

    override fun part2(): String {
        val map = convertInput(Inputs_2019.DAY_10)
        val monitoringStationPoint = getBestMonitoringStation(map).maxBy { it.value }!!.key
        val allPointsWOMonitoring = map.indices2DFiltered({it == "#"}){y, x ->  Point(x,y)}
        val anglesGrouping = allPointsWOMonitoring.filterNot { it == monitoringStationPoint }
            .groupBy { monitoringStationPoint.angle(it) }
            .map { e -> e.key to e.value.sortedBy { ast -> monitoringStationPoint.dist(ast) }.toMutableList() }
            .sortedByDescending { it.first }
        var lastPointRemoved = Point(0, 0)
        for (i in 0..199) {
            val pointsOnAngle = anglesGrouping[i % anglesGrouping.size].second

            if (pointsOnAngle.isNotEmpty()) {
                lastPointRemoved = pointsOnAngle.removeAt(0)
            }
        }

        return (lastPointRemoved.x * 100 + lastPointRemoved.y).toString()
    }
}
