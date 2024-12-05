private data class DirectedGraph <N>(val edges: List<Pair<N,N>>) {
    // edge.first is a dependency of edge.second.
    val nodesToDependencies: Map<N, Set<N>> by lazy {
        val m = mutableMapOf<N, MutableSet<N>>()
        for (edge in edges){
            m.getOrPut(edge.second, { mutableSetOf()}).add(edge.first)
        }
        edges.filter { it.first !in m }.map { m[it.first] = mutableSetOf() }
        m
    }

    fun areNodesReachableInOrder(nodesToVisit: List<N>): Boolean {
        // Thin out graph to only contain nodes mentiones in the input line.
        val mutableNodesToPredecessors  = nodesToDependencies
            .mapValues { m -> m.value.filter { it in nodesToVisit }.toMutableSet() }
            .toMutableMap()

        for (wantedNode in nodesToVisit)
        {
            if (mutableNodesToPredecessors[wantedNode]!!.isNotEmpty()) return false

            mutableNodesToPredecessors.values.forEach { it.remove(wantedNode ) }
        }
        return true
    }
}

private class Day05(val graph: DirectedGraph<Int>, val pageLists: List<List<Int>>) {
    companion object {
        fun fromInput(input: List<String>): Day05 {
            val edgePattern = Regex("""(\d+)\|(\d+)""")
            val edges = input
                .takeWhile { it != "" }
                .mapNotNull { edgePattern.matchEntire(it)?.groupValues }
                .map{Pair(it[1].toInt(), it[2].toInt())}
            val pageLists = input
                .dropWhile { it!= "" }
                .drop(1)
                .map {line -> line.split(",").map { it.toInt() }}
            return Day05(DirectedGraph(edges), pageLists)
        }
    }

    fun solvePart1(): Long {
        return pageLists
            .filter {graph.areNodesReachableInOrder(it) }
            .also { it.println() }
            .sumOf { it[it.size / 2] }
            .toLong()
    }

    fun solvePart2(): Long {
        return 0L
    }
}

fun main() {
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day05_test")
    profiledCheck(143L, "Part 1 test") {
        val day = Day05.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(0L, "Part 2 test") {
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
