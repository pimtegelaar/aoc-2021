fun main() {

    fun bits(input: List<String>, index: Int): List<Int> = input.map { it[index].toString().toInt() }

    fun mcb(bits: List<Int>) = if (bits.filter { it == 1 }.size >= bits.filter { it == 0 }.size) 1 else 0
    fun lcb(bits: List<Int>) = if (mcb(bits) == 1) 0 else 1

    fun rate(input: List<String>, bitF: (List<Int>) -> Int): String {
        var result = ""
        for (i in 0 until input[0].length) {
            result += bitF(bits(input, i))
        }
        return result
    }

    fun lsRating(input: List<String>, bitF: (List<Int>) -> Int): String {
        val length = input[0].length
        var result = input
        for (i in 0 until length) {
            val bits = bits(result, i)
            val relevantBit = bitF(bits)
            result = result.filter { it[i].toString().toInt() == relevantBit }
            if (result.size == 1)
                break
        }
        return result.single()
    }

    fun part1(input: List<String>): Int {
        val gamma = Integer.parseInt(rate(input, ::mcb), 2)
        val epsilon = Integer.parseInt(rate(input, ::lcb), 2)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxygenGenerator = Integer.parseInt(lsRating(input, ::mcb), 2)
        val co2Scrubber = Integer.parseInt(lsRating(input, ::lcb), 2)
        return oxygenGenerator * co2Scrubber
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    verifyEquals(part1(testInput), 198)
    verifyEquals(part2(testInput), 230)

    val input = readInput("Day03")
    println("Part 1 = " + part1(input)) // 1025636
    println("Part 2 = " + part2(input)) // 793873
}

