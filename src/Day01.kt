fun main() {

    fun sumIncreased(sweep: List<Int>): Int {
        val increasedMeasures = sweep.filterIndexed { i, item -> sweep.increased(i, item) }
        return increasedMeasures.size
    }

    fun threeMeasurementWindow(input: List<Int>): List<Int> = input.mapIndexed { index, item ->
        if (index < (input.size - 2))
            item + input[index + 1] + input[index + 2]
        else
            item
    }.dropLast(2)

    fun part1(input: List<String>): Int {
        val sweep = input.map { it.toInt() }
        return sumIncreased(sweep)
    }

    fun part2(input: List<String>): Int {
        val sweep = input.map { it.toInt() }
        return sumIncreased(threeMeasurementWindow(sweep))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    verifyEquals(part1(testInput), 7)
    verifyEquals(part2(testInput), 5)

    val input = readInput("Day01")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

/**
 * Returns whether an item increased in value relative to the item before it.
 **/
private fun List<Int>.increased(index: Int, item: Int) = index > 0 && item > this[index - 1]