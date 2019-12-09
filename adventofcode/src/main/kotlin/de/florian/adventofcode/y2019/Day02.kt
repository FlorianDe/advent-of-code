package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay

fun main() {
    Day02().exec()
}

class Day02 : AoCDay() {
    fun convertToOpcodes(input: String): (IntArray) = input.split(",").map { it.toInt() }.toIntArray()

    fun IntArray.run(): IntArray {
        val opcodes = this.copyOf()
        iterate_opcodes@ for (i in opcodes.indices step 4) {
            when (opcodes[i]) {
                1 -> opcodes[opcodes[i + 3]] = opcodes[opcodes[i + 1]] + opcodes[opcodes[i + 2]]
                2 -> opcodes[opcodes[i + 3]] = opcodes[opcodes[i + 1]] * opcodes[opcodes[i + 2]]
                99 -> break@iterate_opcodes
            }
        }
        return opcodes
    }

    fun runOpCodesWithNounVerbSet(opcodes: IntArray, noun: Int, verb: Int): IntArray {
        opcodes[1] = noun
        opcodes[2] = verb
        return opcodes.run()
    }

    fun runOpCodesWithNounVerbRange(
        opcodes: IntArray,
        nounRange: IntRange,
        verbRange: IntRange,
        predicate: (IntArray) -> Boolean = { _ -> false }
    ): IntArray {
        var resOps: IntArray = opcodes
        outerloop@
        for (noun in nounRange) {
            for (verb in verbRange) {
                resOps = runOpCodesWithNounVerbSet(opcodes, noun, verb)
                if (predicate(resOps)) break@outerloop
            }
        }
        return resOps
    }

    val convertToOpcodes = convertToOpcodes(Inputs_2019.DAY_02)

    override fun part1(): String {
        return runOpCodesWithNounVerbSet(convertToOpcodes, 12, 2)[0].toString()
    }

    override fun part2(): String {
        val a = runOpCodesWithNounVerbRange(convertToOpcodes, 0..99, 0..99) { ops -> ops[0] == 19690720 }
        return (100 * a[1] + a[2]).toString()
    }
}