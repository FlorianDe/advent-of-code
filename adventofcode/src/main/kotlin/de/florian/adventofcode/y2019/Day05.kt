package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay

fun main() {
    Day05().exec()
}

class Day05 : AoCDay() {
    private val memory = convertToOpcodes(Inputs_2019.DAY_05)

    private fun convertToOpcodes(input: String): (IntArray) = input.split(",").map { it.toInt() }.toIntArray()

    override fun part1(): String {
        val computerProgram = ComputerProgram(memory)
        computerProgram.inputs.put(1)
        return computerProgram.run().last().toString()
    }

    override fun part2(): String {
        val computerProgram = ComputerProgram(convertToOpcodes(Inputs_2019.DAY_05))
        computerProgram.inputs.put(5)
        return computerProgram.run().last().toString()
    }
}