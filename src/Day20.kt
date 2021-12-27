fun main() {

    class Enhancer(
        val algorithm: String,
        val rawImage: List<String>
    ) {
        fun String.pixel() = algorithm[Integer.parseInt(this, 2)]

        fun List<String>.extraLine(c: Char) = c.toString().repeat(size + 2)

        fun List<String>.enlarge(c: Char) = listOf(extraLine(c)) + this.map { "$c$it$c" } + extraLine(c)

        fun List<String>.box(x: Int, y: Int) =
            this[y - 1].substring(x - 1, x + 2) + this[y].substring(x - 1, x + 2) + this[y + 1].substring(x - 1, x + 2)

        fun List<String>.print() {
            println()
            forEach { line ->
                println(line.replace('0', '.').replace('1', '#'))
            }
        }

        fun enhance(count: Int): Enhancer {
            val infiniteChar = if (algorithm.first() == '0') '0' else if (count % 2 == 0) '1' else '0'
            val enlarged = rawImage.enlarge(infiniteChar).enlarge(infiniteChar)
            val newImage = mutableListOf<String>()
            for (y in 1 until enlarged.size - 1) {
                val newLine = mutableListOf<Char>()
                for (x in 1 until enlarged.first().length - 1) {
                    val pixel = enlarged.box(x, y).pixel()
                    newLine.add(pixel)
                }
                newImage.add(newLine.joinToString(""))
            }
            return (Enhancer(algorithm, newImage))
        }

        fun litPixels() = rawImage.sumOf { line -> line.filter { it == '1' }.length }
    }

    fun String.toBinary() = replace('.', '0').replace('#', '1')
    fun List<String>.toBinary() = map { it.toBinary() }

    fun parse(input: List<String>) = Enhancer(algorithm = input[0].toBinary(), rawImage = input.drop(2).toBinary())

    fun part1(input: List<String>): Int {
        return parse(input).enhance(1).enhance(2).litPixels()
    }

    fun part2(input: List<String>): Int {
        var enhancer = parse(input)
        for (i in 1..50) {
            enhancer = enhancer.enhance(i)
        }
        return enhancer.litPixels()
    }

    val testInput = readInput("Day20_test")
    verifyEquals(part1(testInput), 35)

    val input = readInput("Day20")
    println("Part 1 = " + part1(input))

    verifyEquals(part2(testInput), 3351)
    println("Part 2 = " + part2(input))
}

