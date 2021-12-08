fun main() {

    fun determineNumbers(signals: List<String>): MutableList<String> {
        val one = signals.singleOfLength(2)
        val seven = signals.singleOfLength(3)
        val four = signals.singleOfLength(4)
        val eight = signals.singleOfLength(7)

        val length6 = signals.ofLength(6).toMutableList()
        val nine = length6.single { it.containsAll(four) }
        length6.remove(nine)
        val zero = length6.single { it.containsAll(one) }
        length6.remove(zero)
        val six = length6.single()

        val length5 = signals.ofLength(5).toMutableList()
        val three = length5.single { it.containsAll(one) }
        length5.remove(three)
        val five = length5.single { six.containsAll(it) }
        length5.remove(five)
        val two = length5.single()

        return mutableListOf(zero, one, two, three, four, five, six, seven, eight, nine)
    }

    fun displayed(numbers: List<String>, output: List<String>) = output.map { numbers.indexOf(it) }.joinToString("")

    fun part1(input: List<String>) = input.sumOf { line ->
        line.split("|")[1]
            .split(" ")
            .map { it.length }
            .filter { it in listOf(2, 3, 4, 7) }
            .size
    }

    fun part2(input: List<String>) = input.sumOf {
        val line = it.split(" | ")
        val signals = line[0].split(" ").map { signal -> signal.sort() }
        val output = line[1].split(" ").map { output -> output.sort() }
        val numbers = determineNumbers(signals)
        displayed(numbers, output).toInt()
    }

    val testInput = readInput("Day08_test")
    verifyEquals(part1(testInput), 26)

    verifyEquals(part2(testInput), 61229)

    val input = readInput("Day08")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

