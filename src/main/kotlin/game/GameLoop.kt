package game

import game.board.GameBoard
import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas

class GameLoop(private val board: GameBoard, private val canvas: Canvas, scaleRatio: Double) : AnimationTimer() {
    private var previousCall: Long = 0L
    private var paused = false
    private val graphicsContext = canvas.graphicsContext2D

    init {
        GameState.subscribe(GameEnded::class) {
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
        val updatableEntities = listOf(gameState.enemies, gameState.towers, gameState.projectiles, gameState.entities).flatten()
        updatableEntities.forEach { it.update(gameState, inSeconds) }

        gameState.currentWave?.update(inSeconds)
        gameState.commit()

        board.draw(graphicsContext, gameState)
        updatableEntities.forEach { it.draw(canvas.graphicsContext2D, gameState) }

        previousCall = now
    }
}
