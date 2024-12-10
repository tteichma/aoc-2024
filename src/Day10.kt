private class Day10(heightData: List<List<Int>>) : DataMap<Int>(heightData) {
    // Endpoints of trails from a given coordinate.
    private val trailCache = getCoordinatesWithValue(9)
        .associateWith { setOf(it) }
        .toMutableMap()

    companion object {
        fun fromInput(input: List<String>): Day10 {
            val heightData = input.map { line ->
                line.map { it.digitToInt() }
            }
            return Day10(heightData)
        }
    }

    private fun getEndPoints(coordinate: IntCoordinate): Set<IntCoordinate> = trailCache.getOrPut(coordinate) {
        coordinate
            .getNeighbours { it == data[coordinate] + 1 }
            .map { getEndPoints(it) }
            .fold(setOf()) { acc, it -> acc + it }
    }

    fun solvePart1(): Long {
        val trailHeads = getCoordinatesWithValue(0)
        trailHeads.sumOf { getEndPoints(it).size }.toLong()
        return trailHeads.sumOf { getEndPoints(it).size }.toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day10_test.txt` file:
    val testInput = readInput("Day10_test")
    profiledCheck(36L, "Part 1 test") {
        val day = Day10.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day10.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day10.txt` file.
    val input = readInput("Day10")
    profiledExecute("Part 1") {
        val day = Day10.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day10.fromInput(input)
        day.solvePart2()
    }.println()
}
