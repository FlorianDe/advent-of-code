package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import java.math.BigInteger

fun main() {
    Day09().exec()
}

class Day09 : AoCDay() {

    companion object {
        const val PREAMBLE_SIZE = 25
    }

    private val numbers: Array<Long> = Inputs_2020.DAY_09.split("\n").map { it.trim().toLong() }.toTypedArray()

    private fun calcInvalidNumber(): Long? {
        for (i in PREAMBLE_SIZE until numbers.size) {
            var valid = false
            for (c1 in 1..PREAMBLE_SIZE)
                for (c2 in 1..PREAMBLE_SIZE)
                    if (c1 != c2)
                        if (numbers[i - c1] + numbers[i - c2] == numbers[i])
                            valid = true
            if (!valid) return numbers[i]
        }
        return null
    }

    override fun part1(): String {
        return calcInvalidNumber()?.toString() ?: throw Error("All inputs are valid!.")
    }

    override fun part2(): String {
        val invalidNumber: BigInteger = calcInvalidNumber()?.toBigInteger() ?: throw Error("All inputs are valid!.")

        for (startIdx in numbers.indices) {
            val start = numbers[startIdx]
            var min = start
            var max = start
            var sum: BigInteger = start.toBigInteger()
            for (endIdx in startIdx + 1 until numbers.size) {
                val end = numbers[endIdx]
                sum = sum.plus(end.toBigInteger())
                if (sum > invalidNumber) break
                if (min > end) min = end
                if (max < end) max = end
                if (sum == invalidNumber) return (min + max).toString()
            }
        }
        throw Error("No contiguous set of at least two numbers found.")
    }
}