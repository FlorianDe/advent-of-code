package de.florian.adventofcode.y2021

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year

fun main() {
    Day03().exec()
}

class Bit(private val bit: Boolean){
    fun getChar(): Char = if (bit) '1' else '0'
    companion object {
        val ZERO = Bit(false)
        val ONE = Bit(true)
    }
}
data class HighLow<out T>(val high: T, val low: T)

class Day03 : AoCDay(Year.YEAR_2021, Day.DAY_03) {
    private val signals = getInput().split("\n").map(String::trim)
    private val signalLength = signals[0].length

    private fun List<String>.getHighLowBit(col: Int): HighLow<Bit> {
        val ones = count { it[col] == Bit.ONE.getChar() }
        val isOnesHigh = ones >= size - ones
        return HighLow(Bit(isOnesHigh), Bit(!isOnesHigh))
    }

    private fun List<String>.reduceByBit(bitChooser: (HighLow<Bit>) -> Bit): Int {
        var signals = this
        for(col in 0 until signalLength){
            val highLowBit = signals.getHighLowBit(col)
            signals = signals.filter { it[col] == bitChooser(highLowBit).getChar() }
            if(signals.size == 1) break
        }
        return signals.first().toInt(2)
    }

    override fun part1(): String {
        return (0 until signalLength)
            .fold(initial = HighLow("", "")) { rates, col ->
                signals.getHighLowBit(col).run {
                    HighLow(rates.high + this.high.getChar(), rates.low + this.low.getChar())
                }
            }.run { "${this.high.toInt(2) * this.low.toInt(2)}" }
    }

    override fun part2(): String {
        return "${signals.reduceByBit { it.high } * signals.reduceByBit { it.low }}"
    }
}