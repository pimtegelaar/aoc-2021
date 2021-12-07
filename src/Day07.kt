import kotlin.math.abs

fun main() {

    val mileage = mutableMapOf<Int, Int>()

    mileage[0] =0
    for (i in 1..2000) {
        mileage[i] = mileage[i - 1]!! + i
    }

    fun part1(input: List<Int>): Int {
        val max = input.maxOrNull()!!
        val min = input.minOrNull()!!
        val fuelPerLocation = mutableMapOf<Int, Int>()
        for (i in min..max) {
            input.forEach { position ->
                val distance = abs(position - i)
                val totalFuelUsed = fuelPerLocation[i] ?: 0
                fuelPerLocation[i] = totalFuelUsed + distance
            }
        }
        val minX = fuelPerLocation.minByOrNull { it.value }
        return minX!!.value
    }

    fun part2(input: List<Int>): Int {
        val max = input.maxOrNull()!!
        val min = input.minOrNull()!!
        val fuelPerLocation = mutableMapOf<Int, Int>()
        for (i in min..max) {
            input.forEach { position ->
                val distance = abs(position - i)
                val totalFuelUsed = fuelPerLocation[i] ?: 0
                fuelPerLocation[i] = totalFuelUsed + mileage[distance]!!
            }
        }
        val minX = fuelPerLocation.minByOrNull { it.value }
        return minX!!.value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")[0].split(",").map { it.toInt() }
    verifyEquals(part1(testInput), 37)
    verifyEquals(part2(testInput), 168)

    val input = readInput("Day07")[0].split(",").map { it.toInt() }
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

