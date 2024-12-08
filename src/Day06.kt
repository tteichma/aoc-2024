private class Day06(data: List<List<Boolean>>, val start: Pair<IntCoordinate, Direction>) : DataMap<Boolean>(data) {
    // True means empty (movable to).

    companion object {
        fun fromInput(input: List<String>): Day06 {
            var start: Pair<IntCoordinate, Direction>? = null
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
                                    '^' -> {
                                        start = Pair(Pair(indLine, it.index), Direction.DU)
                                        true
                                    }

                                    else -> TODO("Need to implement handling of character $it")
                                }
                            }
                    }

            return Day06(data, start!!)
        }
    }

    fun solvePart1(): Long {
        var current = start
        val visited = mutableSetOf<Pair<IntCoordinate, Direction>>()

        while (current !in visited) {
            visited.add(current)
            val next = Pair(current.first + current.second, current.second)
            if (!next.first.isWithinBoundaries()) break

            current = if (data[next.first]) {
                next
            } else {
                Pair(current.first, current.second.right)
            }
        }
        return visited
            .map { it.first }
            .toSet()
            .size
            .toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")
    profiledCheck(41L, "Part 1 test") {
        val day = Day06.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day06.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    profiledExecute("Part 1") {
        val day = Day06.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day06.fromInput(input)
        day.solvePart2()
    }.println()
}
