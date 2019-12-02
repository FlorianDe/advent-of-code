package de.florian.adventofcode.y2019

fun main() {
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
        predicate: (IntArray, Int, Int) -> Boolean = { _, _, _ -> false }
    ) {
        outerloop@
        for (noun in nounRange) {
            for (verb in verbRange) {
                val resOps = runOpCodesWithNounVerbSet(opcodes, noun, verb)
                if (predicate(resOps, noun, verb)) break@outerloop
            }
        }
    }


    val convertToOpcodes = convertToOpcodes(INPUT_DAY_02)

    val print1 = runOpCodesWithNounVerbSet(convertToOpcodes, 12, 2)[0]
    println("Print 1: $print1")

    runOpCodesWithNounVerbRange(convertToOpcodes, 0..99, 0..99) { ops, noun, verb ->
        val res = ops[0] == 19690720
        if (res) println("Print 2: ${100 * noun + verb}")
        res
    }
}