private class Day05(val edges: List<Pair<Int, Int>>, val pageLists: List<List<Int>>) {
    // edge.first is a dependency of edge.second.
    val nodesToDependencies: Map<Int, Set<Int>> by lazy {
        val m = mutableMapOf<Int, MutableSet<Int>>()
        for (edge in edges) {
            m.getOrPut(edge.second) { mutableSetOf() }.add(edge.first)
        }
        edges.filter { it.first !in m }.map { m[it.first] = mutableSetOf() }
        m
    }

    fun getFilteredNodesToDependencies(nodesToKeep: List<Int>) = nodesToDependencies
        .mapValues { m -> m.value.filter { it in nodesToKeep }.toMutableSet() }
        .filter { it.key in nodesToKeep }
        .toMutableMap()


    fun areNodesReachableInOrder(nodesToVisit: List<Int>): Boolean {
        // Thin out graph to only contain nodes mentions in the input line.
        val mutableNodesToDependencies = getFilteredNodesToDependencies(nodesToVisit)
        for (wantedNode in nodesToVisit) {
            if (mutableNodesToDependencies[wantedNode]!!.isNotEmpty()) return false

            mutableNodesToDependencies.values.forEach { it.remove(wantedNode) }
        }
        return true
    }

    fun suggestValidModeOrder(nodesToVisit: List<Int>): List<Int> {
        // Thin out graph to only contain nodes mentions in the input line.
        val mutableNodesToDependencies = getFilteredNodesToDependencies(nodesToVisit)

        val output = mutableListOf<Int>()
        while (mutableNodesToDependencies.isNotEmpty()) {
            val validNextNodes = mutableNodesToDependencies
                .filter { it.value.isEmpty() }
                .keys
            output.addAll(validNextNodes)
            mutableNodesToDependencies.values.forEach { it.removeAll(validNextNodes) }
            mutableNodesToDependencies.keys.removeAll(validNextNodes)
        }
        return output
    }

    companion object {
        fun fromInput(input: List<String>): Day05 {
            val edgePattern = Regex("""(\d+)\|(\d+)""")
            val edges = input
                .takeWhile { it != "" }
                .mapNotNull { edgePattern.matchEntire(it)?.groupValues }
                .map { Pair(it[1].toInt(), it[2].toInt()) }
            val pageLists = input
                .dropWhile { it != "" }
                .drop(1)
                .map { line -> line.split(",").map { it.toInt() } }
            return Day05(edges, pageLists)
        }
    }

    fun solvePart1(): Long {
        return pageLists
            .filter { areNodesReachableInOrder(it) }
            .sumOf { it[it.size / 2] }
            .toLong()
    }

    fun solvePart2(): Long {
        return pageLists
            .filterNot { areNodesReachableInOrder(it) }
            .map { suggestValidModeOrder(it) }
            .sumOf { it[it.size / 2] }
            .toLong()
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day05_test")
    profiledCheck(143L, "Part 1 test") {
        val day = Day05.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(123L, "Part 2 test") {
        val day = Day05.fromInput(testInput)
        day.solvePart2()
    }

    val input = readInput("Day05")
    profiledExecute("Part 1") {
        val day = Day05.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day05.fromInput(input)
        day.solvePart2()
    }.println()
}
