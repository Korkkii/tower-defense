package game

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class Game(width: Double, height: Double, canvas: Canvas) {
    private val gameState = GameState.instance
    private val board = GameBoard(width, height, gameState)
    private val gameLoop = GameLoop(board, canvas, gameState)

    init {
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, gameState.mouseHandler)
        canvas.setOnMouseClicked {
            val square = board.squareAtPosition(it.position())
            val placingTower = gameState.state is PlacingTower<*>
            val buildAreaSquare = square as? BuildAreaSquare
            val squareHasTower = buildAreaSquare?.tower != null ?: false

            if (placingTower) {
                val isBuildSquare = buildAreaSquare != null
                val hasNoTower = buildAreaSquare?.tower == null

                if (isBuildSquare && hasNoTower) {
                    buildAreaSquare?.let { gameState.onNotify(PlaceTowerEvent(it)) }
                }
            } else if (squareHasTower) {
                buildAreaSquare?.tower?.let { gameState.onNotify(SelectTowerEvent(it)) }
            }
        }
    }

    fun start() = gameLoop.start()
}
