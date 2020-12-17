package de.florian.adventofcode.util

import kotlin.math.*

data class Point(var x: Int, var y: Int){
    companion object {
        val angleMap = mapOf(
                90 to Angle.DEG_90,
                180 to Angle.DEG_180,
                270 to Angle.DEG_270
        )
        fun zero() = Point(0,0)
        fun dist(a: Point, b: Point) : Double  = sqrt((b.y - a.y).toDouble().pow(2) + (b.y - a.y).toDouble().pow(2))
        fun angle(from: Point, to: Point) = atan2((to.x - from.x).toDouble(), (to.y - from.y).toDouble())
        fun minValues(a: Point, b: Point) = Point(min(a.x, b.x), min(a.y, b.y))
        fun maxValues(a: Point, b: Point) = Point(max(a.x, b.x), max(a.y, b.y))
    }

    enum class Angle(val rad: Double){
        DEG_90(PI/2),
        DEG_180(PI),
        DEG_270(2*PI*(3/4.0))
    }
    fun setPos(x: Int, y: Int){
        this.x = x
        this.y = y
    }
    fun manhattanDistance(point: Point = zero()): Int {
        return abs(this.x - point.x) + abs(this.y - point.y)
    }
    fun dist(p: Point) : Double = dist(this, p)

    fun angle(p: Point) : Double = angle(this, p)

    fun rotate(angle: Angle, c: Point = Point(0, 0), clockWise: Boolean = true) : Point{
        val angleRad = if(clockWise) angle.rad else -angle.rad
        return Point(
            (cos(angleRad) * (this.x - c.x) + sin(angleRad) * (this.y - c.y) + c.x).toInt(),
            (-sin(angleRad) * (this.x - c.x) + cos(angleRad) * (this.y - c.y) + c.y).toInt()
        )
    }

    operator fun times(scalar: Int): Point = Point(x * scalar, y * scalar)

    operator fun plus(p: Point) = Point(this.x + p.x, this.y + p.y)

    operator fun plusAssign(p: Point) {
        this.x += p.x
        this.y += p.y
    }

    operator fun unaryMinus(): Point {
        return Point(-this.x, -this.y)
    }
}