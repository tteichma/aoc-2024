import kotlin.math.max

private data class Game(val xa: Long, val ya: Long, val xb: Long, val yb: Long, val xp: Long, val yp: Long) {
    init {
        check(xa > 0)
        check(ya > 0)
        check(xb > 0)
        check(xb > 0)
    }

    fun getOptimalCost(): Long? {
        var a = xp / xa + 1L // slightly above bounds
        var b = 0L

        do {
            val x = a * xa + b * xb
            val y = a * ya + b * yb

            if (x == xp && y == yp) {
                return 3 * a + 1 * b
            } else if (x > xp) {
                val offset = (xp - x) / xa
                a += if (offset < 0) offset else -1
            } else if (y > yp) {
                val offset = (yp - y) / ya
                a += if (offset < 0) offset else -1
            } else {
                val offset = max((xp - x) / xb, (yp - y) / yb)
                b += if (offset > 0) offset else 1
            }
        } while (a >= 0)

        return null
    }
}

private fun getTwoLongsFromLine(line: String, offset: Long = 0L): Pair<Long, Long> {
    val matches = getLongListFromString(line)
    return Pair(matches[0] + offset, matches[1] + offset)
}

private class Day13(val games: List<Game>) {
    companion object {
        fun fromInput(input: List<String>, offset: Long = 0L): Day13 {
            val gameInputs = input.chunked(4)

            val games = gameInputs.map {
                val (xa, y1) = getTwoLongsFromLine(it[0])
                val (x2, y2) = getTwoLongsFromLine(it[1])
                val (xp, yp) = getTwoLongsFromLine(it[2], offset)
                Game(xa, y1, x2, y2, xp, yp)
            }

            return Day13(games)
        }
    }

    fun solvePart1(): Long {
        return games.mapNotNull { it.getOptimalCost() }.sum()
    }

    fun solvePart2(): Long {
        return games.mapNotNull { it.getOptimalCost() }.sum()
    }
}

fun main() {
    // Or read a large test input from the `src/Day13_test.txt` file:
    val testInput = readInput("Day13_test")
    profiledCheck(480L, "Part 1 test") {
        val day = Day13.fromInput(testInput)
        day.solvePart1()
    }

    // Read the input from the `src/Day13.txt` file.
    val input = readInput("Day13")
    profiledExecute("Part 1") {
        val day = Day13.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day13.fromInput(input, 10000000000000)
        day.solvePart2()
    }.println()
}
