fun main() {

    fun polymerize(template: String, rules: Map<String, String>): String {
        var updated = template
        for (i in template.length downTo 2) {
            val pair = template.substring(i - 2, i)
            updated = updated.replaceRange(i - 1, i - 1, rules[pair]!!)
        }
        return updated
    }

    fun polymerize2(template: MutableList<Char>, rules: Map<String, Char>): MutableList<Char> {
        for (i in template.size downTo 2) {
            val pair = "${template[i - 2]}${template[i - 1]}"
            template.add(i - 1, rules[pair]!!)
        }
        return template
    }

    fun polymerize3(occurrences: MutableMap<String, Long>, rules: Map<String, String>): MutableMap<String, Long> {
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

    fun part1Old(input: List<String>, steps: Int = 10): Int {
        val template = input[0]
        val rules = input.drop(2).associate {
            val split = it.split(" -> ")
            split[0] to split[1]
        }
        var updated = template
        for (i in 1..steps) {
            updated = polymerize(updated, rules)
        }
        val sorted = updated.groupBy { it }.toList().sortedBy { (_, value) -> value.size }.toMap().toList()
        return sorted.last().second.size - sorted.first().second.size
    }

    fun part1(input: List<String>, steps: Int = 10): Int {
        val template = input[0]
        val rules = input.drop(2).associate {
            val split = it.split(" -> ")
            split[0] to split[1][0]
        }
        val updated = template.toCharArray().toMutableList()
        for (i in 1..steps) {
            polymerize2(updated, rules)
        }
        val sorted = updated.groupBy { it }.toList().sortedBy { (_, value) -> value.size }.toMap().toList()
        return sorted.last().second.size - sorted.first().second.size
    }

    fun part2(input: List<String>, steps: Int = 10): Long {
        val template = input[0]
        val rules = input.drop(2).associate {
            val split = it.split(" -> ")
            split[0] to split[1]
        }
        var occurrences = occurrences(template)
        for (i in 1..steps) {
            occurrences = polymerize3(occurrences, rules)
        }
        val grouped = occurrences.entries.groupBy { it.key[0] }
        val sums = grouped.map { entry -> entry.key to entry.value.sumOf { it.value } }
        val sorted = sums.sortedBy { (_, value) -> value }.toMap().toList()

        return sorted.last().second - sorted.first().second
    }

    val testInput = readInput("Day14_test")
    verifyEquals(part1Old(testInput), 1588)
    verifyEquals(part1(testInput), 1588)

    val input = readInput("Day14")
    println("Part 1 = " + part1Old(input, 10))
    println("Part 1 = " + part1(input, 10))

    verifyEquals(part2(testInput, 40), 2188189693529 - 1)
    val start = System.currentTimeMillis()
    println("Part 2 = " + (part2(input, 40) - 1)) // 2911561572631
    val duration = System.currentTimeMillis() - start
    println("It took: $duration")
}

