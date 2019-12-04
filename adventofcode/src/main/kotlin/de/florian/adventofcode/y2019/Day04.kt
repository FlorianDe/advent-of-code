package de.florian.adventofcode.y2019

fun main() {
    val day04 = Day04()
    println("Part1: ${day04.part1()}")
    println("Part2: ${day04.part2()}")
}

class Day04 {
    private val minTwoDigitsRegex = """(\d)\1""".toRegex()

    private fun getRange(input: String): IntRange {
        val (fromStr, toStr) = input.split("-").zipWithNext().first()
        return Integer.parseInt(fromStr)..Integer.parseInt(toStr)
    }

    private fun isIncreasingNumber(num: Int): Boolean {
        var t = num
        while (t / 10 > 0) {
            if ((t % 10) < ((t / 10) % 10)) {
                return false
            }
            t /= 10
        }
        return true
    }

    private fun exactlyAtLeastOnePairOfTwoFollowingDigits(num: Int): Boolean {
        val intArray = num.toString().chunked(1).map { Integer.parseInt(it) }.toIntArray()
        if ((intArray[0] == intArray[1] && intArray[1] != intArray[2]) || (intArray[intArray.size - 3] != intArray[intArray.size - 2] && intArray[intArray.size - 2] == intArray[intArray.size - 1])) {
            return true
        }
        for (x in 1..intArray.size - 3) {
            if (intArray[x] == intArray[x + 1] && intArray[x - 1] != intArray[x] && intArray[x + 1] != intArray[x + 2]) {
                return true
            }
        }
        return false
    }

    fun part1(): String {
        return countStuff { minTwoDigitsRegex.containsMatchIn(it.toString()) }
    }

    fun part2(): String {
        return countStuff { exactlyAtLeastOnePairOfTwoFollowingDigits(it) }
    }

    private fun countStuff(pred: (Int) -> Boolean): String {
        return getRange(INPUT_DAY_04)
            .asSequence()
            .filter { it in 100_000..999999 }
            .filter { isIncreasingNumber(it) }
            .filter { pred(it) }
            .count()
            .toString()
    }
}