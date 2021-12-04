package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay

fun main() { Day01().exec() }

class Day01 : AoCDay() {
    private fun convertInput(input: String) = input.split("\n").map { Integer.parseInt(it.trim()) }

    private val numbers = convertInput(Inputs_2020.DAY_01)

    override fun part1(): String {
        numbers.forEach { a ->
            numbers.forEach { b ->
                if (a + b == 2020) return "${a * b}";
            }
        }
        throw Error("Input broken.")
    }

    override fun part2(): String {
        numbers.forEach { a ->
            numbers.forEach { b ->
                numbers.forEach { c ->
                    if (a + b + c == 2020) return "${a * b * c}";
                }
            }
        }
        throw Error("Input broken.")
    }
}