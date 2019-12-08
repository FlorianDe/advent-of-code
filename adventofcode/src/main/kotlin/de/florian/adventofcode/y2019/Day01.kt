package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import kotlin.math.floor
import kotlin.streams.asSequence

fun main() { Day01().exec() }

class Day01 : AoCDay() {
    fun String.splittedDoubleSequence() =  this.split("\n").stream().asSequence().map{it.toDouble()}
    val formula: (Double) -> Double = { floor(it / 3.0) - 2 }
    fun recFormula(it: Double) : (Double) = if (formula(it) < 0) 0.0 else formula(it) + recFormula(formula(it))

    override fun part1(): String {
        return Inputs_2019.DAY_01.splittedDoubleSequence()
            .map(formula)
            .sum().toInt().toString()
    }

    override fun part2(): String {
        return Inputs_2019.DAY_01.splittedDoubleSequence()
            .map{recFormula(it)}
            .sum().toInt().toString()
    }
}