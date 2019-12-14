package de.florian.adventofcode.util

import kotlin.math.*

data class Point3D(var x: Int, var y: Int, var z: Int){
    fun dist(p: Point3D) : Double = dist(this, p)

    fun absSum() : Int = abs(x) +abs(y)+abs(z)

    operator fun plusAssign(p: Point3D) {
        this.x += p.x
        this.y += p.y
        this.z += p.z
    }

    companion object {
        fun dist(a: Point3D, b: Point3D) : Double  = sqrt((b.y - a.y).toDouble().pow(2) + (b.y - a.y).toDouble().pow(2) + (b.z - a.z).toDouble().pow(2))
    }
}