package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year


fun main() { Day02().exec() }

class Day02 : AoCDay(Year.YEAR_2020, Day.DAY_02) {
    companion object {
        val SIMPLE_RANGE_PASSWORD_REGEX =  """(\d+)-(\d+) (\w): (.+)""".toRegex()
    }
    abstract class PasswordPolicy {
        abstract fun valid(input: String): Boolean;
    }
    data class SimpleRangePolicy(val range: ClosedRange<Int>, val letter: Char) : PasswordPolicy() {
        override fun valid(input: String): Boolean = range.contains(input.count { it == letter })
    }
    data class IndexedPasswordPolicy(val firstIndex: Int, val secondIndex: Int, val letter: Char) : PasswordPolicy() {
        override fun valid(input: String): Boolean = (input[firstIndex-1] == letter) xor (input[secondIndex-1] == letter)
    }
    data class PolicyPasswordHolder(val policy: PasswordPolicy, val password: String)

    private fun convertInput(input: String, policyCreator: (from: Int, to: Int, letter: Char) -> PasswordPolicy) = input.split("\n").map {
        val (from, to, letter, password) = SIMPLE_RANGE_PASSWORD_REGEX.find(it.trim())!!.destructured
        PolicyPasswordHolder(policyCreator(Integer.parseInt(from), Integer.parseInt(to), letter.first()), password)
    }

    override fun part1(): String {
        return convertInput(Inputs_2020.DAY_02){ from, to, letter ->
            SimpleRangePolicy(from..to, letter)
        }.count {
            it.policy.valid(it.password)
        }.toString()
    }

    override fun part2(): String {
        return convertInput(Inputs_2020.DAY_02){ from, to, letter ->
            IndexedPasswordPolicy(from, to, letter)
        }.count {
            it.policy.valid(it.password)
        }.toString()
    }
}