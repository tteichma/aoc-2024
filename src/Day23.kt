private class Day23(val nodesToNeighbors: Map<String, Set<String>>) {
    fun getInterConnectionsOfSize(size: Int): Set<Set<String>> {
        if (size == 1) return nodesToNeighbors.keys.map { setOf(it) }.toSet()

        val smallerInterConnections = getInterConnectionsOfSize(size - 1)
        return smallerInterConnections
            .flatMap { interConnection ->
                nodesToNeighbors.getOrDefault(interConnection.first(), setOf())
                    .filter { candidate ->
                        interConnection.all { candidate in nodesToNeighbors.getOrDefault(it, setOf()) }
                    }
                    .map { interConnection + it }
            }
            .toSet()
    }

    companion object {
        fun fromInput(input: List<String>): Day23 {
            val data = mutableMapOf<String, MutableSet<String>>()
            for (line in input) {
                val (a, b) = line.split("-")
                data.getOrPut(a) { mutableSetOf() }.add(b)
                data.getOrPut(b) { mutableSetOf() }.add(a)
            }
            return Day23(data)
        }
    }

    fun solvePart1(): Long {
        val interConnections = getInterConnectionsOfSize(3)
        return interConnections
            .count { interConnection -> interConnection.any { it.startsWith("t") } }
            .toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day23_test.txt` file:
    val testInput = readInput("Day23_test")
    profiledCheck(7L, "Part 1 test") {
        val day = Day23.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
        val day = Day23.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day23.txt` file.
    val input = readInput("Day23")
    profiledExecute("Part 1") {
        val day = Day23.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day23.fromInput(input)
        day.solvePart2()
    }.println()
}
