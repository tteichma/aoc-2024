import java.util.PriorityQueue

private class Day20(data: List<List<Boolean>>, start: IntCoordinate, end: IntCoordinate) :
    DataMap<Boolean>(data) {
    val distFromStart = getDistancesToCoordinate(start)
    val distsToEnd = getDistancesToCoordinate(end)
    val startToEndWithoutCheats = distsToEnd[start]!!

    companion object {
        fun fromInput(input: List<String>): Day20 {
            var start: IntCoordinate? = null
            var end: IntCoordinate? = null
            val data =
                input
                    .withIndex()
                    .map { (indLine, line) ->
                        line
                            .withIndex()
                            .map {
                                when (it.value) {
                                    '#' -> false
                                    '.' -> true
                                    'S' -> {
                                        start = IntCoordinate(indLine, it.index)
                                        true
                                    }

                                    'E' -> {
                                        end = IntCoordinate(indLine, it.index)
                                        true
                                    }

                                    else -> TODO("Need to implement handling of character $it")
                                }
                            }
                    }

            return Day20(data, start!!, end!!)
        }
    }

    fun getDistancesToCoordinate(initialCoordinate: IntCoordinate): Map<IntCoordinate, Long> {
        val distances = mutableMapOf<IntCoordinate, Long>()

        val upcoming = PriorityQueue(
            16,
            Comparator<Pair<IntCoordinate, Long>> { a, b ->
                return@Comparator a.second.compareTo(b.second)
            })
        upcoming.add(Pair(initialCoordinate, 0L))

        while (upcoming.isNotEmpty()) {
            val (coordinate, cost) = upcoming.remove()
            if (coordinate in distances) continue

            distances[coordinate] = cost

            for (neighbour in coordinate.getNeighbours { data[it] }) { // Non-blocked neighbours
                upcoming.add(Pair(neighbour, cost + 1))
            }
        }

        return distances
    }

    // savingPredicate: Function of time saved, that is ture if it should be contained.
    fun solve(allowedCheatDistance: Int, savingPredicate: (Long) -> Boolean): Long {

        val savedTimeCounts = mutableMapOf<Long, Long>()
        for ((coordinateCheatStart, distStartToCheat) in distFromStart) {
            val cheatEndCoordinatesWithCheatDist = sequence {
                for (i1 in 0..allowedCheatDistance) {
                    for (i2 in 0..allowedCheatDistance - i1) {
                        val cheatDistance = i1 + i2
                        yield(
                            Pair(
                                IntCoordinate(
                                    coordinateCheatStart.first + i1,
                                    coordinateCheatStart.second + i2
                                ), cheatDistance
                            )
                        )
                        yield(
                            Pair(
                                IntCoordinate(
                                    coordinateCheatStart.first - i1,
                                    coordinateCheatStart.second + i2
                                ), cheatDistance
                            )
                        )
                        yield(
                            Pair(
                                IntCoordinate(
                                    coordinateCheatStart.first + i1,
                                    coordinateCheatStart.second - i2
                                ), cheatDistance
                            )
                        )
                        yield(
                            Pair(
                                IntCoordinate(
                                    coordinateCheatStart.first - i1,
                                    coordinateCheatStart.second - i2
                                ), cheatDistance
                            )
                        )
                    }
                }
            }
                .toSet()  // Remove duplicates from i==0  and i== allowedCheatDistance
                .filter { it.first.isWithinBoundaries() && data[it.first] }

            for ((coordinateCheatEnd, cheatDist) in cheatEndCoordinatesWithCheatDist) {
                val savedDistance =
                    startToEndWithoutCheats - (distStartToCheat + cheatDist + distsToEnd[coordinateCheatEnd]!!)

                if (savedDistance > 0) {
                    savedTimeCounts[savedDistance] = (savedTimeCounts[savedDistance] ?: 0) + 1
                }
            }
        }

        return savedTimeCounts.filter { savingPredicate(it.key) }.values.sum()
    }
}

fun main() {
    // Or read a large test input from the `src/Day20_test.txt` file:
    val testInput = readInput("Day20_test")

    val testData1 = mapOf(
        2L to 14L,
        4L to 14L,
        6L to 2L,
        8L to 4L,
        10L to 2L,
        12L to 3L,
        20L to 1L,
        36L to 1L,
        38L to 1L,
        40L to 1L,
        64L to 1L,
    )
    for ((inputTime, expected) in testData1) {
        profiledCheck(expected, "Part 1 test (saving $inputTime ps)") {
            val day = Day20.fromInput(testInput)
            day.solve(2) { it == inputTime }
        }
    }

    val testData2 = mapOf(
        50L to 32L,
        52L to 31L,
        54L to 29L,
        56L to 39L,
        58L to 25L,
        60L to 23L,
        62L to 20L,
        64L to 19L,
        66L to 12L,
        68L to 14L,
        70L to 12L,
        72L to 22L,
        74L to 4L,
        76L to 3L,
    )
    for ((inputTime, expected) in testData2) {
        profiledCheck(expected, "Part 2 test (saving $inputTime ps)") {
            val day = Day20.fromInput(testInput)
            day.solve(20) { it == inputTime }
        }
    }

    // Read the input from the `src/Day20.txt` file.
    val input = readInput("Day20")
    profiledExecute("Part 1") {
        val day = Day20.fromInput(input)
        day.solve(2) { it >= 100 }
    }.println()
    profiledExecute("Part 2") {
        val day = Day20.fromInput(input)
        day.solve(20) { it >= 100 }
    }.println()
}
