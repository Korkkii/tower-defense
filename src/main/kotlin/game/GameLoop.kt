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
        val gameState = GameState.instance

        board.update(gameState, inSeconds)
        gameState.enemies.forEach { it.update(gameState, inSeconds) }
        gameState.towers.forEach { it.update(gameState, inSeconds) }
        gameState.projectiles.forEach {
            it.update(gameState, inSeconds)
        }
        gameState.projectiles.removeAll { it.canDelete() }
        gameState.enemies.removeAll { it.health <= 0.0 || it.canDelete() }
        board.draw(canvas.graphicsContext2D, gameState)
        gameState.enemies.forEach { it.draw(canvas.graphicsContext2D, gameState) }
        gameState.towers.forEach { it.draw(canvas.graphicsContext2D, gameState) }
        gameState.projectiles.forEach { it.draw(canvas.graphicsContext2D, gameState) }

        previousCall = now
    }
}
