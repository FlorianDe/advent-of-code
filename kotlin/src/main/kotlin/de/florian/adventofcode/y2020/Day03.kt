package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.Point
import java.math.BigInteger

fun main() {
    Day03().exec()
}

class Day03 : AoCDay(Year.YEAR_2020, Day.DAY_03) {
    companion object {
        const val TREE = "#"
        const val PAVEMENT = "."
        val slopes = listOf(
                Point(1, 1),
                Point(3, 1),
                Point(5, 1),
                Point(7, 1),
                Point(1, 2)
        )

    }

    private fun convertInput(input: String): List<List<String>> = input.split("\n").map { line -> line.trim().chunked(1) }

    val biome = convertInput(Inputs_2020.DAY_03);

    private fun traverseBiome(biome: List<List<String>>, slope: Point): Int {
        val biomeHeight = biome.size
        val biomeWidth = biome.first().size
        var row = 0
        var col = 0
        var trees = 0
        while (row < biomeHeight - 1) {
            row += slope.y
            col = (col + slope.x) % biomeWidth
            if (TREE == biome[row][col]) {
                trees++
            }
        }
        return trees
    }

    override fun part1(): String {
        return traverseBiome(biome, Point(3, 1)).toString()
    }

    override fun part2(): String {
        return slopes.map { traverseBiome(biome, it) }
                .fold(BigInteger.ONE) { acc, e -> acc * e.toBigInteger() }
                .toString()
    }
}