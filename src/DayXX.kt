private class DayXX {
    companion object {
        fun fromInput(input: List<String>): DayXX {
            return DayXX()
        }
    }

    fun solvePart1(): Long {
        return 0L
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("DayXX_test")
    profiledCheck(0L, "Part 1 test") {
        val day = DayXX.fromInput(testInput)
        day.solvePart1()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("DayXX")
    profiledExecute("Part 1") {
        val day = DayXX.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = DayXX.fromInput(input)
        day.solvePart2()
    }.println()
}
