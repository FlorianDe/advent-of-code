package de.florian.adventofcode.y2021

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.Point

fun main() { Day02().exec() }

data class ShipData(val position: Int, val depth: Int, val aim: Int)

class Day02 : AoCDay(Year.YEAR_2021, Day.DAY_02) {
    private val directions = getInput().split("\n").map { action ->
        action.split(" ").run {
            val instruction = this[0]
            val amount = this[1].toInt()
            when(instruction){
                "forward" -> Point(amount, 0)
                "down" -> Point(0, amount)
                "up" -> Point(0, -amount)
                else -> throw Error("Instruction not supported!")
            }
        }
    }

    override fun part1(): String {
        return directions.reduce { acc, point -> acc.plus(point) }.run { "${this.x * this.y}" }
    }

    override fun part2(): String {
        return directions.fold(
            initial = ShipData(0,0,0),
            operation = {
                acc: ShipData, direction: Point ->  ShipData(
                    position = acc.position + direction.x,
                    depth = acc.depth + (direction.x * acc.aim),
                    aim = acc.aim + direction.y
            )
            }
        ).run {  "${this.position * this.depth}" }
    }
}