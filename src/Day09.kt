fun main() {

    data class Point(
        val x: Int,
        val y: Int,
        val value: Int,
    ) {
        var top: Point? = null
        var bottom: Point? = null
        var left: Point? = null
        var right: Point? = null

        var isBasin = false

        var basinSize = 0

        fun checkBasin() {
            isBasin = (top == null || value < top!!.value)
                    && (bottom == null || value < bottom!!.value)
                    && (left == null || value < left!!.value)
                    && (right == null || value < right!!.value)
        }

        fun updateBasin() {
            val basin = findBasin(this)
            basin?.apply { basinSize += 1 }
        }

        fun findBasin(other: Point): Point? {
            if (isBasin)
                return this
            if (value == 9 || this != other && value >= other.value) {
                return null
            }
            return top?.findBasin(this)
                ?: bottom?.findBasin(this)
                ?: left?.findBasin(this)
                ?: right?.findBasin(this)
        }
    }

    fun ltTop(input: List<String>, rowIndex: Int, index: Int, c: Char) =
        rowIndex == 0 || c < input[rowIndex - 1][index]

    fun ltBottom(input: List<String>, rowIndex: Int, index: Int, c: Char) =
        rowIndex == input.size - 1 || c < input[rowIndex + 1][index]

    fun ltLeft(line: String, index: Int, c: Char) =
        index == 0 || c < line[index - 1]

    fun ltRight(line: String, index: Int, c: Char) =
        index == line.length - 1 || c < line[index + 1]

    fun part1(input: List<String>) =
        input.mapIndexed { rowIndex, line ->
            line.mapIndexed { index, c ->
                if (ltTop(input, rowIndex, index, c)
                    && ltBottom(input, rowIndex, index, c)
                    && ltLeft(line, index, c)
                    && ltRight(line, index, c)
                ) c.toString().toInt() + 1
                else 0
            }.sum()
        }.sum()

    fun part2(input: List<String>): Int {
        val rows = input.mapIndexed { rowIndex, line ->
            line.mapIndexed { index, c ->
                Point(x = index, y = rowIndex, value = c.toString().toInt())
            }
        }
        rows.forEach { points ->
            points.forEach { point ->
                if (point.x > 0)
                    point.left = points[point.x - 1]
                if (point.x < points.size - 1)
                    point.right = points[point.x + 1]
                if (point.y > 0)
                    point.bottom = rows[point.y - 1][point.x]
                if (point.y < rows.size - 1)
                    point.top = rows[point.y + 1][point.x]
            }
        }
        rows.forEach { points ->
            points.map(Point::checkBasin)
        }
        rows.forEach { points ->
            points.map(Point::updateBasin)
        }
        return rows.flatMap { points ->
            points.filter(Point::isBasin)
                .map { it.basinSize }
        }.sorted()
            .takeLast(3)
            .reduce(Int::times)
    }

    val testInput = readInput("Day09_test")
    verifyEquals(part1(testInput), 15)

    verifyEquals(part2(testInput), 1134)

    val input = readInput("Day09")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

