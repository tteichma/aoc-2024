fun List<List<String>>.cartesianProduct(prefix: String=""): Sequence<String> = sequence {
    if (this@cartesianProduct.isEmpty()){
        yield(prefix)
        return@sequence
    }

    val indexFirstMultipleOptions = this@cartesianProduct.withIndex().firstOrNull { it.value.size > 1 }
    if (indexFirstMultipleOptions == null) {
        yield(prefix + this@cartesianProduct.joinToString("") { it.first() })
        return@sequence
    }

    val updatedPrefix = prefix + this@cartesianProduct.take(indexFirstMultipleOptions.index).joinToString(""){it.first()}
    for (currentOption in indexFirstMultipleOptions.value){
        val remainingOptions = this@cartesianProduct.drop(indexFirstMultipleOptions.index+1)
        yieldAll(remainingOptions.cartesianProduct(updatedPrefix+currentOption))
    }
}