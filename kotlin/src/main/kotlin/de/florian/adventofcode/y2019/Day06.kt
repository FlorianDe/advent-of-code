package de.florian.adventofcode.y2019

import de.florian.adventofcode.AoCDay
import de.florian.adventofcode.Day
import de.florian.adventofcode.Year
import java.util.*
import kotlin.collections.HashMap


fun main() {
    Day06().exec()
}

class Day06 : AoCDay(Year.YEAR_2019, Day.DAY_06) {
    fun convertInput(input: String) = input.split("\n").map {
        assert(it.contains(")"))
        assert(!it.contains("("))
        val split = it.trim().split(")")
        Edge(split[1], split[0])
    }

    override fun part1(): String {
        val edges = convertInput(Inputs_2019.DAY_06)
        val graph = Graph(edges, true)

        val nodes = edges.map { Node(it.from) }.filter { it.nodeId != "COM" }.toSet()
        return nodes.map { graph.breadthFirstSearchWithTarget(it).visitedNodes }.sum().toString()
    }

    override fun part2(): String {
        val edges = convertInput(Inputs_2019.DAY_06)
        val graph = Graph(edges, false)

        val startNode = graph.adjacencyMap.keys.filter { it.nodeId == "YOU" }.toSet()
        val endNode = graph.adjacencyMap.keys.filter { it.nodeId == "SAN" }.toSet()
        return (graph.getShortestDistance(startNode.first(), endNode.first()) - 2).toString()
    }
}

data class Edge<T>(val from: T, val to: T)
data class Node<T>(
    val nodeId: T
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node<*>

        if (nodeId != other.nodeId) return false

        return true
    }

    override fun hashCode(): Int {
        return nodeId?.hashCode() ?: 0
    }
}

class Graph<T>(nodePairs: List<Edge<T>>, val directed: Boolean = false) {
    val adjacencyMap: HashMap<Node<T>, LinkedList<Node<T>>> = hashMapOf()

    init {
        nodePairs.forEach {
            addEdge(Node(it.from), Node(it.to))
        }
    }

    fun addEdgeHelper(
        source: Node<T>,
        destination: Node<T>
    ) {
        adjacencyMap[source]?.add(destination)
    }

    fun addEdge(
        source: Node<T>,
        destination: Node<T>
    ) {
        if (!adjacencyMap.containsKey(source)) adjacencyMap[source] = LinkedList()
        if (!adjacencyMap.containsKey(destination)) adjacencyMap[destination] = LinkedList()
        addEdgeHelper(source, destination)
        if (!directed) {
            addEdgeHelper(destination, source)
        }
    }

    data class BFSRes<T>(
        val stopped: Boolean,
        val visitedNodes: Int,
        val pred: HashMap<Node<T>, Node<T>> = HashMap(),
        val dist: MutableMap<Node<T>, Int> = mutableMapOf()
    )

    fun breadthFirstSearchWithTarget(startNode: Node<T>, stopNode: Node<T>? = null): BFSRes<T> {
        val visited: HashMap<Node<T>, Boolean> = HashMap()
        val dist: MutableMap<Node<T>, Int> = adjacencyMap.keys.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
        val pred: HashMap<Node<T>, Node<T>> = HashMap()

        val queue = LinkedList<Node<T>>()
        dist[startNode] = 0
        queue.add(startNode)
        var sum = 0

        while (!queue.isEmpty()) {
            val currentFirst = queue.removeFirst()

            if (visited.containsKey(currentFirst)) {
                continue
            }

            visited[currentFirst] = true
            val allNeighbors: LinkedList<Node<T>>? = adjacencyMap[currentFirst]
            if (allNeighbors == null || allNeighbors.isEmpty()) {
                continue
            }
            for (neighbor in allNeighbors) {
                if (!visited.containsKey(neighbor)) {
                    dist[neighbor] = 1 + dist[currentFirst]!!
                    pred[neighbor] = currentFirst
                    if (neighbor.nodeId == stopNode?.nodeId) {
                        return BFSRes(true, sum, pred, dist)
                    }
                    sum++
                    queue.add(neighbor)
                }
            }
        }
        return BFSRes(false, sum)
    }

    fun getShortestDistance(s: Node<T>, dest: Node<T>): Int {
        val bfsRes = breadthFirstSearchWithTarget(s, dest)

        if (!bfsRes.stopped) {
            println("Src and dest are not connected!")
            return -1
        }

        val path = LinkedList<Node<T>>()
        var curr: Node<T> = dest
        path.add(curr)
        while (bfsRes.pred[curr] != null) {
            path.add(bfsRes.pred[curr]!!)
            curr = bfsRes.pred[curr]!!
        }
        println("Shortest path length is : ${bfsRes.dist[dest]}")

        println("The shortest path is:")
        for (i in path.size - 1 downTo 0)
            print("${path[i].nodeId} ")
        println("\n")
        return bfsRes.dist[dest]!!
    }
}

