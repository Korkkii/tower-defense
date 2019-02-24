package game

import javafx.scene.canvas.GraphicsContext

interface GameEntity {
    fun update(currentState: GameState, delta: Double)

    fun draw(graphics: GraphicsContext)
}