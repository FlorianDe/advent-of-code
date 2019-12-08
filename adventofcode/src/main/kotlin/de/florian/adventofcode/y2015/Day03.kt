package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.Collection.Store

fun main() {
    Day03().exec()
}

class Day03 : AoCDay() {
    private fun convertInput(input: String) = input.split("").filter { it.isNotBlank() }
    private fun convertInputForBoth(input: String) = convertInput(input).chunked(2).map { it.zipWithNext().first() }

    enum class Direction(val symbol: String, val dir: Point) {
        NORTH("^", Point(1, 0)),
        SOUTH("v", Point(-1, 0)),
        WEST("<", Point(0, -1)),
        EAST(">", Point(0, 1));

        companion object {
            val retriever = Store(values()) { it.symbol to it }
        }
    }

    data class Point(var x: Int, var y: Int) {
        fun add(p: Point): Point {
            this.x += p.x
            this.y += p.y
            return this
        }
    }

    override fun part1(): String {
        val posSanta = Point(0, 0)
        val houses = hashSetOf(posSanta)
        convertInput(Inputs_2015.DAY_03)
            .filter{ it.isNotBlank() }
            .forEach { houses.add(posSanta.add(Direction.retriever.of(it).dir).copy()) }

        return houses.size.toString()
    }

    override fun part2(): String {
        val posSanta = Point(0, 0)
        val posRobo = Point(0, 0)
        val houses = hashSetOf(Point(0,0))
        convertInputForBoth(Inputs_2015.DAY_03).forEach {
            houses.add(posSanta.add(Direction.retriever.of(it.first).dir).copy())
            houses.add(posRobo.add(Direction.retriever.of(it.second).dir).copy())
        }

        return houses.size.toString()
    }
}