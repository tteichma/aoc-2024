private class Day11(val initial: List<Long>) {
    val stoneNumberCache = mutableMapOf<Pair<Long, Int>, Long>()  // (number, timesRemaining)

    companion object {
        fun fromInput(input: List<String>): Day11 {
            return Day11(getLongListFromString(input.first()))
        }
    }

    fun getEvolvedStoneNumber(n: Long, times: Int = 1): Long = stoneNumberCache.getOrPut(Pair(n, times)) {
        if (times == 0) {
            return@getOrPut 1
        }

        val nextTimes = times - 1
        return@getOrPut when {
            n == 0L -> getEvolvedStoneNumber(1L, nextTimes)
            n.toString().length % 2 == 0 -> {
                val s = n.toString()
                val nextNumbers = Pair(s.dropLast(s.length / 2).toLong(), s.drop(s.length / 2).toLong())
                getEvolvedStoneNumber(nextNumbers.first, nextTimes) + getEvolvedStoneNumber(
                    nextNumbers.second,
                    nextTimes
                )
            }

            else -> getEvolvedStoneNumber(2024 * n, nextTimes)
        }
    }

    fun solve(times: Int): Long {
        return initial.sumOf { getEvolvedStoneNumber(it, times) }
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
