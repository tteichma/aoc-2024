import java.util.PriorityQueue

private class Day16(data: List<List<Boolean>>, val start: Pair<IntCoordinate, Direction>, val end: IntCoordinate) :
    DataMap<Boolean>(data) {
    // True means empty (movable to).
    companion object {
        fun fromInput(input: List<String>): Day16 {
            var start: Pair<IntCoordinate, Direction>? = null
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
                                        start = Pair(Pair(indLine, it.index), Direction.LR)
                                        true
                                    }

                                    'E' -> {
                                        end = Pair(indLine, it.index)
                                        true
                                    }

                                    else -> TODO("Need to implement handling of character $it")
                                }
                            }
                    }

            return Day16(data, start!!, end!!)
        }
    }

    fun getBestPathsAndCosts(): Pair<List<Set<IntCoordinate>>, Long> {
        var bestCost = Long.MAX_VALUE
        val bestPaths = mutableListOf<Set<IntCoordinate>>()

        val bestCostToCoordinateDirection = mutableMapOf<CoordinateDirection, Long>()
        val upcoming = PriorityQueue(
            16,
            Comparator<Triple<CoordinateDirection, Long, Set<IntCoordinate>>> { a, b ->
                return@Comparator a.second.compareTo(b.second)
            })
        upcoming.add(Triple(start, 0L, setOf()))

        while (upcoming.isNotEmpty()) {
            val (coordinateDirection, cost, visited) = upcoming.remove()
            if (cost > bestCost) break

            if (cost > bestCostToCoordinateDirection.getOrPut(coordinateDirection) { cost }) continue

            if (coordinateDirection.first == end) {
                bestCost = cost
                bestPaths.add(visited + setOf(end))
                continue
            }

            val (coordinate, direction) = coordinateDirection
            val newVisited = visited + setOf(coordinate)

            if (data[coordinate + direction]) upcoming.add(
                Triple(
                    Pair(coordinate + direction, direction),
                    cost + 1L,
                    newVisited
                )
            )

            if (coordinate !in visited) {
                upcoming.add(Triple(Pair(coordinate, direction.right), cost + 1000L, newVisited))
                upcoming.add(Triple(Pair(coordinate, direction.right.opposite), cost + 1000L, newVisited))
            }
        }

        return Pair(bestPaths, bestCost)
    }

    fun solvePart1(): Long {
        return getBestPathsAndCosts().second
    }

    fun solvePart2(): Long {
        return getBestPathsAndCosts()
            .first
            .reduce { acc, coordinates -> acc + coordinates }
            .size
            .toLong()
    }
}

fun main() {
    val testInput1 = readInput("Day16_test1")
    profiledCheck(7036L, "Part 1 test 1") {
        val day = Day16.fromInput(testInput1)
        day.solvePart1()
    }
    val testInput2 = readInput("Day16_test2")
    profiledCheck(11048L, "Part 1 test 2") {
        val day = Day16.fromInput(testInput2)
        day.solvePart1()
    }
    val input = readInput("Day16")
    profiledExecute("Part 1") {
        val day = Day16.fromInput(input)
        day.solvePart1()
    }.println()

    profiledCheck(45L, "Part 2 test 1") {
        val day = Day16.fromInput(testInput1)
        day.solvePart2()
    }
    profiledCheck(64L, "Part 2 test 2") {
        val day = Day16.fromInput(testInput2)
        day.solvePart2()
    }
    profiledExecute("Part 2") {
        val day = Day16.fromInput(input)
        day.solvePart2()
    }.println()
}
