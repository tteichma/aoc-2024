private class Day22(val initialSecrets: List<Long>) {
    data class PriceHistoryKey(val a: Byte, val b: Byte, val c: Byte, val d: Byte)

    companion object {
        const val NUM_ITERATIONS = 2000

        fun advanceSecret(x: Long): Long {
            val step1 = prune(x.xor(x * 64))
            val step2 = prune(step1.xor(step1 / 32))
            return prune(step2.xor(step2 * 2048))
        }

        fun prune(x: Long) = x % 16777216

        fun fromInput(input: List<String>): Day22 {
            return Day22(input.map { it.toLong() })
        }
    }

    fun solvePart1(): Long {
        var secrets = initialSecrets
        repeat(NUM_ITERATIONS) {
            secrets = secrets.map { advanceSecret(it) }
        }
        return secrets.sum()
    }

    fun solvePart2(): Long {
        val historiesToSellValues = initialSecrets.map { initialSecret ->
            val secretSeries =
                (initialSecret..initialSecret + NUM_ITERATIONS).runningReduce { acc, _ -> advanceSecret(acc) }
            val prices = secretSeries.map { (it % 10) }
            val pricesBySequence = mutableMapOf<PriceHistoryKey, Long>()
            prices
                .reversed()  // Makes first occurrence take precedence.
                .windowed(5)
                .associateByTo(pricesBySequence, {
                    PriceHistoryKey(
                        (it[3] - it[4]).toByte(),
                        (it[2] - it[3]).toByte(),
                        (it[1] - it[2]).toByte(),
                        (it[0] - it[1]).toByte()
                    )
                }) { it[0] }
            pricesBySequence
        }
        val bestKey =  historiesToSellValues
            .flatMap { it.keys }
            .toSet()
            .maxBy { key ->
                historiesToSellValues
                    .sumOf { it.getOrDefault(key, 0L) }
            }
        val prices = historiesToSellValues.map { it.getOrDefault(bestKey,0L) }
        prices.println()
        return prices.sum()
    }
}

fun main() {
    val testInput1 = readInput("Day22_test1")
    val testInput2 = readInput("Day22_test2")
    profiledCheck(37327623L, "Part 1 test") {
        val day = Day22.fromInput(testInput1)
        day.solvePart1()
    }
    profiledCheck(23L, "Part 2 test") {
        val day = Day22.fromInput(testInput2)
        day.solvePart2()
    }

    // Read the input from the `src/Day22.txt` file.
    val input = readInput("Day22")
    profiledExecute("Part 1") {
        val day = Day22.fromInput(input)
        day.solvePart1()
    }.println()
    profiledExecute("Part 2") {
        val day = Day22.fromInput(input)
        day.solvePart2()
    }.println()
}
