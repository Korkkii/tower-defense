package game

import javafx.scene.canvas.GraphicsContext

interface GraphicsComponent<T : GameEntity> {
    fun draw(entity: T, graphics: GraphicsContext, state: GameState)
}
