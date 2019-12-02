package de.florian.adventofcode.y2019

import kotlin.math.floor
import kotlin.streams.asSequence

fun main() {
    fun String.splittedDoubleSequence() =  this.split("\n").stream().asSequence().map{it.toDouble()}
    val formula: (Double) -> Double = { floor(it / 3.0) - 2 }
    fun recFormula(it: Double) : (Double) = if (formula(it) < 0) 0.0 else formula(it) + recFormula(formula(it))

    val part1 = INPUT_DAY_01.splittedDoubleSequence()
        .map(formula)
        .sum().toInt()
    println("Part 1: $part1")

    val part2 = INPUT_DAY_01.splittedDoubleSequence()
        .map{recFormula(it)}
        .sum().toInt()
    println("Part 2: $part2")

    //Part 1: 3330521
    //Part 2: 4992931
}