package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.y2019.Direction.*

fun main() {
    Day03().exec()
}

class Day03 : AoCDay(Year.YEAR_2019, Day.DAY_03) {
    private val lines = getLines(Inputs_2019.DAY_03)
    private val result = getIntersectionsAndCenter(lines)

    private fun getLines(input: String) = input
        .split("\n")
        .map { line -> line.split(",") }
        .map { dirs ->
            Line(dirs.map { dir ->
                Pair(
                    valueOf(dir[0].toString()),
                    Integer.parseInt(dir.substring(1 until dir.length))
                )
            })
        }
        .zipWithNext()
        .first()

    override fun part1(): String {
        return result.first
            .filter { it != result.second }
            .map { it.manhattanDistance(result.second) }
            .min().toString()
    }

    override fun part2(): String {
        val stepsToIntersection = mutableSetOf<Pair<Point, Int>>()
        for (intersection in result.first) {
            stepsToIntersection.add(
                Pair(
                    intersection,
                    lines.first.calcSteps(result.second, intersection) + lines.second.calcSteps(
                        result.second,
                        intersection
                    )
                )
            )
        }

        return stepsToIntersection
            .filter { it.second > 0 }
            .minBy { it.second }?.second.toString()
    }

    private fun getIntersectionsAndCenter(lines: Pair<Line, Line>): Pair<MutableSet<Point>, Point> {
        val bb1 = lines.first.boundingBox()
        val bb2 = lines.second.boundingBox()

        val coordOffsetY = Math.min(bb1.first.y, bb2.first.y)
        val coordOffsetX = Math.min(bb1.first.x, bb2.first.x)
        val maxHeight = Math.max(bb1.second.y, bb2.second.y)
        val maxWidth = Math.max(bb1.second.x, bb2.second.x)
        val breadboard = Array(maxHeight - coordOffsetY + 1) { Array(maxWidth - coordOffsetX + 1) { false } }

        val center = Point(-coordOffsetX, -coordOffsetY)

        var curPos = Point(center.x, center.y)
        for (direction in lines.first.directions) {
            for (i in 0 until direction.second) {
                curPos = direction.first.operation(curPos)
                breadboard[curPos.y][curPos.x] = true
            }
        }

        val intersections = mutableSetOf<Point>()
        curPos = Point(center.x, center.y)
        for (direction in lines.second.directions) {
            for (i in 0 until direction.second) {
                curPos = direction.first.operation(curPos)
                if (breadboard[curPos.y][curPos.x]) {
                    intersections.add(curPos.copy())
                }
            }
        }

        return Pair(intersections, center)
    }
}

enum class Direction(val operation: (Point) -> Point) {
    R({ p -> Point(p.x + 1, p.y) }),
    D({ p -> Point(p.x, p.y - 1) }),
    L({ p -> Point(p.x - 1, p.y) }),
    U({ p -> Point(p.x, p.y + 1) })
}

class Line(val directions: List<Pair<Direction, Int>>) {

    fun calcSteps(center: Point, point: Point): Int {
        var steps = 0
        var curPos = Point(center.x, center.y)

        for (direction in directions) {
            for (i in 0 until direction.second) {
                curPos = direction.first.operation(curPos)
                steps++
                if (curPos == point) {
                    return steps
                }
            }
        }
        return -1
    }

    fun boundingBox(): Pair<Point, Point> {
        var minWidth = 0
        var maxWidth = 0

        var minHeight = 0
        var maxHeight = 0

        var curWidth = 0
        var curHeight = 0
        for (direction in directions) {
            when (direction.first) {
                R -> {
                    curWidth += direction.second
                    if (curWidth > maxWidth) maxWidth = curWidth
                }
                D -> {
                    curHeight -= direction.second
                    if (curHeight < minHeight) minHeight = curHeight
                }
                L -> {
                    curWidth -= direction.second
                    if (curWidth < minWidth) minWidth = curWidth
                }
                U -> {
                    curHeight += direction.second
                    if (curHeight > maxHeight) maxHeight = curHeight
                }
            }
        }

        return Pair(Point(minWidth, minHeight), Point(maxWidth, maxHeight))
    }
}