package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Point
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun main() {
    Day11().exec()
}

class Day11 : AoCDay() {
    companion object {
        @JvmField
        val BLACK: BigInteger = BigInteger.ZERO
        @JvmField
        val WHITE: BigInteger = BigInteger.ONE
    }

    val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_11)

    override fun part1(): String {
        return (runPainter(BLACK).keys.size).toString()
    }

    override fun part2(): String {
        val points = runPainter(WHITE)

        var offset = Point(0, 0)
        var max = Point(0, 0)
        for (p in points.keys) {
            offset = Point.minValues(offset, p)
            max = Point.maxValues(max, p)
        }

        val array = Array(max.y - offset.y + 1) { Array(max.x - offset.x + 1) { false } }
        points.forEach {
            array[it.key.y - offset.y][it.key.x - offset.x] = it.value == BigInteger.ONE
        }

        var license = "\n"
        for (lines in array) {
            for (p in lines) {
                license += if (p) "#" else " "
            }
            license += "\n"
        }

        return license
    }

    fun runPainter(startColor: BigInteger): Map<Point, BigInteger> {
        val comp = IntCodeComputer(memory)

        val painter = thread {
            comp.run()
        }

        return run {
            var curDir = Point(0, 1)
            val painted = mutableMapOf<Point, BigInteger>()
            var pos = Point(0, 0)

            comp.inputs.put(startColor)
            while (!painter.isInterrupted) {
                val color = comp.outputs.poll(1, TimeUnit.SECONDS)
                val direction = comp.outputs.poll(1, TimeUnit.SECONDS)

                if (color == null || direction == null) {
                    break
                }

                when (direction) {
                    BigInteger.ZERO -> curDir = curDir.rotate(Point.Angle.DEG_270)
                    BigInteger.ONE -> curDir = curDir.rotate(Point.Angle.DEG_90)
                }

                painted[pos.copy()] = color
                pos = curDir
                comp.inputs.put(painted.getOrDefault(pos, BLACK))
            }

            painted
        }
    }
}