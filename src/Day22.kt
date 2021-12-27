fun main() {

    fun IntRange.intersects(other: IntRange) = first <= other.last && other.first <= last

    fun IntRange.intersectingRange(other: IntRange) = IntRange(
        first.coerceAtLeast(other.first),
        last.coerceAtMost(other.last)
    )

    data class Cuboid(
        val xRange: IntRange,
        val yRange: IntRange,
        val zRange: IntRange,
        val switch: Boolean = true
    ) {

        fun intersects(other: Cuboid) = xRange.intersects(other.xRange)
                && yRange.intersects(other.yRange)
                && zRange.intersects(other.zRange)

        /** We cut cuboids of the pieces intersecting left, right, top, bottom, back and front of the cuboid.*/
        fun cut(other: Cuboid): MutableList<Cuboid> {
            val cuts = mutableListOf<Cuboid>()
            if (other.xRange.first > xRange.first) {
                cuts.add(Cuboid(IntRange(xRange.first, other.xRange.first - 1), yRange, zRange))
            }
            if (other.xRange.last < xRange.last) {
                cuts.add(Cuboid(IntRange(other.xRange.last + 1, xRange.last), yRange, zRange))
            }
            val middleXRange = xRange.intersectingRange(other.xRange)
            if (other.yRange.first > yRange.first) {
                cuts.add(Cuboid(middleXRange, IntRange(yRange.first, other.yRange.first - 1), zRange))
            }
            if (other.yRange.last < yRange.last) {
                cuts.add(Cuboid(middleXRange, IntRange(other.yRange.last + 1, yRange.last), zRange))
            }

            val middleYRange = yRange.intersectingRange(other.yRange)
            if (other.zRange.first > zRange.first) {
                cuts.add(Cuboid(middleXRange, middleYRange, IntRange(zRange.first, other.zRange.first - 1)))
            }
            if (other.zRange.last < zRange.last) {
                cuts.add(Cuboid(middleXRange, middleYRange, IntRange(other.zRange.last + 1, zRange.last)))
            }
            return cuts
        }

        val IntRange.volume get() = (1 + last - first).toLong()

        val volume: Long = xRange.volume * yRange.volume * zRange.volume
    }

    fun getVolume(cuboids: List<Cuboid>) = cuboids.sumOf { it.volume }

    fun parse(input: String): Cuboid {
        val match = Regex(".*?(-?\\d+)".repeat(6)).find(input)!!
        val (x1, x2, y1, y2, z1, z2) = match.destructured
        val switch = input.startsWith("on")
        val xRange = IntRange(x1.toInt(), x2.toInt())
        val yRange = IntRange(y1.toInt(), y2.toInt())
        val zRange = IntRange(z1.toInt(), z2.toInt())
        return Cuboid(xRange, yRange, zRange, switch)
    }

    fun determineSwitchedOnCuboids(input: List<String>): MutableList<Cuboid> {
        var cuboids = mutableListOf<Cuboid>()
        input.map(::parse).forEach { cuboid ->
            val updatedCuboids = mutableListOf<Cuboid>()
            cuboids.forEach { switchedOnCuboid ->
                if (switchedOnCuboid.intersects(cuboid)) {
                    updatedCuboids.addAll(switchedOnCuboid.cut(cuboid))
                } else {
                    updatedCuboids.add(switchedOnCuboid)
                }
            }
            if (cuboid.switch) {
                updatedCuboids.add(cuboid)
            }
            cuboids = updatedCuboids
        }
        return cuboids
    }

    fun part1(input: List<String>): Long {
        val range = IntRange(-50, 50)
        val startingArea = Cuboid(range, range, range)
        val cuboids = determineSwitchedOnCuboids(input).filter { it.intersects(startingArea) }.map {
            Cuboid(
                it.xRange.intersectingRange(range),
                it.yRange.intersectingRange(range),
                it.zRange.intersectingRange(range)
            )
        }
        return getVolume(cuboids)
    }

    fun part2(input: List<String>) = getVolume(determineSwitchedOnCuboids(input))

    val testInput = readInput("Day22_test")
    verifyEquals(part1(testInput), 474140L)

    val input = readInput("Day22")
    println("Part 1 = " + part1(input))

    verifyEquals(part2(testInput), 2758514936282235)
    println("Part 2 = " + part2(input))
}
