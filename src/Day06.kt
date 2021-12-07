fun main() {

    fun part1(input: List<Int>, days: Int): Int {
        var pool = input
        for (day in 1..days) {
            val newborns = mutableListOf<Int>()
            pool = pool.map {
                if (it == 0) {
                    newborns.add(8)
                    6
                } else it - 1
            } + newborns
        }
        return pool.size
    }

    /** Same as part one, but more performant */
    fun part2(input: List<Int>, days: Int): Long {
        val agePool = mutableListOf<Long>(0, 0, 0, 0, 0, 0, 0, 0, 0)

        input.map {
            agePool[it] = agePool[it] + 1
        }

        for (day in 1..days) {
            val day0 = agePool[0]
            for (i in 0 until agePool.size - 1) {
                agePool[i] = agePool[i + 1]
            }
            agePool[6] = agePool[6] + day0
            agePool[agePool.size - 1] = day0
        }

        return agePool.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")[0].split(",").map { it.toInt() }
    verifyEquals(part1(testInput, 18), 26)
    verifyEquals(part1(testInput, 80), 5934)

    verifyEquals(part2(testInput, 18), 26L)
    verifyEquals(part2(testInput, 80), 5934L)

    val input = readInput("Day06")[0].split(",").map { it.toInt() }
    println("Part 1 = " + part1(input, 80))
    println("Part 2 = " + part2(input, 256))
}

