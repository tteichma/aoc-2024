private class Day08(data: List<List<Char>>) : DataMap<Char>(data) {
    val uniqueChars = data.flatten().filterNot { it == '.' }.toSet()

    companion object {
        fun fromInput(input: List<String>): Day08 {
            return Day08(input.map { it.toCharArray().toList() })
        }
    }

    fun solvePart1(): Long {
        val antiNodeCoordinates = mutableSetOf<IntCoordinate>()
        for (c in uniqueChars) {
            val charCoordinates = getCoordinatesWithValue(c).toList()

            for (coord1 in charCoordinates) {
                for (coord2 in charCoordinates) {
                    if (coord1 == coord2) continue

                    val delta = coord2 - coord1
                    antiNodeCoordinates.add(coord1 - delta)
                    antiNodeCoordinates.add(coord2 + delta)
                }
            }
        }
        return antiNodeCoordinates
            .count { it.isWithinBoundaries() }
            .toLong()
    }

    fun solvePart2(): Long {
        val antiNodeCoordinates = mutableSetOf<IntCoordinate>()
        for (c in uniqueChars) {
            val charCoordinates = getCoordinatesWithValue(c).toList()

            for (coord1 in charCoordinates) {
                for (coord2 in charCoordinates) {
                    if (coord1 == coord2) continue

                    val delta = coord2 - coord1

                    var nextCoordinate = coord1
                    while (nextCoordinate.isWithinBoundaries()) {
                        antiNodeCoordinates.add(nextCoordinate)
                        nextCoordinate -= delta
                    }
                    nextCoordinate = coord2
                    while (nextCoordinate.isWithinBoundaries()) {
                        antiNodeCoordinates.add(nextCoordinate)
                        nextCoordinate += delta
                    }
                }
            }
        }
        return antiNodeCoordinates
            .size
            .toLong()
    }
}

fun main() {
    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    profiledCheck(14L, "Part 1 test") {
        val day = Day08.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(34L, "Part 2 test") {
        val day = Day08.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    profiledExecute("Part 1") {
        val day = Day08.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day08.fromInput(input)
        day.solvePart2()
    }.println()
}
