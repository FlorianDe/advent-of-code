package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Grid
import de.florian.adventofcode.util.Point
import java.lang.StringBuilder
import kotlin.math.max

fun main() {
    Day17(Inputs_2019.DAY_17).exec()
}

class Day17(inputStr: String) : AoCDay() {
    val memory = CollectionsUtil.getMemory(inputStr)

    override fun part1(): String {
        val computer = IntCodeComputer(memory)

        var pos = Point.zero()
        var walker = Point.zero()
        val points = hashSetOf<Point>()
        val crossings = hashSetOf<Point>()
        val sb = StringBuilder()
        var camDim = Point.zero()
        while (!computer.halted) {
            while (!computer.halted && computer.outputs.size < 1) {
                computer.step()
            }
            if (!computer.halted && computer.outputs.size == 1) {
                val pixel = computer.outputs.take().toInt()
                pos = when(pixel){
                    10 -> {
                        camDim = Point(max(pos.x, camDim.x), max(pos.y, camDim.y))
                        Point(0, pos.y+1)
                    }
                    35 -> {
                        points.add(pos.copy())
                        Point(pos.x+1, pos.y)
                    }
                    46 -> Point(pos.x+1, pos.y)
                    94, 86, 60, 62 -> {
                        walker = pos.copy()
                        Point(pos.x+1, pos.y)
                    }
                    else -> Point(pos.x+1, pos.y)
                }
                sb.append(pixel.toChar())
            }
        }

        for(y in 1 until camDim.y) {
            for(x in 1 until camDim.x) {
                val curPos = Point(x,y)
                if(points.contains(curPos) && Grid.NEUMANN_NEIGHBOURHOOD.all {  points.contains(curPos+it) }){
                    crossings.add(curPos)
                }
            }
        }
        return crossings.map { it.x * it.y }.sum().toString()
    }

    override fun part2(): String {
        return "TODO part2"
    }
}
