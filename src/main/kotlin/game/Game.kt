package game

import javafx.scene.canvas.Canvas

class Game(width: Double, height: Double, canvas: Canvas) {
    private val gameState = GameState.instance
    private val board = GameBoard(width, height, gameState)
    private val gameLoop = GameLoop(board, canvas, gameState)
    
    fun start() = gameLoop.start()
}