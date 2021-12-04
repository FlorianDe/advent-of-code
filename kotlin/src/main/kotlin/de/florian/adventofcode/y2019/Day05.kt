package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.CollectionsUtil
import java.math.BigInteger

fun main() {
    Day05().exec()
}

class Day05 : AoCDay(Year.YEAR_2019, Day.DAY_05) {
    private val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_05)

    override fun part1(): String {
        val computerProgram = IntCodeComputer(memory)
        computerProgram.inputs.put(BigInteger.ONE)
        return computerProgram.run().last().toString()
    }

    override fun part2(): String {
        val computerProgram = IntCodeComputer(memory)
        computerProgram.inputs.put(BigInteger.valueOf(5))
        return computerProgram.run().last().toString()
    }
}