package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import kotlin.math.ceil

fun main() {
    Day13().exec()
}

class Day13 : AoCDay() {
    data class Bus(val id: Long, val offset: Long)
    data class BusInformation(val earliestTs: Long, val busses: List<Bus>)

    private val itinerary: BusInformation = Inputs_2020.DAY_13.split("\n").let { line ->
        BusInformation(
                line.first().toLong(),
                line.last().split(",").map { it.trim() }.mapIndexedNotNull { idx, value ->  if(value == "x")  null else Bus(value.toLong(), idx.toLong()) }
        )
    }

    override fun part1(): String {
        return itinerary.busses
                .map { bus -> bus to ceil(itinerary.earliestTs/bus.id.toDouble())*bus.id - itinerary.earliestTs }
                .minBy { it.second }
                ?.let { it.first.id * it.second }
                ?.toInt()
                .toString()
    }

    /**
     * Chinese remainder theorem
     * Solved by Sieving
     */
    override fun part2(): String {
        var ts = 0L
        var step = itinerary.busses.first().id
        for (i in 1 until itinerary.busses.size) {
            val bus = itinerary.busses[i]
            while ( (ts+bus.offset) % bus.id != 0L){
                ts += step
            }
            step *= bus.id
        }
        return ts.toString()
    }
}