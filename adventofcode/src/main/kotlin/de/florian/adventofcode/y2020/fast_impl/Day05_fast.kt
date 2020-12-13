import de.florian.adventofcode.AoCDay
import kotlin.system.measureTimeMillis

fun main() {
    println("\nAll took ${measureTimeMillis { Day05_Fast().exec()}} ms")
}

class Day05_Fast : AoCDay() {
    companion object{
        private const val MAX_ROWS: Int  = 7
        private const val MAX_COLS: Int  = 3
        private const val BIT_LENGTH: Int = MAX_ROWS + MAX_COLS
        private const val WHITESPACE_LENGTH = 1
        private const val BIT_LINE_SKIP = BIT_LENGTH+WHITESPACE_LENGTH
    }

    data class SeatDataHolder(val min: Int, val max:Int, val seats: Array<Boolean>)

    private val seatInformation = calcSeatInformation()

    private fun calcSeatInformation(): SeatDataHolder{
        val seatSize = 2 shl (BIT_LENGTH-1)
        val maxSeatId: Int = seatSize - 1
        val seatInput = Inputs_2020.DAY_05.toCharArray()
        val seatRowsLength = seatInput.size / BIT_LINE_SKIP

        var min = maxSeatId
        var max = 0
        val seats: Array<Boolean> = Array(seatSize) { false }

        for (y in 0 until seatRowsLength-1){
            var seatId = 0
            for(x in 0 until BIT_LENGTH){
                val idx = (y*BIT_LENGTH+y+x)
                if(seatInput[idx] == 'B' || seatInput[idx] == 'R'){
                    seatId = seatId or (1 shl (BIT_LENGTH-x-1))
                }
                //B -> 66 01000010
                //R -> 82 01010010
                //F -> 70 01000110
                //L -> 76 01001100
                //seatId = seatId or ((((-(seatInput[idx].toInt() and 4)) shr 2)+1)*(1 shl (BIT_LENGTH-x-1)))
            }
            if(seatId>max){
                max = seatId
            }
            if(seatId<min){
                min = seatId
            }
            seats[seatId] = true
        }
        return SeatDataHolder(min, max, seats)
    }

    override fun part1(): String {
        return calcSeatInformation().max.toString()
    }

    override fun part2(): String {
        for(seatId in seatInformation.min until seatInformation.max - 1)
            if(seatInformation.seats[seatId] != seatInformation.seats[seatId+1])
                return (seatId+1).toString()

        return "No empty seat!"
    }
}