private data class Game(val xa: Int, val ya: Int, val xb: Int, val yb: Int, val xp: Int, val yp: Int) {
    init {
        check(xa > 0)
        check(ya > 0)
        check(xb > 0)
        check(xb > 0)
    }

    fun getOptimalCost(): Int? {
        var a = xp / xa + 1 // slightly above bounds
        var b = 0

        val validSolutions = mutableListOf<Pair<Int, Int>>()
        do {
            val x = a * xa + b * xb
            val y = a * ya + b * yb

            if (x == xp && y == yp) {
                validSolutions.add(Pair(a, b))
                --a
            } else if (x > xp || y > yp) {
                --a
            } else {
                ++b
            }
        } while (a >= 0)

        return validSolutions.maxOfOrNull { 3 * it.first + 1 * it.second }
    }
}

private fun getTwoIntsFromLine(line: String): Pair<Int, Int> {
    val matches = getIntListFromString(line)
    return Pair(matches[0], matches[1])
}

private class Day13(val games: List<Game>) {
    companion object {
        fun fromInput(input: List<String>): Day13 {
            val gameInputs = input.chunked(4)

            val games = gameInputs.map {
                val (xa, y1) = getTwoIntsFromLine(it[0])
                val (x2, y2) = getTwoIntsFromLine(it[1])
                val (xp, yp) = getTwoIntsFromLine(it[2])
                Game(xa, y1, x2, y2, xp, yp)
            }

            return Day13(games)
        }
    }

    fun solvePart1(): Long {
        return games.withIndex().mapNotNull { it.value.getOptimalCost() }.sum()
            .toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day13_test.txt` file:
    val testInput = readInput("Day13_test")
    profiledCheck(480L, "Part a test") {
        val day = Day13.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day13.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day13.txt` file.
    val input = readInput("Day13")
    profiledExecute("Part 1") {
        val day = Day13.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day13.fromInput(input)
        day.solvePart2()
    }.println()
}
