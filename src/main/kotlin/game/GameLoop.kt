package game

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas

class GameLoop(private val board: GameBoard, private val canvas: Canvas, private val gameState: GameState) : AnimationTimer() {
    private var previousCall: Long = 0L
    override fun handle(now: Long) {
        if (previousCall == 0L) {
            previousCall = now
            return
        }

        val differenceInNanoseconds = now - previousCall
        val inSeconds = differenceInNanoseconds / 1000000000.0

        board.update(gameState, inSeconds)
        gameState.enemies.forEach { it.update(gameState, inSeconds) }
        gameState.towers.forEach { it.update(gameState, inSeconds) }
        board.draw(canvas.graphicsContext2D)
        gameState.enemies.forEach { it.draw(canvas.graphicsContext2D) }
        gameState.towers.forEach { it.draw(canvas.graphicsContext2D) }

        previousCall = now
    }
}
