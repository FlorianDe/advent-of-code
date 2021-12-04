package de.florian.adventofcode.y2021

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() { Day01().exec() }

class Day01 : AoCDay(Year.YEAR_2021, Day.DAY_01) {
    private val numbers = getInput().split("\n").map { Integer.parseInt(it.trim()) }

    private fun measurementIncreased(measurements: List<Int>, windowSize: Int = 1, stepSize: Int = 1): Int {
        return measurements
            .windowed(windowSize, stepSize){it.sum()}
            .zipWithNext()
            .count { (a,b) -> a < b }
    }

    override fun part1(): String {
        return "${measurementIncreased(numbers)}"
    }

    override fun part2(): String {
        return "${measurementIncreased(numbers, 3)}"
    }
}