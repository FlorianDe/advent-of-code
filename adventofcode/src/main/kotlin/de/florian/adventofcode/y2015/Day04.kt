package de.florian.adventofcode.y2015

import de.florian.adventofcode.AoCDay
import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    Day04().exec()
}

class Day04 : AoCDay() {
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    fun findHash(prefix: String): String {
        var ans = "NO MD5 HASH FOUND"
        for (i in 0 until Int.MAX_VALUE) {
            if ("${Inputs_2015.DAY_04}$i".md5().startsWith(prefix)) {
                ans = i.toString()
                break
            }
        }
        return ans
    }

    override fun part1(): String {
        return findHash("00000")
    }

    override fun part2(): String {
        return findHash("000000")
    }
}