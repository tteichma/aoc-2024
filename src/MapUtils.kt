// Note: Coordinates are (0, 0) is top left, (x, 0) is bottom left, (y, 0) is top right!
typealias IntCoordinate = Pair<Int, Int>

operator fun IntCoordinate.plus(other: IntCoordinate) = Pair(this.first + other.first, this.second + other.second)
operator fun IntCoordinate.minus(other: IntCoordinate) = Pair(this.first - other.first, this.second - other.second)

fun <T> List<List<T>>.rotateRight(): List<List<T>> {
    val lastOldColIndex = this.first().lastIndex
    val lastOldRowIndex = this.lastIndex
    return (0..lastOldColIndex).map { iNewRow ->
        (0..lastOldRowIndex).map { this[lastOldRowIndex - it][iNewRow] }
    }
}

fun <T> List<List<T>>.rotateLeft(): List<List<T>> {
    val lastOldColIndex = this.first().lastIndex
    val lastOldRowIndex = this.lastIndex
    return (lastOldColIndex downTo 0).map { iNewRow ->
        (0..lastOldRowIndex).map { this[it][iNewRow] }
    }
}


sealed class Direction(val nextCoordinate: (IntCoordinate) -> IntCoordinate) {
    data object LR : Direction({ Pair(it.first, it.second + 1) })
    data object RL : Direction({ Pair(it.first, it.second - 1) })
    data object UD : Direction({ Pair(it.first + 1, it.second) })
    data object DU : Direction({ Pair(it.first - 1, it.second) })

    val opposite
        get() = oppositeDirs[this]!!
    val perpendicular
        get() = perpendicularDirsLists[this] ?: listOf()

    val right
        get() = when (this) {
            DU -> LR
            LR -> UD
            UD -> RL
            RL -> DU
        }

    companion object {
        val entries
            get() = allEntries

        private val oppositeDirs by lazy {
            mapOf(
                LR to RL,
                RL to LR,
                UD to DU,
                DU to UD
            )
        }
        private val perpendicularDirsLists by lazy {
            mapOf(
                LR to listOf(UD, DU),
                RL to listOf(UD, DU),
                UD to listOf(LR, RL),
                DU to listOf(LR, RL)
            )
        }
        private val allEntries by lazy { listOf(LR, RL, UD, DU) }
    }
}

operator fun IntCoordinate.plus(direction: Direction) = when (direction) {
    Direction.DU -> Pair(this.first - 1, this.second)
    Direction.UD -> Pair(this.first + 1, this.second)
    Direction.RL -> Pair(this.first, this.second - 1)
    Direction.LR -> Pair(this.first, this.second + 1)
}

open class DataMap<T>(val data: List<List<T>>) {
    init {
        check(data.map { it.size }.toSet().size == 1)
    }

    fun rotateLeft() = data.rotateLeft()
    fun rotateRight() = data.rotateRight()

    val lastRowIndex = data.lastIndex
    val lastColIndex = data.first().lastIndex
    val rowSize = data.size
    val colSize = data.first().size

    val rowIndices = data.indices
    val colIndices = data.first().indices

    val coordinates by lazy { rowIndices.flatMap { indRow -> colIndices.map { indCol -> Pair(indRow, indCol) } } }

    operator fun get(key: IntCoordinate) = data[key.first][key.second]

    fun copyDataWithModification(coordinate: IntCoordinate, newValue: T) = data
        .withIndex()
        .map { (iRow, row) ->
            row
                .withIndex()
                .map { if (iRow == coordinate.first && it.index == coordinate.second) newValue else it.value }
        }

    fun getCoordinatesWithValue(value: T): Sequence<IntCoordinate> = sequence {
        for ((iRow, row) in data.withIndex()) {
            for ((iCol, it) in row.withIndex()) {
                if (it == value) yield(Pair(iRow, iCol))
            }
        }
    }

    protected fun IntCoordinate.isWithinBoundaries() =
        (this.first in rowIndices && this.second in colIndices)

    protected fun IntCoordinate.getNeighbours(predicate: (IntCoordinate) -> Boolean = { true }) =
        Direction.entries
            .map { this@getNeighbours + it }
            .filter { it.isWithinBoundaries() }
            .filter { predicate(it) }

    fun floodMap(start: IntCoordinate, predicate: (T) -> Boolean): Set<IntCoordinate> {
        val expectedValue = data[start]
        val seen = mutableSetOf<IntCoordinate>()
        val toExplore = mutableSetOf(start)

        while (toExplore.isNotEmpty()) {
            val currentCoordinate = toExplore.pop() ?: break
            seen.add(currentCoordinate)
            toExplore.addAll(
                currentCoordinate
                    .getNeighbours { this[it] == expectedValue }
                    .filterNot { it in seen })
        }

        return seen
    }

    override fun toString() = data.joinToString("\n") { row -> row.joinToString("") }
}