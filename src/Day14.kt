private data class Robot(
    private var x: Long,
    private var y: Long,
    val vx: Int,
    val vy: Int,
    val sizeX: Long,
    val sizeY: Long
) {
    val px
        get() = x
    val py
        get() = y

    fun move(times: Long) {
        val correctedVX = if (vx >= 0) vx.toLong() else sizeX + vx
        val correctedVY = if (vy >= 0) vy.toLong() else sizeY + vy

        x = (x + times * correctedVX) % sizeX
        y = (y + times * correctedVY) % sizeY
    }
}

private class Day14(val sizeX: Long, val sizeY: Long, val robots: List<Robot>) {
    companion object {
        fun fromInput(sizeX: Long, sizeY: Long, input: List<String>): Day14 {
            val robots = input.map {
                val numbers = getIntListFromString(it)
                Robot(numbers[0].toLong(), numbers[1].toLong(), numbers[2], numbers[3], sizeX, sizeY)
            }

            return Day14(sizeX, sizeY, robots)
        }
    }

    fun getRobotsCounts() = robots.groupBy { Pair(it.px, it.py) }.mapValues { it.value.size }
    fun printRobots() {
        val robotsCounts = robots.groupBy { Pair(it.px, it.py) }.mapValues { it.value.size }

        (0..<sizeY).joinToString("\n") { iRow ->
            (0..<sizeX).joinToString("") { iCol ->
                robotsCounts.getOrDefault(Pair(iCol, iRow), 0)
                    .let { if (it == 0) "." else it.digitToChar() }
                    .toString()
            }
        }.println()
    }


    fun solvePart1(): Long {
        for (robot in robots) {
            robot.move(100)
        }

        val robotsPerQuadrant = robots.groupBy {
            val borderX = sizeX / 2
            val borderY = sizeY / 2

            when {
                (it.px < borderX) && (it.py < borderY) -> 0
                (it.px > borderX) && (it.py < borderY) -> 1
                (it.px < borderX) && (it.py > borderY) -> 2
                (it.px > borderX) && (it.py > borderY) -> 3
                else -> null
            }
        }
            .mapNotNull { if (it.key != null) it.value.size.toLong() else null }

        if (robotsPerQuadrant.size < 4) return 0  // At least one empty sector.

        return robotsPerQuadrant.reduce { acc, i -> acc * i }
    }

    fun solvePart2(): Long {
        var highestNumMostVertical = 0
        for (iteration in 1..100_000) {
            robots.forEach { it.move(1) }
            val robotCounts = getRobotsCounts()
            val numMostVertical = robotCounts
                .keys
                .groupBy { it.first }
                .maxOf { it.value.size }

            if (numMostVertical <= highestNumMostVertical) {
                continue
            } else {
                highestNumMostVertical = numMostVertical
            }

            printRobots()
            println("Iteration $iteration")
            println("")
        }
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day14_test.txt` file:
    val testInput = readInput("Day14_test")
    profiledCheck(12L, "Part 1 test") {
        val day = Day14.fromInput(11, 7, testInput)
        day.solvePart1()
    }

    // Read the input from the `src/Day14.txt` file.
    val input = readInput("Day14")
    profiledExecute("Part 1") {
        val day = Day14.fromInput(101, 103, input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day14.fromInput(101, 103, input)
        day.solvePart2()
    }.println()
}
