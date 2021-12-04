package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import de.florian.adventofcode.util.CollectionsUtil
import de.florian.adventofcode.util.permute
import java.math.BigInteger
import java.util.LinkedList
import java.util.concurrent.Executors
import java.util.concurrent.Future


fun main() {
    Day07().exec()
}

class Day07 : AoCDay(Year.YEAR_2019, Day.DAY_07) {
    private val memory = CollectionsUtil.getMemory(Inputs_2019.DAY_07)

    override fun part1(): String {
        val permThrusterOutput = mutableMapOf<List<BigInteger>, BigInteger>()
        val perms = listOf(0, 1, 2, 3, 4).map { it.toBigInteger() }.permute()

        for (perm in perms) {
            var programs = mutableListOf<IntCodeComputer>()
            for (value in perm) {
                val comp = IntCodeComputer(memory)
                comp.inputs.put(value)
                programs.add(comp)
            }
            programs[0].inputs.put(BigInteger.ZERO)
            for (i in 0 until programs.size) {
                val compRes = programs[i].run().last()
                if (i < programs.size - 1) {
                    programs[i + 1].inputs.put(compRes)
                } else {
                    permThrusterOutput[perm] = compRes
                }
            }
        }

        return permThrusterOutput.map { it.value }.max().toString()
    }

    override fun part2(): String {
        val permThrusterOutput = mutableMapOf<List<BigInteger>, BigInteger>()
        val perms = listOf(5, 6, 7, 8, 9).map { it.toBigInteger() }.permute()
        for (perm in perms) {
            val programs = Array(perm.size) { IntCodeComputer(memory, name = "Computer-${(65 + it).toChar()}") }
            for (i in programs.indices) {
                programs[Math.floorMod(i - 1, programs.size)].outputs.put(perm[i])
                programs[i].inputs = programs[Math.floorMod(i - 1, programs.size)].outputs
            }
            programs[programs.size - 1].outputs.put(BigInteger.ZERO)
            val executor = Executors.newFixedThreadPool(programs.size)
            val futures = LinkedList<Future<Pair<String, BigInteger>>>()
            for (i in programs.indices) {
                futures.add(executor.submit(programs[i]))
            }
            for (future in futures) {
                val computerResult = future.get()
                if ("Computer-E" == computerResult.first) {
                    permThrusterOutput[perm] = computerResult.second
                }
            }
            executor.shutdown()
        }
        return permThrusterOutput.map { it.value }.max().toString()
    }
}