package de.florian.adventofcode.y2019

abstract class DayAoc2019 {
    abstract fun part1(): String
    abstract fun part2(): String

    fun exec(){
        println("Part1: ${this.part1()}")
        println("Part2: ${this.part2()}")
    }
}