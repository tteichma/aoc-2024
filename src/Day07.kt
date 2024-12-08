private class Day07(val equations: List<Pair<Long, List<Long>>>) {
    companion object {
        fun fromInput(input: List<String>): Day07 {
            val equations = input.map { line ->
                val splitLine = line.split(":")
                Pair(splitLine[0].toLong(), getLongListFromString(splitLine[1]))
            }
            return Day07(equations)
        }

        fun yieldEquationResults(rhs: List<Long>, includeConcat: Boolean): Sequence<Long> = sequence {
            if (rhs.size == 1) {
                yield(rhs.first())
                return@sequence
            }

            for (result in yieldEquationResults(rhs.dropLast(1), includeConcat)) {
                yield(result + rhs.last())
                yield(result * rhs.last())
                if (includeConcat) {
                    yield("${result}${rhs.last()}".toLong())
                }
            }
        }
    }

    fun solve(includeConcat: Boolean): Long {
        val solvableEquations = equations.filter { (lhs, rhs) ->
            yieldEquationResults(rhs, includeConcat).any { it == lhs }
        }
        return solvableEquations.sumOf { it.first }
    }
}

fun main() {
    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test")
    profiledCheck(3749L, "Part 1 test") {
        val day = Day07.fromInput(testInput)
        day.solve(false)
    }
    profiledCheck(11387L, "Part 2 test") {
        val day = Day07.fromInput(testInput)
        day.solve(true)
    }

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    profiledExecute("Part 1") {
        val day = Day07.fromInput(input)
        day.solve(false)
    }.println()
    profiledExecute("Part 2") {
        val day = Day07.fromInput(input)
        day.solve(true)
    }.println()
}
