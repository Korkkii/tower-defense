package game

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import ui.MouseHandler

class Game(width: Double, height: Double, canvas: Canvas) {
    private val gameState = GameState.instance
    private val board = GameBoard(width, height, gameState)
    private val gameLoop = GameLoop(board, canvas, gameState)

    init {
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, gameState.mouseHandler)
        canvas.setOnMouseClicked {
            val square = board.squareAtPosition(it.position())
            val buildAreaSquare = square as? BuildAreaSquare
            val isBuildSquare = buildAreaSquare != null
            val hasNoTower = buildAreaSquare?.tower == null

            if (isBuildSquare && hasNoTower) {
                buildAreaSquare?.let { gameState.onNotify(PlaceTowerEvent(it)) }
            }

        }
    }

    fun start() = gameLoop.start()
}