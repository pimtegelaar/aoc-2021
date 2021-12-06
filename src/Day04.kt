fun main() {

    data class BingoCard(
        val numbers: List<Int>
    ) {
        val card = mutableListOf(
            "-----",
            "-----",
            "-----",
            "-----",
            "-----"
        )

        var bingoClaimed = false

        fun rowBingo(row: Int) = card[row] == "xxxxx"

        fun columnBingo(column: Int) = card.filter { it[column] == 'x' }.size == 5

        fun bingo(number: Int): Boolean = if (!bingoClaimed && numbers.contains(number)) {
            val index = numbers.indexOf(number)
            val row = index.div(5)
            val column = index.mod(5)
            card[row] = card[row].replaceRange(column, column + 1, "x")
            val bingo = rowBingo(row) || columnBingo(column)
            if (bingo) bingoClaimed = true
            bingo
        } else {
            false
        }

        fun unmarkedNumbers(): Int {
            var result = 0
            card.joinToString("").forEachIndexed { index, c ->
                if (c == '-') {
                    result += numbers[index]
                }
            }
            return result
        }
    }

    fun getCards(input: List<String>) = input.chunked(6).map { chunk ->
        BingoCard(
            chunk.drop(1)
                .joinToString(" ") {
                    it.trim().replace("\\s+".toRegex(), " ")
                }
                .trim()
                .split(" ")
                .map { it.toInt() }
        )
    }

    fun getDraws(input: String) = input.split(",").map { it.toInt() }

    fun part1(input: List<String>): Int {
        val draws = getDraws(input[0])
        val cards = getCards(input.drop(1))
        draws.forEach { draw ->
            cards.forEach { card ->
                val bingo = card.bingo(draw)
                if (bingo) {
                    val unmarked = card.unmarkedNumbers()
                    return unmarked * draw
                }
            }
        }
        throw IllegalStateException("Nobody won!")
    }

    fun part2(input: List<String>): Int {
        val draws = getDraws(input[0])
        val cards = getCards(input.drop(1))
        var claims = 0
        draws.forEach { draw ->
            cards.forEach { card ->
                val bingo = card.bingo(draw)
                if (bingo) {
                    claims += 1
                    if (claims == cards.size) {
                        val unmarked = card.unmarkedNumbers()
                        return unmarked * draw
                    }
                }
            }
        }
        throw IllegalStateException("Nobody won!")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    verifyEquals(part1(testInput), 4512)
    verifyEquals(part2(testInput), 1924)

    val input = readInput("Day04")
    println("Part 1 = " + part1(input)) // 41503
    println("Part 2 = " + part2(input)) // 3178
}
