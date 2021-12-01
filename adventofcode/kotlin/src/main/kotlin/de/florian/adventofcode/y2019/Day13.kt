package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.y2019.Day13.Game.Tile.*
import java.math.BigInteger

fun main() {
    Day13().exec()
}

class Day13 : AoCDay() {
    val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_13)

    override fun part1(): String {
        return IntCodeComputer(memory).run().chunked(3).count { it[2].toInt() == BLOCK.typeId }.toString()
    }

    override fun part2(): String {
        memory[0] = BigInteger.valueOf(2)
        return Game(IntCodeComputer(memory)).run().toString()
    }

    class Game(val computer: IntCodeComputer) {
        companion object {
            val SCORE_POINT_POS = Point(-1, 0)
        }

        var ball = Point(-1, -1)
        var paddle = Point(-1, -1)
        var field = Array(1) { Array(1) { EMPTY } }
        var score = 0

        fun run(): Int {
            while (!computer.halted) {
                var timerStart = System.currentTimeMillis()
                while (!computer.halted && computer.outputs.size < 3) {
                    computer.step()
                }
                if (!this.computer.halted && computer.outputs.size == 3) {
                    val cell = readGameInput()
                    if (cell.first == SCORE_POINT_POS) {
                        this.score = cell.second
                    } else {
                        if (this.field.size <= cell.first.y) {
                            val sizeDiff = cell.first.y - this.field.size + 1
                            this.field = this.field.plus(Array(sizeDiff) { Array(this.field[0].size) { EMPTY } })
                        } else if (this.field[0].size <= cell.first.x) {
                            val sizeDiff = cell.first.x - this.field[0].size + 1
                            for (i in this.field.indices) {
                                this.field[i] = this.field[i].plus(Array(sizeDiff) { EMPTY })
                            }
                        }

                        val cellType = Tile.store.of(cell.second)
                        val p = cell.first
                        when (cellType) {
                            EMPTY, WALL, BLOCK -> field[p.y][p.x]
                            PADDLE -> paddle.setPos(p.x, p.y)
                            BALL -> {
                                ball.setPos(p.x, p.y)
                                writeGameOutput(paddle, ball)
                            }
                        }
                    }
                }
            }
            return score
        }

        fun readGameInput(): Pair<Point, Int> {
            val x = this.computer.outputs.take().toInt()
            val y = this.computer.outputs.take().toInt()
            val value = this.computer.outputs.take().toInt()

            return Pair(Point(x, y), value)
        }

        fun writeGameOutput(paddle: Point, ball: Point) {
            var output = 0
            if (paddle.x < ball.x) {
                output = 1
            } else if (paddle.x > ball.x) {
                output = -1
            }
            this.computer.inputs.put(output.toBigInteger())
        }

        enum class Tile(val typeId: Int) {
            EMPTY(0),
            WALL(1),
            BLOCK(2),
            PADDLE(3),
            BALL(4);

            companion object {
                val store = CollectionsUtil.Store(values()) { it.typeId to it }
            }
        }
    }
}



