package de.florian.adventofcode.util

import java.math.BigInteger

fun <T,V> Array<Array<T>>.indices2DFiltered(predicate: (T) -> Boolean, action: (y: Int, x: Int) -> V): MutableList<V> {
    val elements = mutableListOf<V>()
    for(y in this.indices){
        for(x in this[y].indices){
            if(predicate(this[y][x])) {
                elements.add(action(y, x))
            }
        }
    }
    return elements
}
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