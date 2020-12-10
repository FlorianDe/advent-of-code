import de.florian.adventofcode.AoCDay

fun main() {
    Day05().exec()
}

class Day05 : AoCDay() {
    companion object {
        const val ROW_BINARY_DEPTH = 7;
        const val COL_BINARY_DEPTH = 3;
        val seatDecoderMap: Map<Char, String> = mapOf(
                'F' to "0",
                'B' to "1",
                'L' to "0",
                'R' to "1"
        )
    }

    data class BinarySeatHolder(val seatRow: Int, val seatCol: Int) {
        val seatId: Int = seatRow*(ROW_BINARY_DEPTH+1)+seatCol

        companion object {
            fun decodeFromString(input: String) : BinarySeatHolder {
                assert(input.length == ROW_BINARY_DEPTH + COL_BINARY_DEPTH)
                val parseStringToBinary: (range: IntRange) -> Int = { range -> input.subSequence(range).map { seatDecoderMap[it]!! }.joinToString("").toInt(2) }
                val row: Int = parseStringToBinary(0 until ROW_BINARY_DEPTH)
                val col: Int = parseStringToBinary(ROW_BINARY_DEPTH until ROW_BINARY_DEPTH + COL_BINARY_DEPTH)
                return BinarySeatHolder(row, col)
            }
        }
    }

    private fun convertInput(input: String) = input.split("\n").map { BinarySeatHolder.decodeFromString(it.trim()) }

    private val decodedSeats = convertInput(Inputs_2020.DAY_05).sortedBy { it.seatId }

    override fun part1(): String {
        return decodedSeats.last().seatId.toString()
    }

    override fun part2(): String {
        val sortedSeatIds = decodedSeats.map { it.seatId }.toIntArray()
        val first = sortedSeatIds.first()
        for(i in 0 until sortedSeatIds.last()-first)
            if(sortedSeatIds[i] != i+first)
                return "${i+first}"
        throw Error("Couldn't find any seat.")
    }
}