private class Day17(var a: Int, var b: Int, var c: Int, val instructions: List<Int>) {
    var currentPointer = 0

    fun getComboValue(combo: Int) = when (combo) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> a
        5 -> b
        6 -> c
        else -> TODO("Undefined")
    }

    fun getDiv(value: Int): Int {
        var out = a
        repeat(getComboValue(value)) {
            out /= 2
        }
        return out
    }

    fun executeNextInstruction(): Int? {
        val opCode = instructions[currentPointer]
        val value = instructions[currentPointer + 1]

        when (opCode) {
            0 -> { //ADV
                a = getDiv(value)
            }

            1 -> { //BXL
                b = b xor value
            }

            2 -> { //BST
                b = getComboValue(value) % 8
            }

            3 -> { //JNZ
                if (a != 0) {
                    currentPointer = value
                    return null
                }
            }

            4 -> { //BXC
                b = b xor c
            }

            5 -> { //OUT
                currentPointer += 2
                return getComboValue(value)%8
            }

            6 -> { //BDV
                b = getDiv(value)
            }

            7 -> { //CDV
                c = getDiv(value)
            }
        }
        currentPointer += 2
        return null
    }

    companion object {
        fun fromInput(input: List<String>): Day17 {
            val a = getIntsFromString(input[0]).first()
            val b = getIntsFromString(input[1]).first()
            val c = getIntsFromString(input[2]).first()
            val instructions = getIntListFromString(input[4])

            return Day17(a, b, c, instructions)
        }
    }

    fun solvePart1(): String {
        val outputs = mutableListOf<Int>()

        while (currentPointer < instructions.lastIndex) {
            executeNextInstruction()?.let { outputs.add(it) }
        }

        return outputs.joinToString(",")
    }

    fun solvePart2(): String {
        return ""
    }
}

fun main() {
    // Or read a large test input from the `src/Day17_test.txt` file:
    val testInput = readInput("Day17_test")
    profiledCheck("4,6,3,5,6,3,5,2,1,0", "Part 1 test") {
        val day = Day17.fromInput(testInput)
        day.solvePart1()
    }
    profiledCheck("", "Part 2 test") {
        val day = Day17.fromInput(testInput)
        day.solvePart2()
    }

    // Read the input from the `src/Day17.txt` file.
    val input = readInput("Day17")
    profiledExecute("Part 1") {
        val day = Day17.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day17.fromInput(input)
        day.solvePart2()
    }.println()
}
