private data class DiskSegment(val length: Int, val fileId: Int?)

private fun List<DiskSegment>.getScore(): Long {
    var pos = 0L
    var checksum = 0L
    for (segment in this) {
        if (segment.fileId != null) {
            for (i in 0..<segment.length) {
                checksum += (pos + i) * segment.fileId
            }
        }
        pos += segment.length
    }
    return checksum
}

private fun List<DiskSegment>.println() =
    this.joinToString("") { (it.fileId ?: ".").toString().repeat(it.length) }.println()

private class Day09(val diskContent: List<DiskSegment>) {
    companion object {
        fun fromInput(input: List<String>): Day09 {
            return Day09(
                input
                    .first()
                    .withIndex()
                    .map { DiskSegment(it.value.digitToInt(), if (it.index % 2 == 0) it.index / 2 else null) }
            )
        }
    }


    fun solvePart1(): Long {
        val mutableDiskContent = diskContent.toMutableList()
        val updatedDiskContent = mutableListOf<DiskSegment>()

        while (mutableDiskContent.isNotEmpty()) {
            val lastSegment = mutableDiskContent.removeLast()
            if (lastSegment.fileId == null) {
                continue
            }

            val firstSegment = if (mutableDiskContent.isNotEmpty()) {
                mutableDiskContent.removeFirst()
            } else {
                updatedDiskContent.add(lastSegment)
                break
            }
            if (firstSegment.fileId != null) {
                updatedDiskContent.addLast(firstSegment)
                mutableDiskContent.addLast(lastSegment)  // Put it back
                continue
            }

            if (firstSegment.length == lastSegment.length) {
                updatedDiskContent.addLast(lastSegment)
            } else if (firstSegment.length < lastSegment.length) {
                updatedDiskContent.addLast(DiskSegment(firstSegment.length, lastSegment.fileId))
                mutableDiskContent.addLast(DiskSegment(lastSegment.length - firstSegment.length, lastSegment.fileId))
            } else { // first > second
                updatedDiskContent.addLast(DiskSegment(lastSegment.length, lastSegment.fileId))
                mutableDiskContent.addFirst(DiskSegment(firstSegment.length - lastSegment.length, null))
            }
        }

        return updatedDiskContent.getScore()
    }

    fun solvePart2(): Long {
        val mutableDiskContent = diskContent.toMutableList()

        var indLastUntriedSegment = mutableDiskContent.lastIndex

        while (indLastUntriedSegment > 0) {
            while (mutableDiskContent[indLastUntriedSegment].fileId == null) {
                --indLastUntriedSegment
            }

            val lastUntriedSegment = IndexedValue(indLastUntriedSegment, mutableDiskContent[indLastUntriedSegment])

            val firstSuitableGap = mutableDiskContent
                .withIndex()
                .firstOrNull { it.value.fileId == null && it.value.length >= lastUntriedSegment.value.length }

            if (firstSuitableGap == null || firstSuitableGap.index >= lastUntriedSegment.index) {
                --indLastUntriedSegment
                continue
            }

            mutableDiskContent.removeAt(lastUntriedSegment.index)
            mutableDiskContent.add(lastUntriedSegment.index, DiskSegment(lastUntriedSegment.value.length, null))
            mutableDiskContent.removeAt(firstSuitableGap.index)
            mutableDiskContent.add(firstSuitableGap.index, lastUntriedSegment.value)
            val lengthDifference = firstSuitableGap.value.length - lastUntriedSegment.value.length
            if (lengthDifference == 0) {
                --indLastUntriedSegment
            } else {
                mutableDiskContent.add(
                    firstSuitableGap.index + 1,
                    DiskSegment(lengthDifference, null)
                )
            }
        }

        return mutableDiskContent.getScore()
    }
}

fun main() {
    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test")
    profiledCheck(1928L, "Part 1 test") {
        val day = Day09.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck(2858L, "Part 2 test") {
        val day = Day09.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    profiledExecute("Part 1") {
        val day = Day09.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day09.fromInput(input)
        day.solvePart2()
    }.println()
}
