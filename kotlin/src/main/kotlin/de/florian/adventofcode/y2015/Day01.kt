package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay

fun main() { Day01().exec() }

class Day01 : AoCDay() {
    private fun convertInput(input: String) = input.split("")
    val determineDir : (String) -> (Int) = {
        when(it){
            "(" -> 1
            ")" -> -1
            else -> 0
        }
    }

    override fun part1(): String {
        return convertInput(Inputs_2015.DAY_01).map(determineDir).sum().toString()
    }

    override fun part2(): String {
        val list = convertInput(Inputs_2015.DAY_01)
        var sum = 0
        for (i in list.indices){
            sum += determineDir(list[i])
            if(sum == -1){
                return ""+i
            }
        }
        return "SANTA NEVER CAME TO BASE -1"
    }
}