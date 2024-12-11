private class Day11(val initial: List<Long>) {
    val cache = mutableMapOf<Pair<Long, Int>, List<Long>>()  // (number, timesRemaining)

    companion object {
        fun fromInput(input: List<String>): Day11 {
            return Day11(getLongListFromString(input.first()))
        }
    }

    fun evolveNumber(n: Long, times: Int = 1): List<Long> = cache.getOrPut(Pair(n, times)) {
        if (times > 1) {
            return@getOrPut evolveNumber(n, times - 1).flatMap { evolveNumber(it) }
        }

        return@getOrPut when {
            n == 0L -> listOf(1L)
            n.toString().length % 2 == 0 -> {
                val s = n.toString()
                listOf(s.dropLast(s.length / 2).toLong(), s.drop(s.length / 2).toLong())
            }

            else -> listOf(2024 * n)
        }
    }

    fun solve(times: Int): Long {
        val endList = initial.flatMap { evolveNumber(it, times) }
        return endList.size.toLong()
    }
}

fun main() {
    // Or read a large test input from the `src/Day11_test.txt` file:
    val testInput = readInput("Day11_test")

    profiledCheck(55312L, "Part 1 test") {
        val day = Day11.fromInput(testInput)
        day.solve(25)
    }

    // Read the input from the `src/Day11.txt` file.
    val input = readInput("Day11")
    profiledExecute("Part 1") {
        val day = Day11.fromInput(input)
        day.solve(25)
    }.println()
    profiledExecute("Part 2") {
        val day = Day11.fromInput(input)
        day.solve(75)
    }.println()
}
