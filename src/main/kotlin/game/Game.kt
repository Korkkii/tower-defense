package game

import game.board.BuildAreaSquare
import game.board.GameBoard
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Rectangle
import ui.AlertHandler
import ui.rectangle

class Game(width: Double, height: Double, val canvas: Canvas) {
    private val gameState = GameState.instance
    private val board = GameBoard(width, height)
    private val gameLoop = GameLoop(board, canvas, board.ratio)
    private val alertBoxSize = Vector(width * 0.15 / board.ratio, height * 0.125 / board.ratio)
    private val alertBoxLocation = Vector(width * 0.825 / board.ratio, height * 0.85 / board.ratio)
    @Suppress("unused")
    private val waveGenerator = WaveGenerator(board)
    @Suppress("unused")
    private val alertHandler = AlertHandler(rectangle(alertBoxLocation, alertBoxSize))

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
