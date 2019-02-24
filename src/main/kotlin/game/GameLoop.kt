package game

import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas

class GameLoop(val board: GameBoard, val canvas: Canvas) : AnimationTimer() {
    override fun handle(now: Long) {
        board.update()
        board.draw(canvas.graphicsContext2D)
    }
}
