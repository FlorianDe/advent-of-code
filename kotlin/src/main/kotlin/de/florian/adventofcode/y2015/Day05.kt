package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() {
    Day05().exec()
}

class Day05 : AoCDay(Year.YEAR_2015, Day.DAY_05) {
    private val minTwoChars = """(\w)\1""".toRegex()
    private val exceptionCharCombi = """ab|cd|pq|xy""".toRegex()

    private val singleCharBridge = """(\w).\1""".toRegex()
    private fun convertInput(input: String) = input.split("\n").map { it.trim() }

    val containsDoubleLetterPair: (String) -> (Boolean) = {
        val arr = it.chunked(1)
        var ans = false

        outerloop@
        for (start in 0 until arr.size - 3) {
            val a = arr[start]
            val b = arr[start + 1]
            for (i in start+2 until arr.size - 1) {
                if (a == arr[i] && b == arr[i + 1]) {
                    ans = true
                    break@outerloop
                }
            }
        }

        ans
    }

    override fun part1(): String {
        return convertInput(Inputs_2015.DAY_05).asSequence()
            .filter { word ->
                "aeiou".map { l ->
                    word.count { it == l }
                }.sum() > 2
            }
            .filter { minTwoChars.containsMatchIn(it) }
            .filter { !exceptionCharCombi.containsMatchIn(it) }
            .count()
            .toString()
    }

    override fun part2(): String {
        return convertInput(Inputs_2015.DAY_05).asSequence()
            .filter { singleCharBridge.containsMatchIn(it) }
            .filter { containsDoubleLetterPair(it) }
            .count()
            .toString()
    }
}