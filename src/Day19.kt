private class Day19(val towels: Set<String>, val designs: List<String>) {
    companion object {
        fun fromInput(input: List<String>): Day19 {
            val towels = input.first().split(", ").toSet()
            val designs = input.drop(2)

            return Day19(towels, designs)
        }
    }

    fun yieldPossibleTowelCombinations(
        designSuffix: String,
        towelsSoFar: List<String>,
        remainingTowels: Set<String>
    ): Iterator<List<String>> = iterator {
        for (towel in remainingTowels) {
            if (designSuffix == towel) {
                yield(towelsSoFar + towel)
            } else if (designSuffix.take(towel.length) == towel) {
                yieldPossibleTowelCombinations(
                    designSuffix.drop(towel.length),
                    towelsSoFar + towel,
                    remainingTowels
                )
                    .forEach { yield(towelsSoFar + it) }
            }
        }
    }

    fun solvePart1(): Int {
        return designs.count { yieldPossibleTowelCombinations(it, listOf(), towels).hasNext() }
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day19_test.txt` file:
    val testInput = readInput("Day19_test")
    profiledCheck(6, "Part 1 test") {
        val day = Day19.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
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
