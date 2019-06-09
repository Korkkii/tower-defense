package game

import javafx.scene.canvas.GraphicsContext

interface GameEntity : DrawableEntity, UpdatableEntity

interface UpdatableEntity {
    fun update(currentState: GameState, delta: Double)
}

interface DrawableEntity {
    fun draw(graphics: GraphicsContext, state: GameState)
}
