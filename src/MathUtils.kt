fun Long.pow(exp: Int): Long {
    if (exp == 0) return 1L
    var out = this
    repeat(exp - 1) {
        out *= this
    }
    return out
}

fun getPrimeFactors(number: Long): List<Long> {
    var n = number
    val primes = mutableListOf<Long>()
    for (i in 2L..n / 2) {
        while (n % i == 0L) {
            primes.add(i)
            n /= i
        }
    }
    if (n != 1L) {
        primes.add(n)
    }
    return primes.toList()
}

fun getLcm(a: Long, b: Long): Long {
    val primesA = getPrimeFactors(a).groupBy { it }.map { it.key to it.value.size }
    val primesB = getPrimeFactors(b).groupBy { it }.map { it.key to it.value.size }
    return (primesA + primesB)
        .groupBy { it.first }
        .map { it.key to it.value.maxOf { v -> v.second } }
        .map { it.first.pow(it.second) }
        .reduce { acc, it -> acc * it }
}

fun solveChineseRemainderTheorem(inputs: List<Pair<Long, Long>>): Long {
    // NOTE: Does not work for recurrences sharing lcd, yet.
    val offsets = inputs.map { it.first }  // a_i
    val recurrences = inputs.map { it.second }  // m_i

    val recurrencesProduct = recurrences.reduce { acc, n -> acc * n }
    val factors = recurrences.map { recurrencesProduct / it }
    val x = inputs.indices.map { i -> (1..<recurrences[i]).first { (it * factors[i]) % recurrences[i] == 1L } }

    val solution = inputs.indices.sumOf { offsets[it] * factors[it] * x[it] }
    return solution % recurrencesProduct
}
