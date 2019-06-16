package game.board

import game.GameEntity
import game.GameState
import game.Vector
import game.map
import javafx.scene.canvas.GraphicsContext
import java.io.File

class GameBoard(private val width: Double, private val height: Double) : GameEntity(Vector(0.0, 0.0)) {
    private val rectangleWidth = 16.0
    private val rectangleHeight = 16.0
    private val children = mutableListOf<BoardSquare>()
    private val lines = File("src/main/resources/map.txt").readLines()
    private val rawBoard = lines.map { it.toCharArray().asList() }
    private val board: List<List<BoardSquare>> = rawBoard.map(this::mapBoardValueToSquare)
    val ratio = (width / rectangleWidth / rawBoard.size)
    val path: List<PathSquare> = generatePath(board)

    init {
        val flattened = board.flatten()
        children.addAll(flattened)
    }

    private fun mapBoardValueToSquare(x: Int, y: Int, cellValue: Char): BoardSquare {
        val constructor = when (cellValue) {
            'u' -> ::UpSquare
            'd' -> ::DownSquare
            'l' -> ::LeftSquare
            'r' -> ::RightSquare
            's' -> ::StartSquare
            else -> ::BuildAreaSquare
        }

        return constructor(
            x * rectangleWidth,
            y * rectangleHeight,
            rectangleWidth,
            rectangleHeight
        )
    }

    override fun update(currentState: GameState, delta: Double) {}

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fillRect(0.0, 0.0, width, height)
        children.forEach { it.draw(graphics, state) }
    }

    private fun generatePath(board: List<List<BoardSquare>>): List<PathSquare> {
        val flattenedBoard = board.flatten()
        val startSquare =
            flattenedBoard.find { it is StartSquare } as? StartSquare ?: throw Error("No start square found")
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
