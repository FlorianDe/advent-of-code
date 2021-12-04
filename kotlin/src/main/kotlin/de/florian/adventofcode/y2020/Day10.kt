package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay

fun main() {
    Day10().exec()
}

class Day10 : AoCDay() {
    private val adapters: List<Int> = Inputs_2020.DAY_10.split("\n").map { it.trim().toInt() }.sorted()

    override fun part1(): String {
        return adapters
                .zipWithNext { a, b -> b - a }
                .groupBy { it }
                .let {
                    (it[1]!!.size + 1) * (it[3]!!.size + 1)
                }.toString()
    }
    
    override fun part2(): String {
        return adapters.foldRight(mutableMapOf(0 to 1L)) { adapter, combs ->
            combs[adapter] = (1..3).map { combs.getOrDefault(adapter - it, 0) }.sum()
            combs
        }.toList().last().toString()
    }
}