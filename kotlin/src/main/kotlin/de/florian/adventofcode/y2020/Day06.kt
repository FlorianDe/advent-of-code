package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay

fun main() { Day06().exec() }

class Day06 : AoCDay() {
    private fun convertInput(input: String): List<List<MutableSet<Char>>> = input.split(Regex("\\n\\n")).map { group ->
        group.trim().split("\n").map { it.toSortedSet()}
    }

    private val answers = convertInput(Inputs_2020.DAY_06)

    override fun part1(): String {
        return answers.map {
            it.fold<Set<Char>, Set<Char>>(mutableSetOf(), { acc, e -> (acc + e) }).size
        }.sum().toString()
    }

    override fun part2(): String {
        return answers.map { group ->
            val map = mutableMapOf<Char, Int>()
            group.forEach { personAnswers ->
                personAnswers.forEach { answer ->
                    map.merge(answer, 1){a,b -> a+b}
                }
            }
            map.filter { it.value == group.count() }.size
        }.sum().toString()
    }
}