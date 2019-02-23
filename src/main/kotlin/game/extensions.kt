package game

fun <T> List<List<Int>>.map(mappingFunction: (x: Int, y: Int, cellValue: Int) -> T ): List<List<T>> {
    return this.mapIndexed { columnIndex, cells ->
        cells.mapIndexed { rowIndex, cellValue ->
            mappingFunction(rowIndex, columnIndex, cellValue) } }
}

fun <T> List<List<Int>>.flatMap(mappingFunction: (x: Int, y: Int, cellValue: Int) -> T ): List<T> {
    return this.map(mappingFunction).flatten()
}