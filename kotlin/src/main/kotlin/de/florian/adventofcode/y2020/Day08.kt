package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() {
    Day08().exec()
}

class Day08 : AoCDay(Year.YEAR_2020, Day.DAY_08) {

    override fun part1(): String {
        val interpreter = BootCodeInterpreter(BootCodeInterpreter.parseInput(Inputs_2020.DAY_08)).apply { run() }
        return interpreter.accumulator.toString()
    }

    override fun part2(): String {
        val code = BootCodeInterpreter.parseInput(Inputs_2020.DAY_08)

        for (line in code.indices) {
            val op = code[line].operation
            if (op == Operation.JMP || op == Operation.NOP) {
                val mutatedCode = code.mapIndexed { index, instruction ->
                    if (index == line) {
                        if (instruction.operation == Operation.NOP) {
                            Instruction(Operation.JMP, instruction.value)
                        } else {
                            Instruction(Operation.NOP, instruction.value)
                        }
                    } else {
                        instruction
                    }
                }.toTypedArray()

                val interpreter = BootCodeInterpreter(mutatedCode)
                if (interpreter.run() == code.size) {
                    return interpreter.accumulator.toString()
                }
            }
        }
        throw Error("Couldn't calculate any result.")
    }
}