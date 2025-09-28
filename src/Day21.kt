import kotlin.math.abs

private abstract class KeyPad {
    abstract fun createKeyToPosition(): Map<Char, IntCoordinate>

    val keyPairsToPositons = createKeyToPosition()
    val definedPositions = keyPairsToPositons.values.toSet()

    fun yieldPossibleInputSequences(outputSequence: String) = sequence {
        val fullOutputSequence = ("A$outputSequence").toList()
        val outputSequenceTokens: MutableList<List<String>> = mutableListOf()

        for ((fromChar, toChar) in fullOutputSequence.windowed(2)) {
            val fromPos = keyPairsToPositons[fromChar]!!
            val toPos = keyPairsToPositons[toChar]!!
            val offset = toPos - fromPos
            val horizontalSequence = (if (offset.first > 0) ">" else "<").repeat(abs(offset.first))
            val verticalSequence = (if (offset.second > 0) "^" else "v").repeat(abs(offset.second))

            val currentSubSequence = mutableListOf<String>()
            if (offset.first != 0&& fromPos + IntCoordinate(offset.first, 0) in definedPositions) {
                currentSubSequence.add("${horizontalSequence}${verticalSequence}A")
            }
            if (offset.second!=0&&fromPos + IntCoordinate(0, offset.second) in definedPositions) {
                currentSubSequence.add("${verticalSequence}${horizontalSequence}A")
            }
            if (offset == IntCoordinate(0,0)){
                currentSubSequence.add("A")
            }
            outputSequenceTokens.add(currentSubSequence)

        }
        yieldAll(outputSequenceTokens.cartesianProduct())
    }
}

    private object NumberKeyPad : KeyPad() {
        override fun createKeyToPosition(): Map<Char, IntCoordinate> {
            val out = mutableMapOf(
                '0' to IntCoordinate(1, 0), 'A' to IntCoordinate(2, 0)
            )

            for (numTo in 1..9) {
                out[numTo.digitToChar()] = IntCoordinate((numTo - 1) % 3, (numTo - 1) / 3 + 1)
            }
            return out.toMap()
        }
    }

    private object DirectionKeyPad : KeyPad() {
        override fun createKeyToPosition() = mapOf(
            '<' to IntCoordinate(0, 0),
            'v' to IntCoordinate(1, 0),
            '>' to IntCoordinate(2, 0),
            '^' to IntCoordinate(1, 1),
            'A' to IntCoordinate(2, 1),
        )
    }

    private class Day21(val lines: List<String>) {
        companion object {
            fun fromInput(input: List<String>): Day21 {
                return Day21(input)
            }
        }

        fun solvePart1(): Long {
            var result = 0L
            for (line in lines) {
                var possibleInputSequences = NumberKeyPad.yieldPossibleInputSequences(line).toList()
                repeat(2) {
                    possibleInputSequences =
                        possibleInputSequences.flatMap { DirectionKeyPad.yieldPossibleInputSequences(it) }
                }
                result += possibleInputSequences.minOf { it.length } * line.dropLast(1).toInt()
            }
            return result
        }

        fun solvePart2(): Long {
            return 0L
        }
    }

    fun main() {
        // Or read a large test input from the `src/Day21_test.txt` file:
        val testInput = readInput("Day21_test")
        profiledCheck(126384L, "Part 1 test") {
            val day = Day21.fromInput(testInput)
            day.solvePart1()
        }
        profiledCheck(0L, "Part 2 test") {
            val day = Day21.fromInput(testInput)
            day.solvePart2()
        }

        // Read the input from the `src/Day21.txt` file.
        val input = readInput("Day21")
        profiledExecute("Part 1") {
            val day = Day21.fromInput(input)
            day.solvePart1()
        }.println()
        profiledExecute("Part 2") {
            val day = Day21.fromInput(input)
            day.solvePart2()
        }.println()
    }
