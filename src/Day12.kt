fun main() {

    data class Point(
        val location: String
    ) {
        val connected = mutableSetOf<Point>()
        var visited = false

        override fun toString(): String {
            return location
        }

        fun getRoutes(): List<MutableList<Point>> {
            if (location == "end") {
                return listOf(mutableListOf(this))
            } else if (canVisit()) {
                visited = true
                val updatedRoutes = connected.flatMap {
                    it.getRoutes().filter(MutableList<Point>::isNotEmpty).map { route ->
                        route.add(0, this)
                        route
                    }
                }
                visited = false
                return updatedRoutes
            }
            return listOf(mutableListOf())
        }

        fun getRoutes(route: List<Point>): List<List<Point>> {
            if (location == "end") {
                return listOf(route + this)
            } else if (location.first().isUpperCase() ||
                (route.isEmpty() || location != "start" && (!route.contains(this) ||
                        route.filter { it.location.first().isLowerCase() }.size
                        == (route.filter { it.location.first().isLowerCase() }.toSet().size)))
            ) {
                return connected.flatMap { it.getRoutes(route + this) }
            }
            return emptyList()
        }

        fun canVisit() = !visited || location.first().isUpperCase()
    }

    fun connect(input: List<String>, points: MutableMap<String, Point>) {
        input.forEach {
            val split = it.split("-")
            val a = split[0]
            val b = split[1]
            val pa = Point(a)
            val pb = Point(b)
            val pointA = points.putIfAbsent(a, pa) ?: pa
            val pointB = points.putIfAbsent(b, pb) ?: pb
            pointA.connected.add(pointB)
            pointB.connected.add(pointA)
        }
    }

    fun part1(input: List<String>): Int {
        val points = mutableMapOf<String, Point>()
        connect(input, points)
        val routes = points["start"]!!.getRoutes()
        routes.forEach { route -> println(route.joinToString(",") { it.location }) }
        return routes.size
    }

    fun part2(input: List<String>): Int {
        val points = mutableMapOf<String, Point>()
        connect(input, points)
        val routes = points["start"]!!.getRoutes(emptyList())
        routes.forEach { route -> println(route.joinToString(",") { it.location }) }
        return routes.size
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    verifyEquals(part1(testInput), 10)

    verifyEquals(part2(testInput), 36)

    val input = readInput("Day12")
    println("Part 1 = " + part1(input)) // 4378
    println("Part 2 = " + part2(input)) // 133621
}
