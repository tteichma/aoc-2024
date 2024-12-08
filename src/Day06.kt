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

    fun walkGuard(): Pair<Int, Boolean> {
        var current = start
        var leftArea = false
        val visited = mutableSetOf<Pair<IntCoordinate, Direction>>()

        while (current !in visited) {
            visited.add(current)
            val next = Pair(current.first + current.second, current.second)
            if (!next.first.isWithinBoundaries()) {
                leftArea = true
                break
            }

            current = if (data[next.first]) {
                next
            } else {
                Pair(current.first, current.second.right)
            }
        }
        return Pair(
            visited
                .map { it.first }
                .toSet()
                .size,
            leftArea)
    }

    fun solvePart1(): Long {
        return walkGuard().first.toLong()
    }

    fun solvePart2(): Long {
        var successfulBlocks = 0L
        for (iRow in rowIndices) {
            for (iCol in colIndices) {
                val modifiedCoordinate = Pair(iRow, iCol)
                if (data[modifiedCoordinate] && modifiedCoordinate != start.first) {
                    val modifiedDay = Day06(copyDataWithModification(modifiedCoordinate, false), start)
                    if (!modifiedDay.walkGuard().second) {
                        successfulBlocks += 1
                    }
                }
            }
        }
        return successfulBlocks
    }
}

fun main() {
    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")
    profiledCheck(41L, "Part 1 test") {
        val day = Day06.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(6L, "Part 2 test") {
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
