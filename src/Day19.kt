private class Day19(val towels: Set<String>, val designs: List<String>) {
    val knownSuffixCounts = mutableMapOf<String, Long>()

    companion object {
        fun fromInput(input: List<String>): Day19 {
            val towels = input.first().split(", ").toSet()
            val designs = input.drop(2)

            return Day19(towels, designs)
        }
    }

    fun getNumPossibleTowelCombinationsForSuffix(
        designSuffix: String,
    ): Long {
        knownSuffixCounts[designSuffix]?.let { return it }

        var numMatching = 0L
        for (towel in towels) {
            if (designSuffix == towel) {
                ++numMatching
            } else if (designSuffix.take(towel.length) == towel) {
                numMatching += getNumPossibleTowelCombinationsForSuffix(
                    designSuffix.drop(towel.length),
                )
            }
        }
        knownSuffixCounts[designSuffix] = numMatching
        return numMatching
    }

    fun solvePart1(): Int {
        return designs.count { getNumPossibleTowelCombinationsForSuffix(it)  > 0}
    }

    fun solvePart2(): Long {
        return designs.sumOf { getNumPossibleTowelCombinationsForSuffix(it) }
    }
}

fun main() {
    // Or read a large test input from the `src/Day19_test.txt` file:
    val testInput = readInput("Day19_test")
    profiledCheck(6, "Part 1 test") {
        val day = Day19.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(16, "Part 2 test") {
        val day = Day19.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day19.txt` file.
    val input = readInput("Day19")
    profiledExecute("Part 1") {
        val day = Day19.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day19.fromInput(input)
        day.solvePart2()
    }.println()
}
