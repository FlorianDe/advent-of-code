package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import kotlin.system.measureTimeMillis

fun main() {
    println("Took ${measureTimeMillis { Day05().exec()}} ms")
}

class Day05 : AoCDay(Year.YEAR_2020, Day.DAY_05) {
    private val replaceBinaryHigh: (str: String) -> String = {it.replace(Regex("[BR]"), "1")}
    private val replaceBinaryLow: (str: String) -> String = {it.replace(Regex("[FL]"), "0")}
    private val parseNumberFromBinary: (str: String ) -> Int = { it.toInt(2) }

    private val sortedDecodedSeats = Inputs_2020.DAY_05
            .split("\n")
            .map(replaceBinaryHigh)
            .map(replaceBinaryLow)
            .map(parseNumberFromBinary)
            .sorted()

    override fun part1(): String {
        return sortedDecodedSeats.last().toString()
    }

    override fun part2(): String {
        val filterFirstAndLastElement:(index: Int, number: Int) -> Boolean = {idx: Int, _: Int -> (idx!=0 && idx!=sortedDecodedSeats.size-1) }

        return sortedDecodedSeats
                .filterIndexed(filterFirstAndLastElement)
                .zipWithNext()
                .findLast { p -> p.first+1 != p.second }
                ?.first
                ?.plus(1).toString()
    }
}