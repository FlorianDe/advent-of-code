package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import java.math.BigInteger


fun main() {
    Day09().exec()
}

class Day09 : AoCDay() {
    private val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_09)
    override fun part1(): String {
        return ComputerProgram(memory, listOf(BigInteger.ONE)).also { it.run() }.outputs.last().toString()
    }

    override fun part2(): String {
        return ComputerProgram(memory, listOf(BigInteger.valueOf(2))).also { it.run() }.outputs.last().toString()
    }
}
