fun main() {
    var flashes = 0

    data class Point(
        val x: Int,
        val y: Int,
        var value: Int
    ) {
        var top: Point? = null
        var bottom: Point? = null
        var left: Point? = null
        var right: Point? = null
        var tl: Point? = null
        var tr: Point? = null
        var bl: Point? = null
        var br: Point? = null

        var flashed = false

        fun surrounding() = mutableListOf(top, bottom, left, right, tl, tr, bl, br).filterNotNull()

        fun increase() {
            if (flashed)
                return
            if (value == 9) {
                flash()
                value = 0
            } else {
                value += 1
            }
        }

        fun reset() {
            flashed = false
        }

        fun flash() {
            flashed = true
            flashes++
            surrounding().forEach(Point::increase)
        }
    }

    fun List<Point>.safeGet(x: Int): Point? =
        if (x < 0 || x >= size) null else this[x]

    fun List<List<Point>>.safeGet(x: Int, y: Int): Point? =
        if (y < 0 || y >= size) null else this[y].safeGet(x)

    fun assignNeighbors(rows: List<List<Point>>) {
        rows.forEach { points ->
            points.forEach { point ->
                point.apply {
                    left = points.safeGet(x - 1)
                    right = points.safeGet(x + 1)
                    top = rows.safeGet(x, y - 1)
                    bottom = rows.safeGet(x, y + 1)
                    tl = rows.safeGet(x - 1, y - 1)
                    tr = rows.safeGet(x + 1, y - 1)
                    bl = rows.safeGet(x - 1, y + 1)
                    br = rows.safeGet(x + 1, y + 1)
                }
            }
        }
    }

    fun printStep(step: Int, rows: List<List<Point>>) {
        println("Step $step")
        rows.forEach { points ->
            println(points.map { it.value }.joinToString("").replace("0", "☀"))
        }
        println()
    }

    fun parse(input: List<String>) = input.mapIndexed { rowIndex, line ->
        line.mapIndexed { index, c ->
            Point(x = index, y = rowIndex, value = c.toString().toInt())
        }
    }

    fun navigate(input: List<String>, maxSteps: Int = 100, synchronize: Boolean = false): Int {
        flashes = 0
        var lastFlashes = 0
        val rows = parse(input)
        assignNeighbors(rows)
        for (i in 1..maxSteps) {
            rows.forEach { points ->
                points.forEach(Point::increase)
            }
            if (synchronize && flashes - lastFlashes == 100) {
                return i
            }
            lastFlashes = flashes
            rows.forEach { points ->
                points.forEach(Point::reset)
            }
            printStep(i, rows)
        }
        return flashes
    }

    fun part1(input: List<String>): Int {
        return navigate(input)
    }

    fun part2(input: List<String>): Int {
        return navigate(input = input, maxSteps = 500, synchronize = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    verifyEquals(part1(testInput), 1656)

    verifyEquals(part2(testInput), 195)

    val input = readInput("Day11")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

