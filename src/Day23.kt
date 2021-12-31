import java.util.*
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {

    data class Room(
        val type: Char,
        val index: Int,
        val spots: String
    ) {
        val isOrganized = spots.all { it == type }
        val isUnOrganized = spots.any { it != type } && spots.any { it != '.' }
        val isReadyToAccept = spots.any { it == '.' } && spots.all { it == '.' || it == type }
        val firstOpen = spots.lastIndexOf('.')
        val firstToLeave = if (spots.first() == '.') spots.lastIndexOf('.') + 1 else 0

        fun enter() = Room(type, index, spots.replaceRange(firstOpen, firstOpen + 1, type.toString()))

        fun leave() = Room(type, index, spots.replaceRange(firstToLeave, firstToLeave + 1, "."))
    }

    val scoreMap = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

    val hallwaySpotsForPods = listOf(0, 1, 3, 5, 7, 9, 10)

    val pos = mapOf('A' to 0, 'B' to 1, 'C' to 2, 'D' to 3)

    data class Burrow(
        val hallway: String,
        val rooms: List<Room>
    ) {
        var score: Int = 0
        var visited = false
        val podsInHallway by lazy { hallway.withIndex().filter { it.value.isLetter() } }
        val movablePodsInHallway by lazy { podsInHallway.filter { rooms[pos.getValue(it.value)].isReadyToAccept } }
        val unorganizedRooms by lazy { rooms.filter(Room::isUnOrganized) }
        val isOrganized by lazy { rooms.all(Room::isOrganized) }
        val availableHallwaySpots by lazy { hallwaySpotsForPods.filter { hallway[it] == '.' } }

        private fun hallwayPathClear(start: Int, end: Int) =
            hallway.slice(
                if (start > end) {
                    (start - 1) downTo end
                } else {
                    (start + 1)..end
                }
            ).all { it == '.' }

        private fun findHallwayCandidates(): List<Burrow> = movablePodsInHallway.mapNotNull { (index, spot) ->
            val room = rooms[pos.getValue(spot)]
            if (hallwayPathClear(index, room.index)) {
                val y = room.spots.lastIndexOf('.')
                val newScore = (abs(index - room.index) + y + 1) * scoreMap.getValue(spot)
                Burrow(
                    hallway.replaceRange(index, index + 1, "."),
                    rooms.toMutableList().apply {
                        this[pos.getValue(spot)] = room.enter()
                    }
                ).apply { score = newScore }
            } else {
                null
            }
        }

        private fun findRoomCandidates() = unorganizedRooms.flatMap { room ->
            val (y, pod) = room.spots.withIndex().first { it.value != '.' }
            availableHallwaySpots.mapNotNull { index ->
                if (hallwayPathClear(index, room.index)) {
                    val newScore = (abs(room.index - index) + y + 1) * scoreMap.getValue(pod)
                    Burrow(
                        hallway.replaceRange(index, index + 1, pod.toString()),
                        rooms.toMutableList().apply {
                            this[pos.getValue(room.type)] = room.leave()
                        }
                    ).apply { score = newScore }
                } else {
                    null
                }
            }
        }

        fun findCandidates(): List<Burrow> = findHallwayCandidates() + findRoomCandidates()

        fun organize(): Int {
            val openSet = PriorityQueue(Comparator.comparing(Burrow::score))
            openSet.add(this)
            val scores = mutableMapOf<Burrow, Int>()

            while (openSet.isNotEmpty()) {
                val current = openSet.poll()
                current.visited = true
                current.findCandidates().forEach { next ->
                    if (!next.visited) {
                        val newScore = current.score + next.score
                        if (newScore < (scores[next] ?: Int.MAX_VALUE)) {
                            scores[next] = newScore
                            openSet.add(Burrow(next.hallway, next.rooms).apply { score = newScore })
                        }
                        next.visited = true
                    }
                }
            }
            return scores.firstNotNullOf { (key, value) -> value.takeIf { key.isOrganized } }
        }
    }

    fun String.stripToCore() = drop(1).dropLast(1)

    fun parse(input: List<String>) = with(input.drop(1).dropLast(1).map { it.stripToCore() }) {
        Burrow(
            hallway = first(), rooms = with(drop(1)) {
                listOf(
                    Room('A', 2, map { it[2] }.joinToString("")),
                    Room('B', 4, map { it[4] }.joinToString("")),
                    Room('C', 6, map { it[6] }.joinToString("")),
                    Room('D', 8, map { it[8] }.joinToString(""))
                )
            }
        )
    }

    fun part1(input: List<String>) = parse(input).organize()

    fun List<String>.inject(position: Int, vararg values: String) = take(position) + values + drop(position)

    fun part2(input: List<String>) = parse(input.inject(3, "  #D#C#B#A#  ", "  #D#B#A#C#  ")).organize()

    val input = readInput("Day23")
    println("It took: " + measureTimeMillis {
        println("Part 1 = " + part1(input))
        println("Part 2 = " + part2(input))
    })
}
