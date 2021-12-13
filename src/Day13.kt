const val DOT = "▒"

fun main() {

    data class Point(
        val x: Int,
        val y: Int
    )

    data class Fold(
        val horizontal: Boolean,
        val value: Int
    )

    fun String.mark(x: Int, replacement: String = DOT) = replaceRange(x, x + 1, replacement)

    fun foldHorizontal(sheet: MutableList<String>, fold: Fold) {
        sheet[fold.value] = "_".repeat(sheet.size)

        println()
        sheet.forEach(::println)

        for (y in fold.value until sheet.size) {
            sheet[y].forEachIndexed { x, c ->
                if (c == DOT.first()) {
                    val newY = fold.value - (y - fold.value)
                    sheet[newY] = sheet[newY].mark(x)
                }
            }
        }
        sheet.subList(fold.value, sheet.size).clear()
    }

    fun foldVertical(sheet: MutableList<String>, fold: Fold) {
        sheet.forEachIndexed { y, line ->
            sheet[y] = line.mark(fold.value, "|")
        }

        println()
        sheet.forEach(::println)

        sheet.forEachIndexed { y, line ->
            for (x in fold.value until line.length) {
                if (line[x] == DOT.first()) {
                    val newX = fold.value - (x - fold.value)
                    sheet[y] = sheet[y].mark(newX)
                }
            }
            sheet[y] = sheet[y].dropLast(line.length - fold.value)
        }
    }

    fun fold(sheet: MutableList<String>, fold: Fold) {
        if (fold.horizontal) {
            foldHorizontal(sheet, fold)
        } else {
            foldVertical(sheet, fold)
        }
    }

    fun parsePoints(input: List<String>) = input.filter { it.contains(",") }.map { line ->
        val split = line.split(",")
        Point(split[0].toInt(), split[1].toInt())
    }

    fun parseFolds(input: List<String>) = input.filter { it.startsWith("fold") }.map {
        val split = it.substring(11).split("=")
        Fold(split[0] == "y", split[1].toInt())
    }

    fun createSheet(sheetSize: Int, points: List<Point>) = mutableListOf<String>().apply {
        for (y in 1..sheetSize) {
            add("-".repeat(sheetSize))
        }
        points.forEach { point -> this[point.y] = this[point.y].mark(point.x) }
    }

    fun part1(input: List<String>, sheetSize: Int): Int {
        val points = parsePoints(input)
        val folds = parseFolds(input)
        val sheet = createSheet(sheetSize, points)

        folds.first().run { fold(sheet, this) }

        println()
        sheet.forEach(::println)

        return sheet.sumOf { line -> line.filter { it == DOT.first() }.length }
    }

    fun part2(input: List<String>, sheetSize: Int): Int {
        val points = parsePoints(input)
        val folds = parseFolds(input)
        val sheet = createSheet(sheetSize, points)

        folds.forEach { fold(sheet, it) }

        println()
        sheet.forEach(::println)

        return sheet.sumOf { line -> line.filter { it == DOT.first() }.length }
    }

    val testInput = readInput("Day13_test")
    verifyEquals(part1(testInput, 15), 17)

    val input = readInput("Day13")
    println("Part 1 = " + part1(input, 2000))

    verifyEquals(part2(testInput, 15), 16)
    println("Part 2 = " + part2(input, 2000))
}
