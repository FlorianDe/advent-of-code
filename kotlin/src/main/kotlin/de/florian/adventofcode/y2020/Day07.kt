package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import java.util.*

fun main() {
    Day07().exec()
}

class Day07 : AoCDay() {
    companion object {
        //language=RegExp
        val BAG_COUNT_REGEX = """(\d+) ([\w\s ]+)""".toRegex()
        const val SHINY_GOLD_BAG_NAME = "shiny gold"
        val BAG_SPLIT = Regex("bags,|bag,")
        val SANITIZER_REGEX = Regex("bags|bag|\\.")
    }

    data class ShinyBag(val name: String, val number: Int = 1)

    private fun convertInput(input: String) = input.split("\n").map { line ->
        line.split(" bags contain ").zipWithNext().first().let {
            val containsParts = it.second.trim()
            var bagList: List<ShinyBag>? = null
            if (!containsParts.contains("no other bags")) {
                bagList = containsParts.split(BAG_SPLIT).map { bagDescription ->
                    val sanitizedBagPart = bagDescription.replace(SANITIZER_REGEX,"").trim()
                    val (count, name) = BAG_COUNT_REGEX.find(sanitizedBagPart)?.destructured!!
                    ShinyBag(name, count.toInt())
                }
            }
            it.first.trim() to bagList
        }
    }.toMap()

    private val bagMap: Map<String, List<ShinyBag>?> = convertInput(Inputs_2020.DAY_07)

    override fun part1(): String {
        val bagsWhichContainShiny = mutableSetOf<String>()

        for (startingBag in bagMap.keys) {
            val discovered = mutableSetOf<String>()
            val stack = ArrayDeque<String>()
            stack.push(startingBag)
            while(!stack.isEmpty()) {
                val curBag = stack.pop()
                val containing = bagMap[curBag]
                if (!discovered.contains(curBag)) {
                    discovered.add(curBag)
                    if (curBag.contains(SHINY_GOLD_BAG_NAME)) {
                        bagsWhichContainShiny.add(startingBag)
                    }
                    if (containing == null) {
                        continue
                    }
                    for (next in containing) {
                        stack.push(next.name)
                    }
                }
            }
        }
        return bagsWhichContainShiny.filter { it != SHINY_GOLD_BAG_NAME }.size.toString()
    }

    override fun part2(): String {
        var bagsTotal = -1;
        val stack = ArrayDeque<String>()
        stack.push(SHINY_GOLD_BAG_NAME)
        while(!stack.isEmpty()) {
            val curBag = stack.pop()
            bagsTotal++
            val containing = bagMap[curBag] ?: continue
            for (next in containing) {
                for(c in 0 until next.number){
                    stack.push(next.name)
                }
            }
        }
        return bagsTotal.toString()
    }
}