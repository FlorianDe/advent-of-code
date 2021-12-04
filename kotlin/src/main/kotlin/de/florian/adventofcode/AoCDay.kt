package de.florian.adventofcode

import java.io.File
import kotlin.system.measureNanoTime

enum class Year(val input: String) {
    YEAR_2015("y2015"),
    YEAR_2016("y2016"),
    YEAR_2017("y2017"),
    YEAR_2018("y2018"),
    YEAR_2019("y2019"),
    YEAR_2020("y2020"),
    YEAR_2021("y2021"),
}

enum class Day(val input: String) {
    DAY_01("Day01"),
    DAY_02("Day02"),
    DAY_03("Day03"),
    DAY_04("Day04"),
    DAY_05("Day05"),
    DAY_06("Day06"),
    DAY_07("Day07"),
    DAY_08("Day08"),
    DAY_09("Day09"),
    DAY_10("Day10"),
    DAY_11("Day11"),
    DAY_12("Day11"),
    DAY_13("Day13"),
    DAY_14("Day14"),
    DAY_15("Day15"),
    DAY_16("Day16"),
    DAY_17("Day17"),
    DAY_18("Day18"),
    DAY_19("Day19"),
    DAY_20("Day20"),
    DAY_21("Day21"),
    DAY_23("Day23"),
    DAY_24("Day24"),
    DAY_25("Day25"),
}

abstract class AoCDay(private val year: Year, private val day: Day) {
    abstract fun part1(): String
    abstract fun part2(): String
    private val _input: String by lazy {
        File("src/main/kotlin/de/florian/adventofcode/${this.year.input}/inputs/${this.day.input}.txt").readText()
    }
    fun getInput() =  "" + _input

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