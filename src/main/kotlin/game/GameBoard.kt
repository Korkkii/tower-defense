package game

import game.towers.SingleTower
import javafx.scene.canvas.GraphicsContext
import java.io.File

class GameBoard(private val width: Double, private val height: Double, private val gameState: GameState) : GameEntity {
    private val children = mutableListOf<BoardSquare>()
    private val lines = File("src/main/resources/map.txt").readLines()
    private val rawBoard = lines.map { it.toCharArray().asList() }
    private val board: List<List<BoardSquare>>
    val path: List<PathSquare>

    init {
        val rectangleWidth = 16.0
        val rectangleHeight = 16.0
        board = rawBoard.map { x, y, cellValue ->
            when (cellValue) {
                'u' -> UpSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                'd' -> DownSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                'l' -> LeftSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                'r' -> RightSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                's' -> StartSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                else -> BuildAreaSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
            }
        }
        val flattened = board.flatten()
        children.addAll(flattened)
        val start = flattened.find { it is StartSquare } as? StartSquare ?: throw Error("No start square found")
        val generatedPath = generatePath(board, start)

        path = generatedPath
        gameState.currentWave = Wave(1, this)
        val towerPlace = board[3][3] as BuildAreaSquare
        val tower = towerPlace?.let { SingleTower(it) }

        tower?.let { gameState.towers.add(it) }
    }

    override fun update(currentState: GameState, delta: Double) {}

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fillRect(0.0, 0.0, width, height)
        children.forEach { it.draw(graphics, state) }
    }

    private fun generatePath(board: List<List<BoardSquare>>, startSquare: StartSquare): List<PathSquare> {
        val flattenedBoard = board.flatten()
        val resultPath = mutableListOf<PathSquare>()
        var last: PathSquare = startSquare
        var loopComplete = false

        while (!loopComplete) {
            val lastIndex = flattenedBoard.indexOf(last)
            val x = lastIndex % board[0].size
            val y = lastIndex / board.size
            val nextX = x + last.nextDirection.x
            val nextY = y + last.nextDirection.y

            val nextSquare = board[nextY][nextX] as PathSquare

            if (!resultPath.contains(nextSquare)) {
                resultPath += nextSquare
            } else {
                loopComplete = true
            }

            last = nextSquare
        }

        return resultPath
    }

    fun squareAtPosition(position: Vector): BoardSquare? {
        return board.flatten().find { it.contains(position.x, position.y) }
    }
}
