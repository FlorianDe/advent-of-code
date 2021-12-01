package de.florian.adventofcode.y2020

import de.florian.adventofcode.y2020.Operation.*

enum class Operation(val token: String) {
    NOP("nop"),
    ACC("acc"),
    JMP("jmp"),
}

data class Instruction(var operation: Operation, var value: Long)

class BootCodeInterpreter(
        codeInput: Array<Instruction>,
        var accumulator: Long = 0,
        var pos: Int = 0
) {
    private val code = codeInput.map { Instruction(it.operation, it.value) }

    companion object {
        private val OP_PARSER_REGEX = Regex("([a-z]+) ([+-]\\d+)")
        fun parseInput(input: String) : Array<Instruction> {
            return input.split("\n").map {
                val match = OP_PARSER_REGEX.find(it.trim()) ?: throw Error("PARSING ERROR")
                val (operation, value) = match.destructured
                Instruction(valueOf(operation.toUpperCase()), value.toLong())
            }.toTypedArray()
        }
    }

    fun step() : Int {
        if(pos < code.size) {
            val instr = code[pos]
            when (instr.operation) {
                NOP -> pos += 1
                ACC -> {
                    accumulator += instr.value
                    pos += 1
                }
                JMP -> pos += instr.value.toInt()
            }
        }
        return pos
    }

    fun run() : Int {
        val seenPositions = mutableSetOf<Int>()
        var nextPos = 0
        do {
            seenPositions.add(this.pos)
            nextPos = this.step()
        } while (!seenPositions.contains(nextPos))

        return nextPos
    }
}