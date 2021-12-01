package de.florian.adventofcode

import kotlin.system.measureNanoTime

abstract class AoCDay {
    abstract fun part1(): String
    abstract fun part2(): String

    fun exec(){
        val t1 = measureNanoTime {
            println("Part1: ${this.part1()}")
        }
        println("Took: ${t1/1000000.0} ms.\n")

        val t2 = measureNanoTime {
            println("Part2: ${this.part2()}")
        }
        println("Took: ${t2/1000000.0} ms.")
    }
}