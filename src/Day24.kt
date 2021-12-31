fun main() {

    fun IntArray.toLong() = joinToString("").toLong()

    fun part1And2(parameters: List<List<Int>>): Pair<Long, Long> {
        val part1 = IntArray(14) { -1 }
        val part2 = IntArray(14) { -1 }
        val stack = ArrayDeque<Pair<Int, Int>>()
        parameters.forEachIndexed { index, instructions ->
            if (instructions.first() == 26) {
                val (lastIndex, lastOffset) = stack.removeFirst()
                val difference = instructions[1] + lastOffset
                if (difference >= 0) {
                    part1[lastIndex] = 9 - difference
                    part1[index] = 9
                    part2[lastIndex] = 1
                    part2[index] = 1 + difference
                } else {
                    part1[lastIndex] = 9
                    part1[index] = 9 + difference
                    part2[lastIndex] = 1 - difference
                    part2[index] = 1
                }
            } else {
                stack.addFirst(index to instructions.last())
            }
        }

        return part1.toLong() to part2.toLong()
    }

    fun String.instructionValue() = split(" ")[2].toInt()

    val parameters = readInput("Day24").chunked(18).map {
        listOf(
            it[4].instructionValue(), // div z (1 or 26)
            it[5].instructionValue(), // add x ?
            it[15].instructionValue() // add y ?
        )
    }
    println(part1And2(parameters))
}
