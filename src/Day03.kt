private sealed class Instruction {
    data class Mult(val x: Int, val y: Int) : Instruction() {
        fun product() = x * y
    }

    data object Do : Instruction()
    data object Dont : Instruction()

    companion object {
        private val REGEX_MULT = Regex("""mul\(\d{1,3},\d{1,3}\)""")
        private val REGEX_DO = Regex("""do\(\)""")
        private val REGEX_DONT = Regex("""don't\(\)""")
        private val REGEX_COMBINED = Regex("${REGEX_MULT.pattern}|${REGEX_DO.pattern}|${REGEX_DONT.pattern}")

        fun parseLine(line: String): List<Instruction> {
            return REGEX_COMBINED.findAll(line).map { it.value }.map {
                when {
                    REGEX_MULT.matches(it) -> {
                        val values = getIntListFromString(it)
                        Mult(values[0], values[1])
                    }

                    REGEX_DO.matches(it) -> Do
                    REGEX_DONT.matches(it) -> Dont
                    else -> throw RuntimeException("Unable to parse $it")
                }
            }.toList()
        }
    }
}


private class Day03(val instructions: List<Instruction>) {
    companion object {
        fun fromInput(input: List<String>): Day03 {
            val instructions = input.flatMap { Instruction.parseLine(it) }

            return Day03(instructions)
        }
    }

    fun solvePart1(): Long {
        return instructions.sumOf {
                if (it is Instruction.Mult) it.product() else 0
        }.toLong()
    }

    fun solvePart2(): Long {
        var isEnabled = true
        var result = 0L
        for (instruction in instructions) {
            when (instruction) {
                is Instruction.Mult -> if (isEnabled) {
                    result += instruction.product()
                }

                is Instruction.Do -> isEnabled = true
                is Instruction.Dont -> isEnabled = false
            }
        }
        return result
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    profiledCheck(161L, "Part 1 test") {
        val day = Day03.fromInput(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"))
        day.solvePart1()
    }
    profiledCheck(48L, "Part 2 test") {
        val day = Day03.fromInput(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"))
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
