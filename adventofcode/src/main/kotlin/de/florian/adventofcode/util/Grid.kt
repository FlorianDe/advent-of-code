package de.florian.adventofcode.util

import java.util.*

class Grid<T>(points: Collection<Point>, cellRetriever: (Point) -> (T), var isBarrier: ((T) -> Boolean)? = null, var neighbourhood: Array<Point> = NEUMANN_NEIGHBOURHOOD, var manhattanDist: Boolean = true) {

    var offset = Point(0, 0)
    var cells: MutableList<MutableList<T>> = mutableListOf()

    init {
        val bounds = boundingBox(points)
        this.offset = -bounds.first
        this.cells = MutableList(bounds.second.y - bounds.first.y) { y ->
            MutableList(bounds.second.x - bounds.first.x) { x ->
                cellRetriever(Point(x-offset.x, y-offset.y))
            }
        }
    }

    companion object {
        val NEUMANN_NEIGHBOURHOOD = arrayOf(
            Point(0, 1),
            Point(0, -1),
            Point(1, 0),
            Point(-1, 0)
        )

        val MOORE_NEIGHBOURHOOD = NEUMANN_NEIGHBOURHOOD + arrayOf(
            Point(1, 1),
            Point(1, -1),
            Point(-1, -1),
            Point(-1, 1)
        )
    }

    fun shortestPath(from: Point, to: T): List<Point> {
        fun d(curPos: Point, neighbour: Point): Double {
            return if (manhattanDist)
                curPos.manhattanDistance(neighbour).toDouble()
            else
                curPos.dist(neighbour)
        }

        val h: (Point) -> Double = { p1 ->
            0.0 // Only possible if some spatial information about the goal is available
        }

        val openSet = mutableSetOf(from)
        val cameFrom = mutableMapOf<Point, Point>()
        val gScore = mutableMapOf<Point, Double>()
        val fScore = mutableMapOf<Point, Double>()
        for (y in cells.indices) {
            for (x in cells[y].indices) {
                gScore[Point(x, y)] = Double.MAX_VALUE / 2 - 1
                fScore[Point(x, y)] = Double.MAX_VALUE / 2 - 1
            }
        }
        gScore[from] = 0.0
        fScore[from] = h(from)

        while (openSet.isNotEmpty()) {
            val current = openSet.minBy { fScore.getValue(it) }!! //fScore.entries.minBy { it.value }!!.key
            if (cells[current.y][current.x] == to) {
                return reconstructPath(cameFrom, current)
            }
            openSet.remove(current)

            for (neighbour in getNeighbours(current)) {
                val tentiativeGScore = d(current, neighbour) + gScore[current]!!
                if (tentiativeGScore < gScore[neighbour]!!) {
                    cameFrom[neighbour] = current
                    gScore[neighbour] = tentiativeGScore
                    fScore[neighbour] = gScore[neighbour]!! + h(neighbour)
                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour)
                    }
                }
            }
        }
        return emptyList()
    }

    fun getNeighbours(p: Point): List<Point> {
        val gridSize = Point(if (cells.isNotEmpty()) cells[0].size else 0, cells.size)
        return neighbourhood
            .map { Point(p.x + it.x, p.y + it.y) }
            .filter { it.y in 0 until gridSize.y && it.x in 0 until gridSize.x }
            .filter { if(isBarrier!=null) !isBarrier!!(cells[it.y][it.x]) else true }
    }

    fun reconstructPath(cameFrom: Map<Point, Point>, goal: Point): List<Point> {
        val path = LinkedList<Point>()
        var current: Point? = goal
        while (cameFrom.containsKey(current)) {
            path.addFirst(current)
            current = cameFrom[current]
        }
        return path
    }

    fun boundingBox(points: Collection<Point>): Pair<Point, Point> {
        var minWidth = 0
        var maxWidth = 0

        var minHeight = 0
        var maxHeight = 0

        for (p in points) {
            if (p.x > maxWidth) maxWidth = p.x
            if (p.y < minHeight) minHeight = p.y
            if (p.x < minWidth) minWidth = p.x
            if (p.y > maxHeight) maxHeight = p.y
        }

        return Pair(Point(minWidth, minHeight), Point(maxWidth, maxHeight))
    }
}