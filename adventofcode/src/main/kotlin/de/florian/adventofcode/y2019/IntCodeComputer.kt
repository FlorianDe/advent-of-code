package de.florian.adventofcode.y2019

import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.y2019.Instruction.*
import de.florian.adventofcode.y2019.ParameterMode.*
import java.math.BigInteger
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.pow

enum class Instruction(val opCode: Int, val parameterCount: Int = 0) {
    ADD(1, 3),
    MULTIPLY(2, 3),
    STORE(3, 1),
    OUTPUT(4, 1),
    JIT(5, 2),
    JIF(6, 2),
    LT(7, 3),
    EQ(8, 3),
    ARB(9, 1),
    STOP(99);

    companion object {
        val store = CollectionsUtil.Store(values()) { it.opCode to it }
    }
}

enum class ParameterMode(val modeCode: Int) {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2)
;
    companion object {
        val store = CollectionsUtil.Store(values()) { it.modeCode to it }
    }
}

class Operation(memory: Int) {
    val instruction: Instruction
    val parameters: Array<ParameterMode>

    init {
        this.instruction = Instruction.store.of(memory % 100)
        if (this.instruction.parameterCount < 1) {
            this.parameters = emptyArray()
        } else {
            parameters = Array(instruction.parameterCount) {
                ParameterMode.store.of(memory / 10.0.pow(it + 2).toInt() % 10)
            }
        }
    }
}

class ComputerProgram(memory: Array<BigInteger>, inputs : List<BigInteger> = emptyList(), val name: String = "default-name") : Callable<Pair<String, BigInteger>> {
    var memory = memory.copyOf()
    var inputs = LinkedBlockingQueue<BigInteger>(inputs)
    var diagnosticCode = mutableListOf<BigInteger>()
    var outputs = LinkedBlockingQueue<BigInteger>()

    fun run(): List<BigInteger> {
        var op: Operation
        var pos = 0
        var relativeBase = 0
        var resultAddress = 0
        do {
            //TODO REFACTOR
            op = Operation(memory[pos].toInt())

            var posModified = false
            if (op.parameters.isNotEmpty()) {
                val resultParameter = op.parameters[op.parameters.size - 1]
                assert(resultParameter != IMMEDIATE)
                resultAddress = getAddress(resultParameter, pos + op.parameters.size, relativeBase)
            }
            when (op.instruction) {
                ADD -> memory[resultAddress] = getValue(op, pos, relativeBase, 1) + getValue(op, pos, relativeBase, 2)
                MULTIPLY -> memory[resultAddress] = getValue(op, pos, relativeBase, 1) * getValue(op, pos, relativeBase, 2)
                STORE -> memory[resultAddress] = inputs.take()
                JIT -> if (getValue(op, pos, relativeBase, 1) != BigInteger.ZERO) {
                    pos = getValue(op, pos, relativeBase, 2).toInt()
                    posModified = true
                }
                JIF -> if (getValue(op, pos, relativeBase, 1) == BigInteger.ZERO) {
                    pos = getValue(op, pos, relativeBase, 2).toInt()
                    posModified = true
                }
                LT -> memory[resultAddress] = if (getValue(op, pos, relativeBase, 1) < getValue(op, pos, relativeBase, 2)) BigInteger.ONE else BigInteger.ZERO
                EQ -> memory[resultAddress] = if (getValue(op, pos, relativeBase, 1) == getValue(op, pos, relativeBase, 2)) BigInteger.ONE else BigInteger.ZERO
                ARB -> relativeBase += getValue(op, pos, relativeBase, 1).toInt()
                OUTPUT -> {
                    val output = memory[getAddress(op.parameters[op.parameters.size - 1], pos + op.parameters.size, relativeBase)]
                    diagnosticCode.add(output)
                    outputs.put(output)
                }
            }
            if (!posModified) {
                pos += op.instruction.parameterCount + 1
            }
        } while (op.instruction != STOP)

        return diagnosticCode
    }

    private fun getValue(operation: Operation, pos: Int, relativeAddress: Int, param: Int): BigInteger {
        return memory[getAddress(operation.parameters[param - 1], pos + param, relativeAddress)]
    }

    private fun getAddress(paramMode: ParameterMode, address: Int, relativeAddress: Int): Int {
        val addr = when(paramMode){
            POSITION -> memory[address].toInt()
            IMMEDIATE -> address
            RELATIVE -> relativeAddress+memory[address].toInt()
        }
        assert(addr>0)
        if(addr>=memory.size){
            memory = memory.plus(Array<BigInteger>(addr-memory.size+1) {BigInteger.ZERO})
        }
        return addr
    }

    override fun call(): Pair<String, BigInteger> {
        return Pair(name, run().last())
    }
}