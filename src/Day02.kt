fun main() {

    data class Coordinates(
        val x: Int = 0, val z: Int = 0
    )

    fun dive(startLocation: Coordinates, input: List<String>): Coordinates {
        var (x, z) = startLocation
        input.map {
            val s = it.split(' ')
            val value = s[1].toInt()
            when (s[0]) {
                "forward" -> x += value
                "up" -> z -= value
                "down" -> z += value
            }
        }
        return Coordinates(x, z)
    }

    fun aimedDive(startLocation: Coordinates, input: List<String>): Coordinates {
        var (x, z) = startLocation
        var aim = 0
        input.map {
            val s = it.split(' ')
            val value = s[1].toInt()
            when (s[0]) {
                "forward" -> {
                    x += value
                    z += aim * value
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }
        return Coordinates(x, z)
    }

    fun part1(input: List<String>): Int {
        val (x, z) = dive(Coordinates(0, 0), input)
        return x * z
    }

    fun part2(input: List<String>): Int {
        val (x, z) = aimedDive(Coordinates(0, 0), input)
        return x * z
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    verifyEquals(part1(testInput), 150)
    verifyEquals(part2(testInput), 900)

    val input = readInput("Day02")
    println("Part 1 = " + part1(input))
    println("Part 2 = " + part2(input))
}

