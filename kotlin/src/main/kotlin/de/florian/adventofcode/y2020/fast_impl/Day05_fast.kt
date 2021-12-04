package de.florian.adventofcode.y2020.fast_impl
import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.y2020.Inputs_2020
import kotlin.system.measureTimeMillis

fun main() {
    println("\nAll took ${measureTimeMillis { Day05_Fast().exec()}} ms")
}

class Day05_Fast : AoCDay(Year.YEAR_2020, Day.DAY_05) {
    companion object{
        private const val MAX_ROWS: Int  = 7
        private const val MAX_COLS: Int  = 3
        private const val WHITESPACE_LENGTH = 1
        private const val BIT_LENGTH: Int = MAX_ROWS + MAX_COLS
        private const val BIT_LINE_SKIP = BIT_LENGTH+WHITESPACE_LENGTH
    }

    data class SeatDataHolder(val min: Long, val max:Long, val missingSeat: Long)

    private val seatInformation = calcSeatInformation()

    private fun calcSeatInformation(): SeatDataHolder{
        val maxSeatId: Long = ((2L shl (BIT_LENGTH-1)) - 1).also {
            if(it > 4294967295L) throw Error("This algorithm is only stable for airplanes with a maximum seat id of 1/2*(sqrt(8*(Long.MAX_VALUE)+1)-1) which is 4294967295.")
        }
        val seatInput = Inputs_2020.DAY_05.toCharArray()
        val seatRowsLength = seatInput.size / BIT_LINE_SKIP
        var seatSum: Long = 0
        var min: Long  = maxSeatId
        var max: Long  = 0

        for (y in 0..seatRowsLength){
            var seatId = 0L
            for(x in 0 until BIT_LENGTH) {
                seatId = (seatId shl 1) + (seatInput[(y*BIT_LENGTH+y+x)].toInt() % 7) % 2
            }
            if(seatId>max) max = seatId
            if(seatId<min) min = seatId
            seatSum += seatId
        }
        return SeatDataHolder(min, max, (((min+max)*(max-min+1)) shr 1)-seatSum)
    }

    override fun part1(): String {
        return seatInformation.max.toString()
    }

    override fun part2(): String {
        return seatInformation.missingSeat.toString();
    }
}