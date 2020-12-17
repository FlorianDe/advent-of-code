package de.florian.adventofcode.util

import java.math.BigDecimal
import kotlin.math.*

data class BigPoint(var x: BigDecimal, var y: BigDecimal){
    companion object {
        fun zero() = BigPoint(BigDecimal.ZERO, BigDecimal.ZERO)
        fun dist(a: BigPoint, b: BigPoint) : Double  = sqrt((b.y - a.y).toDouble().pow(2) + (b.y - a.y).toDouble().pow(2))
        fun angle(from: BigPoint, to: BigPoint) = atan2((to.x - from.x).toDouble(), (to.y - from.y).toDouble())
        fun minValues(a: BigPoint, b: BigPoint) = BigPoint(a.x.min(b.x), a.y.min(b.y))
        fun maxValues(a: BigPoint, b: BigPoint) = BigPoint(a.x.max(b.x), a.y.max(b.y))
    }

    fun manhattanDistance(point: BigPoint = zero()): BigDecimal {
        return (this.x - point.x).abs() + (this.y - point.y).abs()
    }

    fun rotate(angleDeg: Double, c: BigPoint = zero(), clockWise: Boolean = true) : BigPoint{
        val angleRad = Math.toRadians(if(clockWise) angleDeg else -angleDeg)
        return BigPoint(
            (cos(angleRad).toBigDecimal() * (this.x - c.x) + sin(angleRad).toBigDecimal() * (this.y - c.y) + c.x),
            (-sin(angleRad).toBigDecimal() * (this.x - c.x) + cos(angleRad).toBigDecimal() * (this.y - c.y) + c.y)
        )
    }

    operator fun times(scalar: BigDecimal): BigPoint = BigPoint(x * scalar, y * scalar)

    operator fun plus(p: BigPoint) = BigPoint(this.x + p.x, this.y + p.y)

    private operator fun BigPoint.minus(other: BigPoint): BigPoint = BigPoint(this.x + other.x, this.y + other.y)
}