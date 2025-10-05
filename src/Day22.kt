private class Day22(val initialSecrets: List<Long>) {
    fun advanceSecrets(numTimes: Int): List<Long> {
        var secrets = initialSecrets
        secrets.println()
        repeat(numTimes) {
            secrets = secrets
                .map { prune(it.xor(it * 64)) }
                .map { prune(it.xor(it / 32)) }
                .map { prune(it.xor(it * 2048 ))}
        }
        return secrets
    }

    companion object {
        fun prune(x: Long) = x % 16777216

        fun fromInput(input: List<String>): Day22 {
            return Day22(input.map { it.toLong() })
        }
    }

    fun solvePart1(): Long {
        return advanceSecrets(2000).sum()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day22_test.txt` file:
    val testInput = readInput("Day22_test")
    profiledCheck(37327623L, "Part 1 test") {
        val day = Day22.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day22.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day22.txt` file.
    val input = readInput("Day22")
    profiledExecute("Part 1") {
        val day = Day22.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day22.fromInput(input)
        day.solvePart2()
    }.println()
}
