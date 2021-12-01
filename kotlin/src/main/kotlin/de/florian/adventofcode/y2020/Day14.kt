import de.florian.adventofcode.AoCDay

fun main() {
    Day14().exec()
}

class Day14 : AoCDay() {
    companion object {
        val MEM_ADDRESS_VALUE_REGEX = Regex("\\[(\\d+)] = (\\d+)")
    }

    data class Memory(val address: String, val value: Long)
    data class InstructionSet(val mask: String, val memories: List<Memory>)

    // Using a String Map since Kotlin cannot handle >32bit Indices//Map<String, Long> =
    private val instructions = Inputs_2020.DAY_14.split("mask =").map { instr ->
        instr.split("mem").let {
            val memories = it.drop(1).map {
                val (addr, value) = MEM_ADDRESS_VALUE_REGEX.find(it)!!.destructured
                Memory(addr, value.toLong())
            }
            InstructionSet(it.first().trim(), memories)
        }
    }

    private fun computeMaskValue(mask: String, value: Long): Long {
        val binaryVal = value.toString(2)

        return mask.mapIndexed { idx, c ->
            if (c == 'X') {
                val charIdx = binaryVal.length - mask.length + idx
                if (0 <= charIdx && charIdx < binaryVal.length) {
                    binaryVal[charIdx]
                } else {
                    '0'
                }
            } else {
                c
            }
        }.joinToString("").toLong(2)
    }

    override fun part1(): String {
        return instructions.fold(mutableMapOf<String, Long>()) { acc, instructionSet ->
            instructionSet.memories.forEach { instr ->
                acc[instr.address] = computeMaskValue(instructionSet.mask, instr.value)
            }
            acc
        }.values.sum().toString()
    }

    override fun part2(): String {
        return ""
    }
}