package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay


fun main() {
    Day08().exec()
}

class Day08 : AoCDay() {
    val COLOR_BLACK = 0
    val COLOR_WHITE = 1
    val COLOR_TRANSPARENT = 2

    val IMG_WIDTH = 25
    val IMG_HEIGTH = 6
    val IMG_DIM: Int = IMG_HEIGTH * IMG_WIDTH

    val imageInput = convertInput(Inputs_2019.DAY_08)
    private fun convertInput(input: String): (IntArray) = input.chunked(1).map { it.toInt() }.toIntArray()

    override fun part1(): String {
        var minZeroes = Int.MAX_VALUE
        var map = emptyMap<Int, Int>()
        for (offset in 0..imageInput.size - IMG_DIM step IMG_DIM) {
            val countMap = mutableMapOf(0 to 0)
            for (i in 0 until IMG_DIM) {
                var value = imageInput[offset + i]
                countMap.putIfAbsent(value, 0)
                countMap.compute(value) { _, u -> u?.plus(1) }
            }
            if (countMap[0]!! < minZeroes) {
                minZeroes = countMap[0]!!
                map = countMap
            }
        }
        return (map[1]!! * map[2]!!).toString()
    }

    override fun part2(): String {
        val image = IntArray(IMG_DIM) { COLOR_TRANSPARENT }
        for (offset in 0..imageInput.size - IMG_DIM step IMG_DIM)
            for (i in 0 until IMG_DIM)
                if (image[i] == COLOR_TRANSPARENT)
                    image[i] = imageInput[offset + i]

        return "\n" + image.map { if (it == COLOR_WHITE) '#' else ' ' }.chunked(25).joinToString("\n") { it.joinToString("") }
    }
}
