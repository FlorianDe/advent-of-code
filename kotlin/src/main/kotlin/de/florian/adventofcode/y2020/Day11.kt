package de.florian.adventofcode.y2020

import de.florian.adventofcode.AoCDay
import java.awt.Point

fun main() {
    Day11().exec()
}

typealias NeighbourCounter = (grid: List<CharArray>, curPos: Point) -> Int
typealias CrowdDetector = (tile: Char, neighbourCount: Int) -> Boolean

class Day11 : AoCDay() {

    companion object {
        const val SEAT_EMPTY = 'L'
        const val SEAT_OCCUPIED = '#'
        const val FLOOR = '.'
    }

    private val seatGrid: List<CharArray> = Inputs_2020.DAY_11.split("\n").map { it.toCharArray() }

    private val adjacents = listOf(
            Point(1,0),
            Point(1,1),
            Point(0,1),
            Point(-1,1),
            Point(-1,0),
            Point(-1,-1),
            Point(0,-1),
            Point(1,-1)
    )

    private fun automatonRun(seatGrid: List<CharArray>, neighbourCounter: (grid: List<CharArray>, curPos: Point) -> Int, crowdDetector: CrowdDetector): Pair<Boolean, List<CharArray>> {
        val grid = seatGrid.map { it.copyOf() }

        var changed = false
        val rows = grid.size
        for (row in 0 until rows) {
            val cols = grid[row].size
            for(col in 0 until cols){
                val tile = seatGrid[row][col]
                if(tile != FLOOR){
                    val neighbourCount = neighbourCounter(seatGrid, Point(col, row))
                    if(tile == SEAT_EMPTY && neighbourCount == 0){
                        grid[row][col]  = SEAT_OCCUPIED
                        changed = true
                    } else if(crowdDetector(tile, neighbourCount)){
                        grid[row][col]  = SEAT_EMPTY
                        changed = true
                    }
                }
            }
        }
        return Pair(changed, grid)
    }

    private fun simulateSeatSituation(seatGrid: List<CharArray>, neighbourCounter: NeighbourCounter, crowdDetector: CrowdDetector): String{
        var run = Pair(false, seatGrid)
        do {
            run = automatonRun(run.second, neighbourCounter, crowdDetector)
        }while(run.first)

        return run.second.map { row -> row.map { if(it == SEAT_OCCUPIED) 1 else 0 }.sum() }.sum().toString()
    }

    override fun part1(): String {
        val neighbourCounter: NeighbourCounter = {grid, pos ->
            adjacents.map {
                val rows = grid.size
                val cols = grid.first().size

                val nRow = pos.y+it.y
                val nCol = pos.x+it.x
                if((nRow in 0 until rows && nCol in 0 until cols) && grid[nRow][nCol] == SEAT_OCCUPIED){
                    1 // neighbour occupied
                } else {
                    0 // empty or floor
                }
            }.sum()
        }

        val crowdDetector: CrowdDetector = { tile, neighbourCount ->
            tile == SEAT_OCCUPIED && neighbourCount >= 4
        }

        return simulateSeatSituation(seatGrid, neighbourCounter, crowdDetector)
    }
    
    override fun part2(): String {
        val neighbourCounter: NeighbourCounter = {grid, pos ->
            adjacents.map {
                val rows = grid.size
                val cols = grid.first().size

                var scalar = 1
                var nRow = pos.y+scalar*it.y
                var nCol = pos.x+scalar*it.x
                while (nRow in 0 until rows && nCol in 0 until cols){
                    if(grid[nRow][nCol] == SEAT_OCCUPIED){
                        return@map 1 // neighbour occupied
                    }else if(grid[nRow][nCol] == SEAT_EMPTY){
                        return@map 0 // found empty seat
                    }
                    scalar++
                    nRow = pos.y+scalar*it.y
                    nCol = pos.x+scalar*it.x
                }
                return@map 0
            }.sum()
        }

        val crowdDetector: CrowdDetector = { tile, neighbourCount ->
            tile == SEAT_OCCUPIED && neighbourCount >= 5
        }

        return simulateSeatSituation(seatGrid, neighbourCounter, crowdDetector)
    }
}