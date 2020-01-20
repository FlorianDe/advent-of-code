package de.florian.adventofcode.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GridTest {

    val pointsMap = mutableMapOf(
        Point(-2,-1) to "A",
        Point(1,0) to "B",
        Point(2,2) to "C",
        Point(1,1) to " ",
        Point(1,2) to " ",
        Point(1,3) to " ",
        Point(2,3) to "D"
    )

    @Test
    fun getCells() {
        val grid = Grid(pointsMap.keys, {pointsMap[it]?:"#" }, {it == "#"})
        println("Grid [${grid.cells.size}x${grid.cells[0].size}] with cord offset: ${grid.offset}")
        grid.cells.forEach { println(it) }

        assertSame(4, grid.cells.size)
        assertDoesNotThrow {grid.cells[0]}
        assertSame(4, grid.cells[0].size)
        assertSame(2, grid.offset.x)
        assertSame(1, grid.offset.y)
    }

    @Test
    fun shortestPath() {
        val grid = Grid(pointsMap.keys, {pointsMap[it]?:"#" }, {it == "#"})
        grid.neighbourhood = Grid.NEUMANN_NEIGHBOURHOOD

        val shortestPath = grid.shortestPath(Point(1,0), "D")

        assertSame(4, shortestPath.size)
    }
}