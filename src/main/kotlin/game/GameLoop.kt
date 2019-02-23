package game

import javafx.animation.AnimationTimer

class GameLoop(val board: GameBoard) : AnimationTimer() {
    override fun handle(now: Long) {
        board.update()
    }
}
