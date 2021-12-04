package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.times
import kotlin.math.abs

fun main() {
    Day16(Inputs_2019.DAY_16).exec()
}

class Day16(inputStr: String) : AoCDay(Year.YEAR_2019, Day.DAY_16) {
    companion object {
        private val BASE_PATTERN = intArrayOf(0, 1, 0, -1)
    }
    val input = convertInput(inputStr)

    override fun part1(): String {
        return fft()
    }

    override fun part2(): String {
        return fft2()
    }

    fun fft(input: IntArray = this.input, times: Int = 100) : String{
        val pattern = IntArray(input.size)
        val output = input.copyOf()
        repeat(times) {
            for (c in input.indices) {
                val q = (c + 1.0)
                for (i in input.indices) {
                    pattern[i] = BASE_PATTERN[((i + 1.0)/q).toInt() % 4]
                }
                output[c] = (abs(output * pattern) % 10).toInt()
            }
        }
        return output.copyOfRange(0, 8).joinToString("")
    }

    fun fft2(input: IntArray = this.input, times: Int = 100) : String{
        val offset = input.copyOfRange(0, 7).joinToString("").toInt()
        val length = input.size
        val signal =  (offset until 10000 * length).map { input[it % length] }.toIntArray()
        repeat(times){
            for (i in signal.size - 1 downTo 0) {
                val next = if (signal.lastIndex >= i + 1) signal[i + 1] else 0
                signal[i] = (next + signal[i]) % 10
            }
        }
        return signal.take(8).joinToString("")
    }

    fun convertInput(input: String ) = input.asIterable().map { it.toString().toInt() }.toIntArray()
}
