fun main() {

    data class Point(
        val x: Int,
        val y: Int
    )

    data class Path(
        val points: List<Point>
    ) {
        fun largestY() = points.maxOf { it.y }
    }

    fun calculate(
        xRange: IntRange,
        yRange: IntRange,
        initialXV: Int,
        initialYV: Int,
    ): Path? {
        var xV = initialXV
        var yV = initialYV
        var x = 0
        var y = 0
        val points = mutableListOf<Point>()
        while (y > yRange.first) {
            x += xV
            y += yV
            points.add(Point(x, y))
            if (x in xRange && y in yRange) {
                return Path(points)
            }
            yV--
            if (xV > 0) xV--
            if (xV < 0) xV++
        }
        return null
    }

    fun highest(xRange: IntRange, yRange: IntRange): Int {
        val biggest = mutableSetOf<Int>()
        for (x in 1..xRange.first) {
            for (y in 1..500) {
                calculate(xRange, yRange, x, y)?.largestY()?.run {
                    biggest.add(this)
                }
            }
        }
        return biggest.maxOrNull() ?: 0
    }

    fun reachable(xRange: IntRange, yRange: IntRange): Int {
        val reachable = mutableSetOf<Point>()
        for (x in 1..xRange.last) {
            for (y in -500..500) {
                calculate(xRange, yRange, x, y)?.run {
                    reachable.add(Point(x, y))
                }
            }
        }
        return reachable.size
    }

    fun parseRanges(input: String): Pair<IntRange, IntRange> {
        val match = Regex(".*?(-?\\d+).*?(-?\\d+).*?(-?\\d+).*?(-?\\d+)").find(input)!!
        val (x1, x2, y1, y2) = match.destructured
        return Pair(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt())
    }

    fun part1(input: String): Int {
        val (xRange, yRange) = parseRanges(input)
        return highest(xRange, yRange)
    }

    fun part2(input: String): Int {
        val (xRange, yRange) = parseRanges(input)
        return reachable(xRange, yRange)
    }

    val testInput = "target area: x=20..30, y=-10..-5"
    verifyEquals(part1(testInput), 45)

    val input = readInput("Day17").first()
    println("Part 1 = " + part1(input))

    verifyEquals(part2(testInput), 112)
    println("Part 2 = " + part2(input))
}

