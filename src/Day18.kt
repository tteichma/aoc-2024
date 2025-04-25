import java.util.PriorityQueue

private class Day18(
    dataMap: List<List<Int>>,  // Value is last timestep when for which it is free
    val start: IntCoordinate,
    val end: IntCoordinate
) : DataMap<Int>(dataMap) {

    companion object {
        fun fromInput(input: List<String>): Day18 {
            val coordinatesAndTime =
                input.mapIndexed { index, s -> s.split(",").let { Triple(it[0].toInt(), it[1].toInt(), index) } }
            val xSize = coordinatesAndTime.maxOf { it.first } + 1
            val ySize = coordinatesAndTime.maxOf { it.second } + 1
            val mapData = List(ySize) { MutableList(xSize) { Int.MAX_VALUE } }
            for ((x, y, t) in coordinatesAndTime) mapData[y][x] = t

            return Day18(mapData, IntCoordinate(0, 0), IntCoordinate(xSize - 1, ySize - 1))
        }
    }

    fun getTimeToExit(timePassed: Int): Int? {
        val seen = mutableSetOf<IntCoordinate>()
        val upcoming = PriorityQueue(
            16,
            Comparator<Pair<IntCoordinate, Int>> { a, b ->
                return@Comparator a.second.compareTo(b.second)
            })
        upcoming.add(Pair(start, 0))

        while (upcoming.isNotEmpty()) {
            val (coordinate, time) = upcoming.remove()

            if (coordinate in seen) continue
            if (get(coordinate) < timePassed) continue
            if (coordinate == end) return time

            seen.add(coordinate)
            coordinate.getNeighbours { get(it) > time }.forEach { upcoming.add(Pair(it, time+1)) }
        }

        return null
    }

    fun solvePart1(timePassed: Int): Int {
        return getTimeToExit(timePassed) ?: TODO("Did not find exit")
    }

    fun solvePart2(): String {
        val maxTime = rowSize * colSize - getCoordinatesWithValue(Int.MAX_VALUE).count()
        for (time in 0..maxTime) {
            val timeToExit = getTimeToExit(time)
            if (timeToExit == null) {
                val blockingCoordinate =  getCoordinatesWithValue(time-1).first()
                return listOf(blockingCoordinate.second, blockingCoordinate.first).joinToString(",")
            }
        }
        TODO("Did not find blocking coordinate")
    }
}

fun main() {
    // Or read a large test input from the `src/Day18_test.txt` file:
    val testInput = readInput("Day18_test")
    profiledCheck(22, "Part 1 test") {
        val day = Day18.fromInput(testInput)
        day.solvePart1(12)
    }
    profiledCheck("6,1", "Part 2 test") {
        val day = Day18.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day18.txt` file.
    val input = readInput("Day18")
    profiledExecute("Part 1") {
        val day = Day18.fromInput(input)
        day.solvePart1(1024)
    }.println()
    profiledExecute("Part 2") {
        val day = Day18.fromInput(input)
        day.solvePart2()
    }.println()
}
