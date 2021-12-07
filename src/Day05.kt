import kotlin.math.abs

fun main() {

    data class Point(
        val x: Int,
        val y: Int
    )

    data class Line(
        val start: Point,
        val end: Point
    ) {
        fun isVertical() = start.x == end.x
        fun isHorizontal() = start.y == end.y
        fun isDiagonal() = abs(start.x - end.x) == abs(start.y - end.y)
    }

    val field = mutableMapOf<Point, Int>()

    fun setupField(size: Int) {
        field.clear()
        for (y in 0 until size) {
            for (x in 0 until size) {
                field[Point(x, y)] = 0
            }
        }
    }

    val lineRegex = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")

    fun draw(line: Line, drawDiagonal: Boolean = false) {
        if (line.isHorizontal()) {
            val x1 = line.start.x.coerceAtMost(line.end.x)
            val x2 = line.start.x.coerceAtLeast(line.end.x)
            for (x in x1..x2) {
                val point = Point(x, line.start.y)
                field[point] = field[point]?.inc() ?: 1
            }
        } else if (line.isVertical()) {
            val y1 = line.start.y.coerceAtMost(line.end.y)
            val y2 = line.start.y.coerceAtLeast(line.end.y)
            for (y in y1..y2) {
                val point = Point(line.start.x, y)
                field[point] = field[point]?.inc() ?: 1
            }
        } else if (drawDiagonal && line.isDiagonal()) {
            val ltr = line.start.x < line.end.x
            val start = if (ltr) line.start else line.end
            val end = if (ltr) line.end else line.start
            val goingDown = start.y < end.y
            if (goingDown) {
                var y = start.y
                for (x in start.x..end.x) {
                    val point = Point(x, y)
                    field[point] = field[point]?.inc() ?: 1
                    y++
                }
            } else {
                var y = start.y
                for (x in start.x..end.x) {
                    val point = Point(x, y)
                    field[point] = field[point]?.inc() ?: 1
                    y--
                }
            }
        }

    }

    fun countOverlap(): Int {
        return field.filter { it.value > 1 }.size
    }

    fun printAll(size: Int) {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val value = field[Point(x, y)]
                print(if (value == 0) "-" else value)
            }
            println()
        }
    }

    fun getLines(input: List<String>) = input.map {
        val (x1, y1, x2, y2) = lineRegex.find(it)!!.destructured
        Line(
            Point(x1.toInt(), y1.toInt()),
            Point(x2.toInt(), y2.toInt())
        )
    }

    fun part1(input: List<String>, size: Int): Int {
        setupField(size)
        val lines = getLines(input)
        lines.forEach(::draw)
        return countOverlap()
    }

    fun part2(input: List<String>, size: Int): Int {
        setupField(size)
        val lines = getLines(input)
        lines.forEach { draw(line = it, drawDiagonal = true) }
        return countOverlap()
    }

    val testInput = readInput("Day05_test")
    verifyEquals(part1(testInput, 10), 5)
    printAll(10)
    println()
    verifyEquals(part2(testInput, 10), 12)
    printAll(10)

    val input = readInput("Day05")
    println("Part 1 = " + part1(input, 1000))
//    printAll(1000)
    println("Part 2 = " + part2(input, 1000))
//    printAll(1000)
}

