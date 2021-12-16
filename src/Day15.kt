import java.util.*

fun main() {
    data class Node(
        val x: Int,
        val y: Int,
        val value: Int
    ) {
        var gScore: Int = 0
        var parent: Node? = null
        var top: Node? = null
        var bottom: Node? = null
        var left: Node? = null
        var right: Node? = null
        var visited = false
        val neighbors get() = mutableListOf(top, bottom, left, right).filterNotNull()

        fun print() {
            parent?.print()
            println("$x,$y $value")
        }
    }

    fun Array<Node>.safeGet(x: Int): Node? =
        if (x < 0 || x >= size) null else this[x]

    fun Array<Array<Node>>.safeGet(x: Int, y: Int): Node? =
        if (y < 0 || y >= size) null else this[y].safeGet(x)

    fun assignNeighbors(rows: Array<Array<Node>>) {
        rows.forEach { row ->
            row.forEach { node ->
                node.apply {
                    left = row.safeGet(x - 1)
                    right = row.safeGet(x + 1)
                    top = rows.safeGet(x, y - 1)
                    bottom = rows.safeGet(x, y + 1)
                }
            }
        }
    }

    val emptyNode = Node(0, 0, 0)

    fun Array<Array<Node>>.expand(times: Int): Array<Array<Node>> {
        val newSize = size * times
        val result = Array<Array<Node>>(newSize) { emptyArray() }
        for (i in 0 until newSize) {
            result[i] = Array(newSize) { emptyNode }
        }
        for (x in 0 until newSize) {
            for (y in 0 until newSize) {
                val node = this[y % size][x % size]
                val dx = x / size
                val dy = y / size
                val newValue = (node.value + dx + dy).takeIf { it < 10 } ?: ((node.value + dx + dy) - 9)
                result[y][x] = Node(x, y, newValue)
            }
        }
        return result
    }

    fun lowestTotalRisk(nodes: Array<Array<Node>>): Int {
        assignNeighbors(nodes)
        val openSet = PriorityQueue<Node>(Comparator.comparing { it.gScore })
        val startNode = nodes[0][0]
        openSet.add(startNode)
        startNode.visited = true
        while (openSet.isNotEmpty()) {
            val node = openSet.poll()
            node.neighbors.forEach { neighbor ->
                if (!neighbor.visited) {
                    neighbor.gScore = node.gScore + neighbor.value
                    neighbor.parent = node
                    openSet.add(neighbor)
                    neighbor.visited = true
                }
            }
        }
        val destination = nodes.last().last()
        destination.print()
        return destination.gScore
    }

    fun List<String>.parseNodes() = mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            Node(x, y, c.toString().toInt())
        }.toTypedArray()
    }.toTypedArray()

    fun part1(input: List<String>) = lowestTotalRisk(input.parseNodes())

    fun part2(input: List<String>) = lowestTotalRisk(input.parseNodes().expand(5))

    val testInput = readInput("Day15_test")
    verifyEquals(part1(testInput), 40)

    val input = readInput("Day15")
    println("Part 1 = " + part1(input))

    verifyEquals(part2(testInput), 315)
    println("Part 2 = " + part2(input))
}

