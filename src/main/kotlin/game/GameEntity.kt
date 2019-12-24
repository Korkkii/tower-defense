package game

import javafx.scene.canvas.GraphicsContext

abstract class GameEntity(var position: Vector) : UpdatableEntity, DrawableEntity

interface UpdatableEntity {
    fun update(currentState: GameState, delta: Double)
}

interface DrawableEntity {
    fun draw(graphics: GraphicsContext, state: GameState)
}
