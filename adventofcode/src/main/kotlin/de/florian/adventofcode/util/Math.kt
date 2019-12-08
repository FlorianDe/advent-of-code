package de.florian.adventofcode.util

abstract class Math {
    companion object {
        fun <T> permute(input: List<T>): List<List<T>> {
            if (input.size == 1) return listOf(input)
            val perms = mutableListOf<List<T>>()
            val toInsert = input[0]
            for (perm in permute(input.drop(1))) {
                for (i in 0..perm.size) {
                    val newPerm = perm.toMutableList()
                    newPerm.add(i, toInsert)
                    perms.add(newPerm)
                }
            }
            return perms
        }
    }
}
fun <T> List<T>.permute() = Math.permute(this)
