package game

import javafx.scene.canvas.GraphicsContext

class GameBoard(private val width: Double, private val height: Double, private val gameState: GameState) : GameEntity {
    private val children = mutableListOf<BoardSquare>()
    private val rawBoard = listOf(
        listOf(".","s",".",".","."),
        listOf(".","x",".",".","."),
        listOf(".","x","x","x","."),
        listOf(".",".",".","x","."),
        listOf(".",".",".","e","."))
    val board: List<List<BoardSquare>>
    val path: List<PathSquare>

    init {
        val rectangleWidth = width / rawBoard[0].size
        val rectangleHeight = height / rawBoard.size
        board = rawBoard.map { x, y, cellValue ->
            when(cellValue) {
                "e" -> EndSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                "x" -> PathSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                "s" -> StartSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                else -> BuildAreaSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
            }
        }
        val flattened = board.flatten()
        children.addAll(flattened)
        val start = flattened.find { it is StartSquare } as? StartSquare ?: throw Error("No start square found")
        path = generatePath(board, start)

        val enemy = Enemy(this)
        gameState.enemies.add(enemy)
        val towerPlace = board[3][2] as? BuildAreaSquare
        val tower = towerPlace?.let { Tower(it) }

        tower?.let { gameState.towers.add(it) }
    }

    override fun update(currentState: GameState, delta: Double) {}

    override fun draw(graphics: GraphicsContext) {
        graphics.fillRect(0.0, 0.0, width, height)
        children.forEach { it.draw(graphics) }
    }

    private fun getNeighbours(x: Int, y: Int): List<BoardSquare> {
        val result = mutableListOf<BoardSquare>()
        for (direction in directions) {
            val i = x + direction.x
            val j = y + direction.y
            if (isOnBoard(i, j)) result += board[j][i]
        }

        return result
    }

    private fun isOnBoard(x: Int, y: Int) = x >= 0 && y >= 0 && x < board.size && y < board.size

    private fun generatePath(board: List<List<BoardSquare>>, startSquare: StartSquare): List<PathSquare> {
        val resultPath = mutableListOf<PathSquare>(startSquare)
        var last: PathSquare = startSquare

        while (last !is EndSquare) {
            val lastIndex = board.flatten().indexOf(last)
            val x = lastIndex % board.size
            val y = lastIndex / board.size
            val neighbours = getNeighbours(x, y)
            val next = neighbours.find { it is PathSquare && !resultPath.contains(it) } as? PathSquare
                ?: throw Error("Can't find proper path")

            resultPath += next
            last = next
        }

        return resultPath
    }
}
