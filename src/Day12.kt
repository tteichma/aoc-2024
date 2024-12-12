private class Day12(data: List<List<Char>>) : DataMap<Char>(data) {
    companion object {
        fun fromInput(input: List<String>): Day12 {
            return Day12(input.map { line -> line.map { it } })
        }
    }

    fun solve(getScore: (Set<IntCoordinate>) -> Int): Long {
        val seenCoordinates = mutableSetOf<IntCoordinate>()

        var totalScore = 0L

        for (currentCoordinate in coordinates) {
            if (currentCoordinate in seenCoordinates) continue

            val connectedCoordinates = floodMap(currentCoordinate) { it == data[currentCoordinate] }
            seenCoordinates.addAll(connectedCoordinates)
            val score = getScore(connectedCoordinates)
            totalScore += score
        }

        return totalScore
    }

    fun solvePart1(): Long {
        return solve { connectedCoordinates: Set<IntCoordinate> ->
            val numFences =
                connectedCoordinates.sumOf { coord -> 4 - coord.getNeighbours { it in connectedCoordinates }.size }
            numFences * connectedCoordinates.size
        }
    }

    fun solvePart2(): Long {
        return solve { connectedCoordinates: Set<IntCoordinate> ->
            val numFences = Direction.entries.sumOf { direction ->
                connectedCoordinates.count {
                    (it + direction !in connectedCoordinates)
                            && ((it + direction.right !in connectedCoordinates)
                            || (it + direction + direction.right in connectedCoordinates))
                }
            }
            (numFences * connectedCoordinates.size)
        }
    }
}

fun main() {
    // Or read a large test input from the `src/Day12_test.txt` file:
    val testInput = readInput("Day12_test")
    profiledCheck(1930L, "Part 1 test") {
        val day = Day12.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(1206L, "Part 2 test") {
        val day = Day12.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day12.txt` file.
    val input = readInput("Day12")
    profiledExecute("Part 1") {
        val day = Day12.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day12.fromInput(input)
        day.solvePart2()
    }.println()
}
