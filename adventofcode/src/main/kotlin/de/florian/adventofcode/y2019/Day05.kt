package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.Collection.Store
import de.florian.adventofcode.y2019.Instruction.*
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.pow

fun main() { Day05().exec() }

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
        val store = Store(values()) {it.opCode to it}
    }
}

enum class ParameterMode(val modeCode: Int) {
    POSITION(0),
    IMMEDIATE(1);

    companion object {
        val store = Store(values()) {it.modeCode to it}
    }
}

class Operation(memory: Int) {
    val instruction : Instruction
    val parameters: Array<ParameterMode>

    init {
        this.instruction = Instruction.store.of(memory % 100)
        if(this.instruction.parameterCount < 1){
            this.parameters = emptyArray()
        } else {
            parameters = Array(instruction.parameterCount) {
                ParameterMode.store.of(memory/10.0.pow(it+2).toInt()%10)
            }
        }
    }
}

class ComputerProgram(memory: IntArray, val name: String = "default-name") : Callable<Pair<String, Int>> {
    val opcodes = memory.copyOf()
    var inputs = LinkedBlockingQueue<Int>()
    var diagnosticCode = mutableListOf<Int>()
    var outputs = LinkedBlockingQueue<Int>()

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
                STORE -> opcodes[resultAddress] =  inputs.take()
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
                OUTPUT -> {
                    val output = opcodes[getAddress(op.parameters[op.parameters.size - 1], pos + op.parameters.size)]
                    diagnosticCode.add(output)
                    outputs.put(output)
                }
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

    override fun call(): Pair<String, Int> {
        return Pair(name, run().last())
    }
}