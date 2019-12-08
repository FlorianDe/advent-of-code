package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.util.permute
import java.util.LinkedList
import java.util.concurrent.Executors
import java.util.concurrent.Future


fun main() {
    Day07().exec()
}

class Day07 : AoCDay() {
    private val memory = convertToOpcodes(Inputs_2019.DAY_07)
    private fun convertToOpcodes(input: String): (IntArray) = input.split(",").map { it.toInt() }.toIntArray()


    override fun part1(): String {
        val permThrusterOutput = mutableMapOf<List<Int>, Int>()
        val perms = listOf(0, 1, 2, 3, 4).permute()

        for (perm in perms) {
            var programs = mutableListOf<ComputerProgram>()
            for (value in perm) {
                val comp = ComputerProgram(memory)
                comp.inputs.put(value)
                programs.add(comp)
            }
            programs[0].inputs.put(0)
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
        val permThrusterOutput = mutableMapOf<List<Int>, Int>()
        val perms = listOf(5, 6, 7, 8, 9).permute()
        for (perm in perms) {
            val programs = Array(perm.size) {ComputerProgram(memory, "Computer-${(65+it).toChar()}")}
            for (i in programs.indices) {
                programs[Math.floorMod(i -1 , programs.size)].outputs.put(perm[i])
                programs[i].inputs = programs[Math.floorMod(i -1 , programs.size)].outputs
            }
            programs[programs.size-1].outputs.put(0)
            val executor = Executors.newFixedThreadPool(programs.size)
            val futures = LinkedList<Future<Pair<String, Int>>>()
            for (i in programs.indices) {
                futures.add(executor.submit(programs[i]))
            }
            for (future in futures) {
                val computerResult = future.get()
                if("Computer-E" == computerResult.first){
                    permThrusterOutput[perm] = computerResult.second
                }
            }
            executor.shutdown()
        }
        return permThrusterOutput.map { it.value }.max().toString()
    }
}