private class Day03(val mulPairs: List<Pair<Int, Int>>) {
    companion object {
        val MUL_REGEX = Regex("""mul\(\d{1,3},\d{1,3}\)""")

        fun fromInput(input: List<String>): Day03 {
            val mulMatches = input.flatMap { MUL_REGEX.findAll(it)  }
            val mulPairs = mulMatches.map {
                val numbers = getIntsFromString(it.value).toList()
                Pair(numbers[0], numbers[1])
            }.toList()

            return Day03(mulPairs)
        }
    }

    fun solvePart1(): Long {
        return mulPairs.sumOf { (it.first * it.second ).toLong()}
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    profiledCheck(161L, "Part 1 test") {
        val day = Day03.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day03.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    profiledExecute("Part 1") {
        val day = Day03.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day03.fromInput(input)
        day.solvePart2()
    }.println()
}
