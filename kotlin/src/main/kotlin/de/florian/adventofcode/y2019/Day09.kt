package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.CollectionsUtil
import java.math.BigInteger


fun main() {
    Day09().exec()
}

class Day09 : AoCDay(Year.YEAR_2019, Day.DAY_09) {
    private val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_09)
    override fun part1(): String {
        return IntCodeComputer(memory, listOf(BigInteger.ONE)).also { it.run() }.outputs.last().toString()
    }

    override fun part2(): String {
        return IntCodeComputer(memory, listOf(BigInteger.valueOf(2))).also { it.run() }.outputs.last().toString()
    }
}
