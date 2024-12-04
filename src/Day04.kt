private class Day04(val input: List<String>) {
    companion object {
        fun fromInput(input: List<String>): Day04 {
            return Day04(input)
        }
    }

    fun solvePart1(): Long {
        val indices = input.indices
        check(input.all { it.indices == indices })
        val stringsInDirections = listOf(
            input,  // Horizontal
            indices.map { indCol ->
                indices.map { indRow -> input[indRow][indCol] }.joinToString("")
            },  // vertical
            (-indices.last..indices.last).map { offset ->
                indices.mapNotNull { indRow ->
                    input[indRow].getOrNull(indRow + offset)
                }.joinToString("")
            },  // diagonal forward
            (-indices.last..indices.last).map { offset ->
                indices.mapNotNull { indRow ->
                    input[indRow].getOrNull(input.lastIndex - indRow + offset)
                }.joinToString("")
            },  // diagonal backward
        )
        val strings = stringsInDirections
            .flatMap { l -> listOf(l, l.map { it.reversed() }).flatten() }  // Original and reversed
        val numMatches = strings.sumOf { Regex("XMAS").findAll(it).count() }
        return numMatches.toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    profiledCheck(18L, "Part 1 test") {
        val day = Day04.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day04.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    profiledExecute("Part 1") {
        val day = Day04.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day04.fromInput(input)
        day.solvePart2()
    }.println()
}
