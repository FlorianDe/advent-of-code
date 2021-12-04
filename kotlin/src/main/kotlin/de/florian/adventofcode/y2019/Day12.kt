package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.Point3D
import de.florian.adventofcode.util.lcm
import java.math.BigInteger

fun main() {
    Day12().exec()
}

class Day12 : AoCDay(Year.YEAR_2019, Day.DAY_12) {
    private val inputRegex = """<x=([-]?\d+), y=([-]?\d+), z=([-]?\d+)>""".toRegex()

    fun convertInput(input: String) = input.split("\n").map {
        val groups = inputRegex.find(it)?.groups
        lateinit var planet: Planet

        groups?.let { g ->
            g[1]?.let { x ->
                g[2]?.let { y ->
                    g[3]?.let { z ->
                        planet = Planet(
                            Point3D(
                                Integer.valueOf(x.value),
                                Integer.valueOf(y.value),
                                Integer.valueOf(z.value)
                            )
                        )
                    }
                }
            }
        }
        planet
    }

    private fun simulationStep(planets: List<Planet>): List<Planet> {
        var from = 1
        for (a in planets.indices) {
            for (b in from until planets.size) {
                if (planets[a].pos.x > planets[b].pos.x) {
                    planets[a].velocity.x -= 1
                    planets[b].velocity.x += 1
                } else if (planets[a].pos.x < planets[b].pos.x) {
                    planets[a].velocity.x += 1
                    planets[b].velocity.x -= 1
                }

                if (planets[a].pos.y > planets[b].pos.y) {
                    planets[a].velocity.y -= 1
                    planets[b].velocity.y += 1
                } else if (planets[a].pos.y < planets[b].pos.y) {
                    planets[a].velocity.y += 1
                    planets[b].velocity.y -= 1
                }

                if (planets[a].pos.z > planets[b].pos.z) {
                    planets[a].velocity.z -= 1
                    planets[b].velocity.z += 1
                } else if (planets[a].pos.z < planets[b].pos.z) {
                    planets[a].velocity.z += 1
                    planets[b].velocity.z -= 1
                }
            }
            from++
        }

        for (planet in planets) {
            planet.pos += planet.velocity
        }

        return planets.map { it.copy() }
    }


    override fun part1(): String {
        val planets = convertInput(Inputs_2019.DAY_12)

        for (step in 1..1000) {
            simulationStep(planets)
        }

        return planets.map { it.pos.absSum() * it.velocity.absSum() }.sum().toString()
    }

    override fun part2(): String {
        val planets = convertInput(Inputs_2019.DAY_12)
        val statesX = HashSet<String>(100_000)
        val statesY = HashSet<String>(100_000)
        val statesZ = HashSet<String>(100_000)
        statesX.add(planets.idx())
        statesY.add(planets.idy())
        statesZ.add(planets.idz())

        var step: Long = 1
        var stepsX: Long = 0
        var stepsY: Long = 0
        var stepsZ: Long = 0
        while (true) {
            val state = simulationStep(planets)
            if (stepsX == 0L && statesX.contains(state.idx())) {
                stepsX = step
                println("X: $stepsX")
            }
            if (stepsY == 0L && statesY.contains(state.idy())) {
                stepsY = step
                println("Y: $stepsX")
            }
            if (stepsZ == 0L && statesZ.contains(state.idz())) {
                stepsZ = step
                println("Z: $stepsX")
            }
            if (stepsX > 0 && stepsY > 0 && stepsZ > 0) {
                break
            }
            statesX.add(state.idx())
            statesY.add(state.idy())
            statesZ.add(state.idz())
            step++
        }
        return BigInteger.valueOf(stepsX)
            .lcm(BigInteger.valueOf(stepsY))
            .lcm(BigInteger.valueOf(stepsZ))
            .toString()
    }

    fun List<Planet>.idx() = this.joinToString(separator = "") { "${it.pos.x}${it.velocity.x}" }
    fun List<Planet>.idy() = this.joinToString(separator = "") { "${it.pos.y}${it.velocity.y}" }
    fun List<Planet>.idz() = this.joinToString(separator = "") { "${it.pos.z}${it.velocity.z}" }
    data class Planet(var pos: Point3D, var velocity: Point3D = Point3D(0, 0, 0))
}

