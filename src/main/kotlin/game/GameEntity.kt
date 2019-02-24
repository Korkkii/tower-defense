package game

import javafx.scene.canvas.GraphicsContext

interface GameEntity {
    fun update(currentState: GameState, delta: Double)

    fun draw(graphics: GraphicsContext)
}

interface MovingEntity: GameEntity {
    val velocity: Double
    var position: Vector
    var targetPosition: Vector
}