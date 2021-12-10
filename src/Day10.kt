import java.lang.IllegalStateException

fun main() {

    fun Char.isClosing() = this in ")]}>"

    fun Char.matchingClose() = when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalStateException("$this is not in the list of opening characters")
    }

    fun checkForCorruption(line: String): Char? {
        val stack = mutableListOf<Char>()
        line.forEach { c ->
            if (c.isClosing()) {
                if (stack.isNotEmpty() && stack.last().matchingClose() == c) {
                    stack.removeLast()
                } else {
                    return c
                }
            } else {
                stack.add(c)
            }
        }
        return null
    }

    fun complete(line: String): List<Char> {
        val stack = mutableListOf<Char>()
        line.forEach { c ->
            if (c.isClosing() && stack.last().matchingClose() == c) {
                stack.removeLast()
            } else {
                stack.add(c)
            }
        }
        return stack.map { it.matchingClose() }.reversed()
    }

    fun corruptionPoints(c: Char): Int = when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> 0
    }

    fun completionPoints(c: Char): Int = when (c) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> 0
    }

    fun completeScore(chars: List<Char>): Long {
        var result = 0L
        chars.forEach {
            result = result * 5 + completionPoints(it)
        }
        return result
    }

    fun part1(input: List<String>) = input.mapNotNull(::checkForCorruption).sumOf { corruptionPoints(it) }

    fun part2(input: List<String>): Long {
        val incompleteLines = input.filter { checkForCorruption(it) == null }
        val scores = incompleteLines.map(::complete).map(::completeScore).sorted()
        return scores[(scores.size / 2)]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    verifyEquals(part1(testInput), 26397)

    verifyEquals(part2(testInput), 288957L)

    val input = readInput("Day10")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}
