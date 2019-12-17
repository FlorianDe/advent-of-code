package de.florian.adventofcode.util

import java.math.BigInteger

fun BigInteger.lcm(b: BigInteger) = (this / this.gcd(b) * b)
fun <T> List<T>.permute() = MathUtil.permute(this)
abstract class MathUtil {
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

