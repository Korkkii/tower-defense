package game

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas

class GameLoop(private val board: GameBoard, private val canvas: Canvas, gameState: GameState, scaleRatio: Double) : AnimationTimer() {
    private var previousCall: Long = 0L
    private var paused = false
    private val graphicsContext = canvas.graphicsContext2D

    init {
        GameState.subscribe(GameEnded::class.java) {
            paused = true
        }
        graphicsContext.scale(scaleRatio, scaleRatio)
    }

    override fun handle(now: Long) {
        if (paused) return

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
        gameState.currentWave?.update(gameState, inSeconds)
        gameState.commit()

        board.draw(graphicsContext, gameState)
        gameState.enemies.forEach { it.draw(canvas.graphicsContext2D, gameState) }
        gameState.towers.forEach { it.draw(canvas.graphicsContext2D, gameState) }
        gameState.projectiles.forEach { it.draw(canvas.graphicsContext2D, gameState) }

        previousCall = now
    }
}
