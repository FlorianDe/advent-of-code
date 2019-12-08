package de.florian.adventofcode

abstract class AoCDay {
    abstract fun part1(): String
    abstract fun part2(): String

    fun exec(){
        println("Part1: ${this.part1()}")
        println("Part2: ${this.part2()}")
    }
}