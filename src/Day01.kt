import kotlin.math.abs

private class Day01(val lists: List<List<Int>>) {
    val listIndices = lists.first().indices

    companion object {
        fun fromInput(input: List<String>): Day01 {
            val allInts = input.map { getIntsFromString(it).toList() }
            val lists = allInts.first().indices
                .map { ind -> allInts.map { it[ind] } }
            return Day01(lists)
        }
    }

    fun solvePart1(): Long {
        val sortedLists = lists.map { it.sorted() }
        return listIndices
            .map { abs(sortedLists[1][it] - sortedLists[0][it]) }
            .sum()
            .toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    profiledCheck(11L, "Part 1 test") {
        val day = Day01.fromInput(testInput)
        day.solvePart1()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    profiledExecute("Part 1") {
        val day = Day01.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day01.fromInput(input)
        day.solvePart2()
    }.println()
}
