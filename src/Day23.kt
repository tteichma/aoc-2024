private class Day23(val nodesToNeighbors: Map<String, Set<String>>) {
    fun getInterConnectionsOfSize(targetSize: Int? = null): Set<Set<String>> {
        var size = 1
        var currentInterConnections = nodesToNeighbors.keys.map { setOf(it) }.toSet()

        while (size != targetSize) {
            val nextInterConnections = currentInterConnections
                .flatMap { interConnection ->
                    nodesToNeighbors.getOrDefault(interConnection.first(), setOf())
                        .filter { candidate ->
                            interConnection.all { candidate in nodesToNeighbors.getOrDefault(it, setOf()) }
                        }
                        .map { interConnection + it }
                }
                .toSet()
            if (nextInterConnections.isEmpty()) {
                return if (targetSize == null) currentInterConnections else setOf()
            }
            currentInterConnections = nextInterConnections
            ++size
        }
        return currentInterConnections
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

    fun solvePart2(): String {
        val interConnections = getInterConnectionsOfSize()
        return interConnections.first().sorted().joinToString(",")
    }
}

fun main() {
    // Or read a large test input from the `src/Day23_test.txt` file:
    val testInput = readInput("Day23_test")
    profiledCheck(7L, "Part 1 test") {
        val day = Day23.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck("co,de,ka,ta", "Part 2 test") {
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
