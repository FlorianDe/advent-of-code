package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.Point
import de.florian.adventofcode.util.Point3D
import de.florian.adventofcode.util.permute
import java.time.LocalDateTime
import javax.swing.plaf.metal.MetalLookAndFeel

fun main() {
    Day13().exec()
}

class Day13 : AoCDay() {
    val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_13)

    override fun part1(): String {
        return IntCodeComputer(memory).run().chunked(3).count{ it[2].toInt() == 2 }.toString()
    }

    override fun part2(): String {
        return ""
    }
}

