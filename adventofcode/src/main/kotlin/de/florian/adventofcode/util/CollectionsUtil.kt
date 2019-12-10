package de.florian.adventofcode.util

import java.math.BigInteger

abstract class CollectionsUtil {
    class Store<K, V : Enum<V>>(values: Array<V>, supplier: (V) -> (Pair<K, V>)) {
        private val map: Map<K, V> = values.map(supplier).toMap()

        fun of(key: K): V {
            map[key]?.let { return it }
            throw IllegalArgumentException("Object with id $key not supported atm.")
        }
    }

    companion object {
        fun getMemory(input: String): Array<BigInteger> = input.split(",").map { it.toBigInteger() }.toTypedArray()
    }
}