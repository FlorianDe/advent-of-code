package de.florian.adventofcode.util

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int){
    fun manhattanDistance(point: Point): Int {
        return abs(this.x - point.x) + abs(this.y - point.y)
    }
    fun dist(p: Point) : Double = dist(this, p)
    fun angle(p: Point) : Double = angle(this, p)
    companion object {
        fun dist(a: Point, b: Point) : Double  = sqrt((b.y - a.y).toDouble().pow(2) + (b.y - a.y).toDouble().pow(2))
        fun angle(from: Point, to: Point) = atan2((to.x - from.x).toDouble(), (to.y - from.y).toDouble())
    }
}