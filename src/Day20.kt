import java.util.PriorityQueue

private class Day20(data: List<List<Boolean>>, val start: IntCoordinate, val end: IntCoordinate) :
    DataMap<Boolean>(data) {
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
    fun solvePart1(savingPredicate: (Long) -> Boolean): Long {

        val distFromStart = getDistancesToCoordinate(start)
        val distsToEnd = getDistancesToCoordinate(end)

        val startToEndWithoutCheats = distsToEnd[start]!!

        val savedTimeCounts = mutableMapOf<Long, Long>()
        for ((coordinate, distStartToCheat) in distFromStart) {
            for (coordinateCheatWall in coordinate.getNeighbours { !data[it] }) {
                for (coordinateCheatPath in coordinateCheatWall.getNeighbours { data[it] && it != coordinate }) {
                    val savedDistance =
                        startToEndWithoutCheats - (distStartToCheat + 2 + distsToEnd[coordinateCheatPath]!!)

                    if (savedDistance > 0) {
                        savedTimeCounts[savedDistance] = (savedTimeCounts[savedDistance] ?: 0) + 1
                    }
                }
            }
        }

        return savedTimeCounts.filter { savingPredicate(it.key) }.values.sum()
    }

    fun solvePart2(): Long {
        return 0L
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
            day.solvePart1 { it == inputTime }
        }
    }

    // Read the input from the `src/Day20.txt` file.
    val input = readInput("Day20")
    profiledExecute("Part 1") {
        val day = Day20.fromInput(input)
        day.solvePart1 { it >= 100 }
    }.println()
    profiledExecute("Part 2") {
        val day = Day20.fromInput(input)
        day.solvePart2()
    }.println()
}
