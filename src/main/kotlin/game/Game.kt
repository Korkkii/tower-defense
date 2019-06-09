package game

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class Game(width: Double, height: Double, val canvas: Canvas) {
    private val gameState = GameState.instance
    private val board = GameBoard(width, height)
    private val waveGenerator = WaveGenerator(board)
    private val gameLoop = GameLoop(board, canvas, gameState, board.ratio)

    init {
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, gameState.mouseHandler)
        canvas.setOnMouseClicked(::handleMouseClick)
    }

    private fun handleMouseClick(event: MouseEvent) {
        val graphicsContext = canvas.graphicsContext2D
        val inWorldCoords = graphicsContext.transform.inverseTransform(event.position())
        val square = board.squareAtPosition(inWorldCoords)
        val placingTower = GameState.instance.state is PlacingTower
        val buildAreaSquare = square as? BuildAreaSquare
        val squareHasTower = buildAreaSquare?.tower != null

        if (placingTower) {
            val isBuildSquare = buildAreaSquare != null
            val hasNoTower = buildAreaSquare?.tower == null

            if (isBuildSquare && hasNoTower) {
                buildAreaSquare?.let { GameState.notify(PlaceTowerEvent(it)) }
            }
        } else if (squareHasTower) {
            buildAreaSquare?.tower?.let { GameState.notify(SelectTowerEvent(it)) }
        } else {
            GameState.notify(EmptyClick)
        }
    }

    fun start() = gameLoop.start()
}
