package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() {
    Day06().exec()
}

class Day06 : AoCDay(Year.YEAR_2015, Day.DAY_06) {
    companion object {
        const val TURN_ON = "turn on"
        const val TURN_OFF = "turn off"
        const val TOGGLE = "toggle"
    }

    private val inputRegex =
        """($TURN_ON|$TURN_OFF|$TOGGLE) (\d{1,3})[,](\d{1,3}) through (\d{1,3})[,](\d{1,3})""".toRegex()

    private fun convertInput(input: String): List<Operation> = input.split("\n").map {
        val groups = inputRegex.find(it)?.groups

        lateinit var operation: Operation

        groups?.let { g ->
            g[1]?.let { op ->
                g[2]?.let { fromY ->
                    g[3]?.let { fromX ->
                        g[4]?.let { toY ->
                            g[5]?.let { toX ->
                                operation = Operation(
                                    op.value,
                                    Point(Integer.valueOf(fromY.value), Integer.valueOf(fromX.value)),
                                    Point(Integer.valueOf(toY.value), Integer.valueOf(toX.value))
                                )
                            }
                        }
                    }
                }
            }
        }
        operation
    }

    data class Point(var y: Int, var x: Int)

    class Operation(val op: String, val from: Point, val to: Point)

    fun getBrightnessLevel(rules: (Operation, Array<Array<Int>>, Int, Int) -> Int): String {
        val board = Array(1000) { Array(1000) { 0 } }

        convertInput(Inputs_2015.DAY_06).forEach {
            for (y in it.from.y..it.to.y) {
                for (x in it.from.x..it.to.x) {
                    board[y][x] = rules(it, board, y, x)
                }
            }
        }

        return board.map { it.asSequence().sum() }.sum().toString()
    }

    override fun part1(): String {
        return getBrightnessLevel() { op, board, y, x ->
            when (op.op) {
                TURN_ON -> 1
                TURN_OFF -> 0
                TOGGLE -> if (board[y][x] == 0) 1 else 0
                else -> throw IllegalArgumentException("EERRRROOOR!")
            }
        }
    }

    override fun part2(): String {
        return getBrightnessLevel() { op, board, y, x ->
            when (op.op) {
                TURN_ON -> board[y][x]+1
                TURN_OFF -> if(board[y][x]<=1) 0 else board[y][x]-1
                TOGGLE -> board[y][x]+2
                else -> throw IllegalArgumentException("EERRRROOOR!")
            }
        }
    }
}