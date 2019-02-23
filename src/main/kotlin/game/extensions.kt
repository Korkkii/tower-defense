package game

fun <T, U> List<List<U>>.map(mappingFunction: (x: Int, y: Int, cellValue: U) -> T ): List<List<T>> {
    return this.mapIndexed { columnIndex, cells ->
        cells.mapIndexed { rowIndex, cellValue ->
            mappingFunction(rowIndex, columnIndex, cellValue) } }
}

fun <T, U> List<List<U>>.flatMap(mappingFunction: (x: Int, y: Int, cellValue: U) -> T ): List<T> {
    return this.map(mappingFunction).flatten()
}