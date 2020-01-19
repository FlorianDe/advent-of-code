package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.y2019.Day15.Tile.*
import java.math.BigInteger

fun main() {
    Day15().exec()
}

class Day15 : AoCDay() {
    companion object {
        val NORTH = BigInteger.valueOf(1)
        val SOUTH = BigInteger.valueOf(2)
        val WEST = BigInteger.valueOf(3)
        val EAST = BigInteger.valueOf(4)
    }

    val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_15)

    override fun part1(): String {
        val dirMap = mapOf(
            Point(0, 1) to NORTH,
            Point(0, -1) to SOUTH,
            Point(-1, 0) to WEST,
            Point(1, 0) to EAST
        )
        val points = mutableMapOf<Point, Tile>()
        var dir = Point(0, 1)
        var pos = Point.zero()
        val computer = IntCodeComputer(memory)
        computer.inputs.put(dirMap[dir]!!)
        points[pos] = Empty
        println("Start")
        outerloop@while (!computer.halted) {
            while (!computer.halted && computer.outputs.size < 1) {
                computer.step()
            }
            if (!computer.halted && computer.outputs.size == 1) {
                val x = Tile.store.of(computer.outputs.take())
                points[pos + dir] = x
                print("Tile: $x  --> ")
                when (x) {
                    Wall -> {
                        dir = dir.rotate(Point.Angle.DEG_270)
                        computer.inputs.put(dirMap[dir]!!)
                    }
                    Empty -> {
                        pos = pos + dir
                        dir = dir.rotate(Point.Angle.DEG_90)
                        computer.inputs.put(dirMap[dir]!!)
                    }
                    OxygenSystem -> break@outerloop
                }
                println("pos: ${pos}, dir: ${dirMap[dir]!!}")
            }
        }

        return points.toString()
    }

    enum class Tile(val type: BigInteger) {
        Wall(BigInteger.valueOf(0)),
        Empty(BigInteger.valueOf(1)),
        OxygenSystem(BigInteger.valueOf(2));

        companion object {
            val store = CollectionsUtil.Store(values()) { it.type to it }
        }
    }

    override fun part2(): String {
        return ""
    }
}