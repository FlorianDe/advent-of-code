import de.florian.adventofcode.AoCDay
import kotlin.system.measureTimeMillis

fun main() {
    println("Took ${measureTimeMillis { Day05_Fast().exec()}} ms")
}

class Day05_Fast : AoCDay() {
    companion object{
        const val MAX_ROWS: Int  = 7
        const val MAX_COLS: Int  = 3
        const val BIT_LENGTH: Int = MAX_ROWS + MAX_COLS
    }

    data class SeatDataHolder(val min: Int, val max:Int, val seats: Array<Boolean>)

    private fun calcSeatInformation(): SeatDataHolder{
        val seatSize = 2 shl BIT_LENGTH
        val maxSeatId: Int = seatSize - 1
        var min = maxSeatId
        var max = 0
        val seats: Array<Boolean> = Array(seatSize) { false }

        for (line in Inputs_2020.DAY_05.split("\n")){
            var seatId = 0
            for (idx in line.indices){
                seatId += (if(line[idx] == 'B' || line[idx] == 'R') 1 else 0) shl (BIT_LENGTH-idx-1)
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

    private val seatInformation = calcSeatInformation()

    override fun part1(): String {
        return seatInformation.max.toString()
    }

    override fun part2(): String {
        for(seatId in seatInformation.min until seatInformation.max - 1) {
            if(seatInformation.seats[seatId] != seatInformation.seats[seatId+1]){
                return (seatId+1).toString()
            }
        }
        throw Error("No empty seat!")
    }
}