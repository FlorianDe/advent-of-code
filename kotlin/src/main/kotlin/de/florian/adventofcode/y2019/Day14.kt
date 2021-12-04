package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import java.util.*
import kotlin.math.ceil

fun main() {
    Day14().exec()
}

class Day14 : AoCDay(Year.YEAR_2019, Day.DAY_14) {
    val amountStrRegex = """(\d+) (\w+)""".toRegex()
    val toElementQuantity: (String) -> (ElementQuantity) = {
        val (amount, element) = amountStrRegex.find(it.trim())!!.destructured
        ElementQuantity(Element(element), amount.toLong())
    }

    val reactions = Inputs_2019.DAY_14
        .split("\n")
        .map {
            val line = it.trim().split("=>")
            Pair(line[0], line[1])
        }
        .map {
            Reaction(it.first.split(",").map(toElementQuantity), toElementQuantity(it.second))
        }

    override fun part1(): String {
        return Cauldron(reactions, ElementQuantity(Cauldron.FUEL, 1)).cook().toString()
    }

    override fun part2(): String {
        val ORE = 1_000_000_000_000L
        var min = 2L
        var max = ORE
        var mid = (max+min)/2

        val cauldron = Cauldron(reactions, ElementQuantity(Cauldron.FUEL, mid))
        while (max - min > 1){
            mid = (max+min)/2
            cauldron.weightedProduct.quantity = mid
            val lastVal =  cauldron.cook()
            if(lastVal > ORE) {
                max = mid
            } else if(lastVal == ORE) {
                break
            } else {
                min = mid
            }
        }
        return min.toString()
    }

    class Cauldron(reactions: List<Reaction>, var weightedProduct: ElementQuantity){
        companion object {
            val ORE = Element("ORE")
            val FUEL = Element("FUEL")
        }

        var inventory = mutableMapOf<Element, Long>()
        var reactionMap : Map<Element, Reaction> = reactions.map { it.product.element to it }.toMap()

        fun cook() : Long{
            var oreReq = 0L
            val queue: Queue<ElementQuantity> = LinkedList()
            queue.add(weightedProduct)
            while (queue.isNotEmpty()) {
                var (element, produceAmount) = queue.remove()

                if (element == ORE) {
                    oreReq += produceAmount
                    continue
                }

                val invValue = inventory.getOrDefault(element, 0)
                if (invValue >= produceAmount) {
                    inventory.computeIfPresent(element) { _, value ->  value - produceAmount}
                } else {
                    produceAmount -= invValue
                    inventory[element] = 0

                    val reaction = reactionMap[element]!!
                    val batches = ceil(produceAmount / reaction.product.quantity.toDouble()).toLong()
                    val invAdd = batches * reaction.product.quantity - produceAmount
                    if (invAdd > 0) {
                        inventory.merge(element, invAdd) { prev, value -> prev + value}
                    }
                    for (educt in reaction.educts) {
                        queue.add(ElementQuantity(educt.element, batches * educt.quantity))
                    }
                }
            }
            return oreReq
        }
    }

    data class ElementQuantity(val element: Element, var quantity: Long)
    data class Element(val name: String)
    data class Reaction(val educts: List<ElementQuantity>, val product: ElementQuantity)
}