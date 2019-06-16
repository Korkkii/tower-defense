package game

import javafx.scene.canvas.GraphicsContext

interface Component

interface GraphicsComponent<T : GameEntity> : Component {
    fun draw(entity: T, graphics: GraphicsContext, state: GameState)
}

interface PhysicsComponent<T : GameEntity> : Component {
    fun update(entity: T, currentState: GameState, delta: Double)
}
