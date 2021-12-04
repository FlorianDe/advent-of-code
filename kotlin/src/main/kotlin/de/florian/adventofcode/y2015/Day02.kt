package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() { Day02().exec() }

class Day02 : AoCDay(Year.YEAR_2015, Day.DAY_02) {
    private fun convertInput(input: String) = input.split("\n").map {
        val p = it.split("x")
        Triple(Integer.valueOf(p[0]), Integer.valueOf(p[1]), Integer.valueOf(p[2]))
    }

    fun Pair<Int, Int>.mult() = this.first*this.second
    fun Pair<Int, Int>.sum() = this.first+this.second

    val twoSmallest : (Int, Int, Int) -> (Pair<Int, Int>) = { w,h,l ->
        val min: Int = Math.min(w, Math.min(h, l))
        val max: Int = Math.max(w, Math.max(h, l))
        Pair(min, (w+h+l-max-min))
    }

    val calulatePaper: (Triple<Int, Int, Int>) -> (Int)  = {
        val (w, h ,l) = it
        2 * l * w + 2 * w * h + 2 * h * l + twoSmallest(w,h,l).mult()
    }

    val calulateRibbon: (Triple<Int, Int, Int>) -> (Int)  = {
        val (w, h ,l) = it
        l * w * h + 2*twoSmallest(w,h,l).sum()
    }

    override fun part1(): String {
        return convertInput(Inputs_2015.DAY_02).map(calulatePaper).sum().toString()
    }

    override fun part2(): String {
        return convertInput(Inputs_2015.DAY_02).map(calulateRibbon).sum().toString()
    }
}