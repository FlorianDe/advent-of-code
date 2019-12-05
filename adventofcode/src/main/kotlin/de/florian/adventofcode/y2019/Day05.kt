package de.florian.adventofcode.y2019

import com.sun.javaws.exceptions.InvalidArgumentException
import de.florian.adventofcode.y2019.Instruction.*
import kotlin.math.pow

fun main() {
    val day05 = Day05()
    println("Part1: ${day05.part1()}")
    println("Part2: ${day05.part2()}")
}

class Day05 {
    val memory = convertToOpcodes(INPUT_DAY_05)

    fun convertToOpcodes(input: String): (IntArray) = input.split(",").map { it.toInt() }.toIntArray()

    fun part1(): String {
        val computerProgram = ComputerProgram(memory)
        computerProgram.input = 1
        return computerProgram.run().last().toString()
    }

    fun part2(): String {
        val computerProgram = ComputerProgram(convertToOpcodes(INPUT_DAY_05))
        computerProgram.input = 5
        return computerProgram.run().last().toString()
    }
}

enum class Instruction(val opCode: Int, val parameterCount: Int = 0) {
    ADD(1, 3),
    MULTIPLY(2, 3),
    STORE(3, 1),
    OUTPUT(4, 1),
    JIT(5, 2),
    JIF(6, 2),
    LT(7, 3),
    EQ(8, 3),
    STOP(99);

    companion object {
        val map : Map<Int, Instruction> = values().map { it.opCode to it }.toMap()

        fun valueOfOpCode(opCode: Int): Instruction {
            map[opCode]?.let { return it }
            throw InvalidArgumentException(arrayOf("Object with id $opCode not supported atm."))
        }
    }
}

enum class ParameterMode(val modeCode: Int) {
    POSITION(0),
    IMMEDIATE(1);

    companion object {
        val map = values().map { it.modeCode to it }.toMap()

        fun valueOfModeCode(modeCode: Int): ParameterMode {
            map[modeCode]?.let { return it }
            throw InvalidArgumentException(arrayOf("Object with id $modeCode not supported atm."))
        }
    }
}

class Operation(memory: Int) {
    val instruction : Instruction
    val parameters: Array<ParameterMode>

    init {
        this.instruction = Instruction.valueOfOpCode(memory % 100)
        if(this.instruction.parameterCount < 1){
            this.parameters = emptyArray()
        } else {
            parameters = Array(instruction.parameterCount) {
                ParameterMode.valueOfModeCode(memory/10.0.pow(it+2).toInt()%10)
            }
        }
    }
}

class ComputerProgram(memory: IntArray) {
    val opcodes = memory.copyOf()
    var input = 0
    var diagnosticCode = mutableListOf<Int>()

    fun run() : List<Int> {
        var op: Operation
        var pos = 0
        var resultAddress = 0
        do {
            op = Operation(opcodes[pos])

            var posModified = false
            if(op.parameters.isNotEmpty()){
                val resultParameter = op.parameters[op.parameters.size - 1]
                assert(resultParameter != ParameterMode.IMMEDIATE)
                resultAddress = getAddress(resultParameter, pos+op.parameters.size)
            }
            when(op.instruction){
                ADD -> opcodes[resultAddress] =  getValue(op, pos, 1) + getValue(op, pos, 2)
                MULTIPLY -> opcodes[resultAddress] = getValue(op, pos, 1) * getValue(op, pos, 2)
                STORE -> opcodes[resultAddress] =  input
                JIT -> if( getValue(op, pos, 1) != 0) {
                    pos = getValue(op, pos, 2)
                    posModified=true
                }
                JIF -> if( getValue(op, pos, 1) == 0) {
                    pos = getValue(op, pos, 2)
                    posModified=true
                }
                LT -> opcodes[resultAddress] = if( getValue(op, pos, 1) < getValue(op, pos, 2)) 1 else 0
                EQ -> opcodes[resultAddress] = if( getValue(op, pos, 1) == getValue(op, pos, 2)) 1 else 0
                OUTPUT -> diagnosticCode.add(opcodes[getAddress(op.parameters[op.parameters.size-1], pos+op.parameters.size)])
            }
            if(!posModified) {
                pos += op.instruction.parameterCount + 1
            }
        } while(op.instruction != STOP)

        return diagnosticCode
    }

    private fun getValue(operation: Operation, pos: Int, param: Int) : Int{
        return opcodes[getAddress(operation.parameters[param-1], pos+param)]
    }

    private fun getAddress(paramMode: ParameterMode, address: Int) : Int{
        return if (paramMode == ParameterMode.IMMEDIATE) address else opcodes[address]
    }
}