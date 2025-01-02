private enum class Cell { EMPTY, WALL, BOX, LEFT_BOX, RIGHT_BOX }


private class Day15(var dataMap: DataMap<Cell>, var robotPosition: IntCoordinate, val instructions: List<Direction>) {
    companion object {
        fun fromInput(input: List<String>, doubleWidth: Boolean): Day15 {
            var robotPosition: IntCoordinate? = null

            val data = input.takeWhile { it != "" }.withIndex().map { line ->
                line.value.withIndex().flatMap {
                    when (it.value) {
                        '#' -> if (doubleWidth) listOf(Cell.WALL, Cell.WALL) else listOf(Cell.WALL)
                        'O' -> if (doubleWidth) listOf(Cell.LEFT_BOX, Cell.RIGHT_BOX) else listOf(Cell.BOX)
                        '.' -> if (doubleWidth) listOf(Cell.EMPTY, Cell.EMPTY) else listOf(Cell.EMPTY)
                        '@' -> {
                            robotPosition =
                                if (doubleWidth) Pair(line.index, 2 * it.index) else Pair(line.index, it.index)
                            if (doubleWidth) listOf(Cell.EMPTY, Cell.EMPTY) else listOf(Cell.EMPTY)
                        }

                        else -> throw RuntimeException("Should not happen")
                    }
                }
            }

            val instructions = input
                .dropWhile { it != "" }
                .drop(1)
                .joinToString("")
                .map { Direction.fromChar(it) }

            return Day15(DataMap(data), robotPosition!!, instructions)
        }

    }

    fun printMap() {
        dataMap.rowIndices.map { iRow ->
            dataMap.data[iRow].withIndex().joinToString("") {
                when (it.value) {
                    Cell.BOX -> "O"
                    Cell.WALL -> "#"
                    Cell.LEFT_BOX -> "["
                    Cell.RIGHT_BOX -> "]"
                    Cell.EMPTY -> if (Pair(iRow, it.index) == robotPosition) "@" else "."
                }
            }.println()
        }
    }

    fun getMoveCoordinatesIncludingDuplicates(
        coordinate: IntCoordinate,
        direction: Direction,
    ): List<IntCoordinate>? {
        // Returns whether move is
        val nextCoordinate = coordinate + direction
        return when (dataMap.data[nextCoordinate]) {
            Cell.WALL -> {
                null
            }

            Cell.EMPTY -> {
               listOf(coordinate)
            }

            Cell.BOX -> {
                val prerequisiteMoves = getMoveCoordinatesIncludingDuplicates(nextCoordinate, direction)
                if (prerequisiteMoves != null) prerequisiteMoves + listOf(coordinate) else null
            }

            Cell.LEFT_BOX -> {
                if (direction is Direction.LR) { // Handle in right box
                    val prerequisiteMoves = getMoveCoordinatesIncludingDuplicates(nextCoordinate+direction, direction)
                    if (prerequisiteMoves != null) prerequisiteMoves + listOf(nextCoordinate, coordinate) else null
                } else if (direction is Direction.RL) {
                    throw RuntimeException("This should never happen.")
                } else {
                    val prerequisiteMoves1 = getMoveCoordinatesIncludingDuplicates(nextCoordinate, direction)
                    val prerequisiteMoves2 = getMoveCoordinatesIncludingDuplicates(nextCoordinate+Direction.LR, direction)
                    if (prerequisiteMoves1 != null && prerequisiteMoves2!=null) prerequisiteMoves1 + prerequisiteMoves2 + listOf(coordinate) else null
                }
            }

            Cell.RIGHT_BOX -> {
                if (direction is Direction.RL) { // Handle in right box
                    val prerequisiteMoves = getMoveCoordinatesIncludingDuplicates(nextCoordinate+direction, direction)
                    if (prerequisiteMoves != null) prerequisiteMoves + listOf(nextCoordinate, coordinate) else null
                } else if (direction is Direction.LR) {
                    throw RuntimeException("This should never happen.")
                } else {
                    val prerequisiteMoves1 = getMoveCoordinatesIncludingDuplicates(nextCoordinate, direction)
                    val prerequisiteMoves2 = getMoveCoordinatesIncludingDuplicates(nextCoordinate+Direction.RL, direction)
                    if (prerequisiteMoves1 != null && prerequisiteMoves2!=null) prerequisiteMoves1 + prerequisiteMoves2 + listOf(coordinate) else null
                }
            }
        }
    }

    private fun performMove(coordinate: IntCoordinate, direction: Direction) {
        val nextCoordinate = coordinate + direction
        if (robotPosition == coordinate) robotPosition = nextCoordinate

        dataMap = dataMap.getCopyWithModifications(
            mapOf(
                nextCoordinate to dataMap[coordinate],
                coordinate to Cell.EMPTY
            )
        )
    }

    fun solve(): Long {
        for ((ind, direction) in instructions.withIndex()) {
            val alreadyMovedCoordinates = mutableSetOf<IntCoordinate>()
            for (coordinate in getMoveCoordinatesIncludingDuplicates(robotPosition, direction) ?: listOf()){
                if (alreadyMovedCoordinates.add(coordinate)) performMove(coordinate, direction)
            }
        }

        return (dataMap.getCoordinatesWithValue(Cell.BOX) + dataMap.getCoordinatesWithValue(Cell.LEFT_BOX))
            .sumOf { 100L * it.first + it.second }
    }
}

fun main() {
    // Or read a large test input from the `src/Day15_test[12].txt` files:
    val input = readInput("Day15")
    val testInput1 = readInput("Day15_test1")
    val testInput2 = readInput("Day15_test2")
    profiledCheck(2028L, "Part 1 test 1") {
        val day = Day15.fromInput(testInput1, false)
        day.solve()
    }
    profiledCheck(10092L, "Part 1 test 2") {
        val day = Day15.fromInput(testInput2, false)
        day.solve()
    }
    profiledExecute("Part 1") {
        val day = Day15.fromInput(input, false)
        day.solve()
    }.println()

    profiledCheck(9021L, "Part 2 (test 2)") {
        val day = Day15.fromInput(testInput2, true)
        day.solve()
    }
    profiledExecute("Part 2") {
        val day = Day15.fromInput(input, true)
        day.solve()
    }.println()
}
