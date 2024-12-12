private class Day12(data: List<List<Char>>) : DataMap<Char>(data) {
    companion object {
        fun fromInput(input: List<String>): Day12 {
            return Day12(input.map { line -> line.map { it } })
        }
    }

    fun solvePart1(): Long {
        val seenCoordinates = mutableSetOf<IntCoordinate>()

        var totalScore = 0L

        for (currentCoordinate in coordinates) {
            if (currentCoordinate in seenCoordinates) continue

            val connectedCoordinates = floodMap(currentCoordinate) { it == data[currentCoordinate] }
            seenCoordinates.addAll(connectedCoordinates)

            val numFences = connectedCoordinates
                .sumOf { 4 - it.getNeighbours { it in connectedCoordinates }.size }

            totalScore += numFences * connectedCoordinates.size
        }

        return totalScore
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day12_test.txt` file:
    val testInput = readInput("Day12_test")
    profiledCheck(1930L, "Part 1 test") {
        val day = Day12.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
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
