private class Day02(val measurements: List<List<Int>>) {
    companion object {
        fun fromInput(input: List<String>): Day02 {
            return Day02(input.map { getIntsFromString(it).toList() })
        }
    }

    fun getNumSafeReports(ignoreSingleValue: Boolean): Long {
        val allMeasurementVariations = measurements.map { measurement ->
            listOf(measurement) + if (ignoreSingleValue) {
                measurement.indices.map {
                    val filteredMeasurement = measurement.toMutableList()
                    filteredMeasurement.removeAt(it)
                    filteredMeasurement.toList()
                }
            } else listOf()
        }
        return allMeasurementVariations.count { measurementsVariations ->
            val isSafeIncreasing = measurementsVariations.any { m ->
                m.windowed(2).all {
                    val diff = it[1] - it[0]
                    diff in 1..3
                }
            }
            val isSafeDecreasing = measurementsVariations.any { m ->
                m.windowed(2).all {
                    val diff = it[1] - it[0]
                    diff in -3..-1
                }
            }
            isSafeIncreasing || isSafeDecreasing
        }.toLong()
    }

    fun solvePart1(): Long {
        return getNumSafeReports(false)
    }

    fun solvePart2(): Long {
        return getNumSafeReports(true)
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    profiledCheck(2L, "Part 1 test") {
        val day = Day02.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(4L, "Part 2 test") {
        val day = Day02.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    profiledExecute("Part 1") {
        val day = Day02.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day02.fromInput(input)
        day.solvePart2()
    }.println()
}
