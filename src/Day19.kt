import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {

    data class Point3d(
        val x: Int,
        val y: Int,
        val z: Int
    ) {
        val rotations
            get() = listOf(
                Point3d(x, y, z), Point3d(x, -z, y), Point3d(x, -y, -z), Point3d(x, z, -y), Point3d(-x, -y, z),
                Point3d(-x, -z, -y), Point3d(-x, y, -z), Point3d(-x, z, y), Point3d(-z, x, -y), Point3d(y, x, -z),
                Point3d(z, x, y), Point3d(-y, x, z), Point3d(z, -x, -y), Point3d(y, -x, z), Point3d(-z, -x, y),
                Point3d(-y, -x, -z), Point3d(-y, -z, x), Point3d(z, -y, x), Point3d(y, z, x), Point3d(-z, y, x),
                Point3d(z, y, -x), Point3d(-y, z, -x), Point3d(-z, -y, -x), Point3d(y, -z, -x),
            )

        fun diff(other: Point3d) = Point3d(x - other.x, y - other.y, z - other.z)
        fun sum(other: Point3d) = Point3d(x + other.x, y + other.y, z + other.z)
        fun absDiff(other: Point3d) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    data class Probe(
        val points: MutableSet<Point3d> = mutableSetOf()
    ) {

        fun findTranslation(other: Probe) =
            points.flatMap { point -> other.points.map { otherPoint -> point.diff(otherPoint) } }
                .groupingBy { it }.eachCount().filter { (_, count) -> count >= 12 }.entries.firstOrNull()?.key

        fun merge(other: Probe, translation: Point3d) {
            points.addAll(other.points.map { it.sum(translation) })
        }

        fun getRotations() = mutableListOf<Probe>().apply {
            for (i in 0 until 24) {
                add(Probe().apply {
                    this@Probe.points.forEach { point ->
                        points.add(point.rotations[i])
                    }
                })
            }
        }
    }

    fun parse(input: List<String>) = mutableListOf<Probe>().apply {
        input.filter { it.isNotBlank() }.forEach { line ->
            if (line.startsWith("---")) {
                add(Probe())
            } else {
                val (x, y, z) = line.split(",")
                last().points.add(Point3d(x.toInt(), y.toInt(), z.toInt()))
            }
        }
    }

    fun part1And2(input: List<String>): Pair<Int, Int> {
        val probes = parse(input)
        val firstProbe = probes.first()
        probes.removeFirst()
        val translations = mutableListOf<Point3d>()
        while (probes.isNotEmpty()) {
            run probeLoop@{
                probes.forEach { probe ->
                    probe.getRotations().forEach { rotated ->
                        val translation = firstProbe.findTranslation(rotated)
                        if (translation != null) {
                            translations.add(translation)
                            firstProbe.merge(rotated, translation)
                            probes.remove(probe)
                            return@probeLoop
                        }
                    }
                }
            }
        }
        val maxDistance = translations.flatMap { t1 -> translations.map { t2 -> t1.absDiff(t2) } }.reduce(Math::max)
        return Pair(firstProbe.points.size, maxDistance)
    }

    val testInput = readInput("Day19_test")
    val (testPart1, testPart2) = part1And2(testInput)
    verifyEquals(testPart1, 79)
    verifyEquals(testPart2, 3621)

    println("It took: " + measureTimeMillis {
        val input = readInput("Day19")
        val (part1, part2) = part1And2(input)
        println("Part 1 = $part1")
        println("Part 2 = $part2")
    })
}
