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

    fun occurrences(template: String): MutableMap<String, Long> {
        val result = mutableMapOf<String, Long>()
        template.windowed(2, 1).forEach {
            result[it] = result.getOrDefault(it, 0) + 1
        }
        return result
    }

    fun part1And2(input: List<String>, steps: Int = 10): Long {
        val template = input[0]
        val rules = input.drop(2).associate {
            val split = it.split(" -> ")
            split[0] to split[1]
        }
        var occurrences = occurrences(template)
        for (i in 1..steps) {
            occurrences = polymerize(occurrences, rules)
        }
        val grouped = occurrences.entries.groupBy { it.key[0] }
        val sums = grouped.map { entry -> entry.key to entry.value.sumOf { it.value } }
        val sorted = sums.sortedBy { (_, value) -> value }.toMap().toList()

        return sorted.last().second - sorted.first().second
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
