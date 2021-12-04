package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Grid
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.y2019.Day15.Tile.*
import java.math.BigInteger

fun main() {
    Day15().exec()
}

class Day15 : AoCDay(Year.YEAR_2019, Day.DAY_15) {
    companion object {
        val NORTH = BigInteger.valueOf(1)
        val SOUTH = BigInteger.valueOf(2)
        val WEST = BigInteger.valueOf(3)
        val EAST = BigInteger.valueOf(4)

        val dirMap = mapOf(
            Point(0, 1) to NORTH,
            Point(0, -1) to SOUTH,
            Point(-1, 0) to WEST,
            Point(1, 0) to EAST
        )
    }

    val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_15)

    override fun part1(): String {
        val points = getMapPoints()
        val grid = Grid(points.keys, { points[it] ?: Wall }, { it == Wall })
        printMap(grid)
        return grid.shortestPath(grid.offset, OxygenSystem).size.toString()
    }

    override fun part2(): String {
        val points = getMapPoints(false)
        val grid = Grid(points.keys, { points[it] ?: Wall }, { it == Wall })
        printMap(grid)
        var osSpaces = mutableSetOf<Point>()
        var freeSpaces = 0
        for(y in grid.cells.indices){
            for(x in grid.cells.indices){
                if(grid.cells[y][x]==OxygenSystem){
                    osSpaces.add(Point(x,y))
                } else if(grid.cells[y][x]==Empty){
                    freeSpaces++
                }
            }
        }
        var newOsSpaces = mutableSetOf<Point>()
        var filled = 0
        var day = 0
        while(freeSpaces > filled){
            for (os in osSpaces){
                val neighbours = grid.getNeighbours(os)
                for (n in neighbours){
                    if(grid.cells[n.y][n.x] == Empty){
                        grid.cells[n.y][n.x] = OxygenSystem
                        filled++
                        newOsSpaces.add(n)
                    }
                }
            }
            osSpaces = HashSet(newOsSpaces)
            newOsSpaces = mutableSetOf()
            day++
        }
        return day.toString()
    }

    fun printMap(grid: Grid<Tile>) {
        println("Map size: [${grid.cells.size}x${grid.cells[0].size}]")
        grid.cells.forEach {
            println(it.joinToString(" ") {
                when (it) {
                    Wall -> "#"
                    Empty -> " "
                    OxygenSystem -> "O"
                }
            })
        }
    }

    fun getMapPoints(breakOnFirst: Boolean = true): MutableMap<Point, Tile> {
        val osSet = mutableSetOf<Point>()
        var steps = 0
        var maxWastedSteps = 10_000 //Could come in handy if there would be some more OS tanks

        val points = mutableMapOf<Point, Tile>()
        var dir = Point(0, 1)
        var pos = Point.zero()
        val computer = IntCodeComputer(memory)
        computer.inputs.put(dirMap[dir]!!)
        points[pos] = Empty
        outerloop@ while (!computer.halted) {
            steps++
            while (!computer.halted && computer.outputs.size < 1) {
                computer.step()
            }
            if (!computer.halted && computer.outputs.size == 1) {
                val x = Tile.store.of(computer.outputs.take())
                val curNewPos = pos + dir
                points[curNewPos] = x
                when (x) {
                    Wall -> {
                        dir = dir.rotate(Point.Angle.DEG_270)
                        computer.inputs.put(dirMap[dir]!!)
                    }
                    Empty -> {
                        pos = curNewPos
                        dir = dir.rotate(Point.Angle.DEG_90)
                        computer.inputs.put(dirMap[dir]!!)
                    }
                    OxygenSystem -> {
                        if (breakOnFirst) {
                            break@outerloop
                        }

                        pos = curNewPos
                        dir = dir.rotate(Point.Angle.DEG_90)
                        computer.inputs.put(dirMap[dir]!!)
                        if (!osSet.contains(curNewPos)) {
                            osSet.add(curNewPos)
                            steps = 0
                        } else if (steps > maxWastedSteps) {
                            break@outerloop
                        }
                    }
                }
            }
        }
        return points
    }

    enum class Tile(val type: BigInteger) {
        Wall(BigInteger.valueOf(0)),
        Empty(BigInteger.valueOf(1)),
        OxygenSystem(BigInteger.valueOf(2));

        companion object {
            val store = CollectionsUtil.Store(values()) { it.type to it }
        }
    }
}