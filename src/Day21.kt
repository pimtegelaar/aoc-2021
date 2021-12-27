import kotlin.system.measureTimeMillis

fun main() {
    val d3Range = IntRange(1, 3)

    val possibleRolls = sequence {
        d3Range.forEach { i1 ->
            d3Range.forEach { i2 ->
                d3Range.forEach { i3 ->
                    yield(i1 + i2 + i3)
                }
            }
        }
    }.toList().groupingBy { it }.eachCount()

    class DeterministicDie(val sides: Int) {
        var current = 0
        var rolls = 0

        fun roll(): Int {
            if (current == sides) current = 1 else current++
            return current
        }

        fun roll(times: Int): Int {
            var result = 0
            for (i in 1..times) {
                result += roll()
                rolls++
            }
            return result
        }

        override fun toString(): String {
            return "$rolls + $current"
        }
    }

    class Player(
        val startPosition: Int,
        val trackSize: Int
    ) {
        var score: Int = 0
        var position = startPosition
        var totalRolls: Int = 0

        val loseMap = mutableMapOf<Int, Long>()
        val winMap = mutableMapOf<Int, Long>()

        fun takeTurn(rolls: Int, die: DeterministicDie, maxScore: Int): Boolean {
            val rollSum = die.roll(rolls)
            position = (position + rollSum) % trackSize
            if (position == 0) position = 10
            score += position
            totalRolls += rolls
            return score >= maxScore
        }

        fun dirac(rollCount: Int, position: Int, score: Int, universes: Long, maxScore: Int) {
            possibleRolls.forEach { (rollSum, count) ->
                var newPosition = ((position + rollSum) % 10)
                if (newPosition == 0) newPosition = 10
                val newScore = score + newPosition
                val newUniverses = universes * count
                if (newScore >= maxScore) {
                    winMap[rollCount] = winMap[rollCount]?.plus(newUniverses) ?: newUniverses
                } else {
                    loseMap[rollCount] = loseMap[rollCount]?.plus(newUniverses) ?: newUniverses
                    dirac(rollCount + 1, newPosition, newScore, newUniverses, maxScore)
                }
            }
        }

        fun dirac(maxScore: Int) {
            dirac(rollCount = 2, position = startPosition, score = 0, universes = 1, maxScore = maxScore)
        }

        fun player1Wins(other: Player) = winMap.map { (i, count) -> (other.loseMap[i - 1] ?: 0) * count }.sum()

        fun player2Wins(other: Player) = winMap.map { (i, count) -> (other.loseMap[i] ?: 0) * count }.sum()

        override fun toString(): String {
            return "Score: $score Total rolls: $totalRolls"
        }
    }

    fun part1(p1s: Int, p2s: Int): Int {
        val trackSize = 10
        val maxScore = 1000
        val player1 = Player(startPosition = p1s, trackSize = trackSize)
        val player2 = Player(startPosition = p2s, trackSize = trackSize)
        val die = DeterministicDie(100)
        var reached = false
        while (!reached) {
            reached = player1.takeTurn(3, die, maxScore) || player2.takeTurn(3, die, maxScore)
        }
        return player1.score.coerceAtMost(player2.score) * die.rolls
    }

    fun part2(p1s: Int, p2s: Int): Long {
        val trackSize = 10
        val maxScore = 21
        val player1 = Player(startPosition = p1s, trackSize = trackSize)
        val player2 = Player(startPosition = p2s, trackSize = trackSize)
        player1.dirac(maxScore)
        player2.dirac(maxScore)
        val player1Wins = player1.player1Wins(player2)
        val player2Wins = player2.player2Wins(player1)
        return player1Wins.coerceAtLeast(player2Wins)
    }

    verifyEquals(part1(4, 8), 739785)
    verifyEquals(part2(4, 8), 444356092776315)

    val part1: Int
    val part2: Long
    val duration = measureTimeMillis {
        part1 = part1(4, 7)
        part2 = part2(4, 7)
    }
    println("Part 1 = $part1")
    println("Part 2 = $part2")
    println("It took: ${duration}ms")
}
