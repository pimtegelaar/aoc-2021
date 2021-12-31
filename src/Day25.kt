import kotlin.system.measureTimeMillis

fun main() {

    data class Position(
        val x: Int,
        val y: Int,
        var value: Char
    ) {
        var right: Position? = null
        var bottom: Position? = null

        fun canMoveEast() = value == '>' && right!!.value == '.'
        fun canMoveSouth() = value == 'v' && bottom!!.value == '.'

        fun moveEast() {
            right!!.value = '>'
            value = '.'
        }

        fun moveSouth() {
            bottom!!.value = 'v'
            value = '.'
        }
    }

    fun List<List<Position>>.assignNeighbors() {
        forEachIndexed { y, line ->
            line.forEachIndexed { x, position ->
                val rightX = if (x < line.size - 1) x + 1 else 0
                val bottomY = if (y < size - 1) y + 1 else 0
                position.right = this[y][rightX]
                position.bottom = this[bottomY][x]
            }
        }
    }

    fun part1(input: List<String>): Int {
        val positions = input.mapIndexed { y, line -> line.mapIndexed { x, c -> Position(x, y, c) } }.apply {
            assignNeighbors()
        }
        var step = 1
        while (true) {
            val canMoveEast = positions.flatMap { it.filter(Position::canMoveEast) }.onEach(Position::moveEast)
            val canMoveSouth = positions.flatMap { it.filter(Position::canMoveSouth) }.onEach(Position::moveSouth)
            if (canMoveEast.isEmpty() && canMoveSouth.isEmpty()) break
            step++
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    verifyEquals(part1(testInput), 58)

    val input = readInput("Day25")
    val part1: String
    println("It took: " + measureTimeMillis { part1 = "Part 1 = " + part1(input) })
    println(part1)
}
