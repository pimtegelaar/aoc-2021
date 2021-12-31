import kotlin.system.measureTimeMillis

fun main() {

    fun polymerize(occurrences: MutableMap<String, Long>, rules: Map<String, String>): MutableMap<String, Long> {
        val newOccurrences = mutableMapOf<String, Long>()
        occurrences.forEach {
            val c = rules[it.key]!!
            val c1 = it.key[0] + c
            val c2 = c + it.key[1]
            newOccurrences[c1] = newOccurrences.getOrDefault(c1, 0) + it.value
            newOccurrences[c2] = newOccurrences.getOrDefault(c2, 0) + it.value
        }
        return newOccurrences
    }

    fun occurrences(template: String) = with(mutableMapOf<String, Long>().withDefault { 0 }) {
        template.windowed(2, 1).forEach {
            this[it] = getValue(it) + 1
        }
        this
    }

    fun part1And2(input: List<String>, steps: Int = 10): Long {
        val rules = input.drop(2).associate {
            val (pair, toInsert) = it.split(" -> ")
            pair to toInsert
        }
        var occurrences = occurrences(input[0])
        for (i in 1..steps) {
            occurrences = polymerize(occurrences, rules)
        }
        val sorted = occurrences.entries
            .groupBy { it.key[0] }.values
            .map { it.sumOf { occurrence -> occurrence.value } }
            .sortedBy { it }

        return sorted.last() - sorted.first()
    }

    val testInput = readInput("Day14_test")
    verifyEquals(part1And2(testInput, 10) + 1, 1588L)
    verifyEquals(part1And2(testInput, 40) + 1, 2188189693529)

    val input = readInput("Day14")

    println("It took: " + measureTimeMillis {
        println("Part 1 = " + (part1And2(input, 10) - 1)) // 2657
        println("Part 2 = " + (part1And2(input, 40) - 1)) // 2911561572630
    })
}
