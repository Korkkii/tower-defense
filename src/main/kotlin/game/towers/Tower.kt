package game.towers

import game.BuildAreaSquare
import game.Circle
import game.GameEntity
import game.GameState
import javafx.scene.canvas.GraphicsContext

class Tower(val square: BuildAreaSquare, val type: TowerType) : GameEntity {
    val rangeCircle by lazy { Circle(square.center, type.range) }

    init {
        square.tower = this
    }

    fun deleteTower() {
        square.tower = null
    }

    override fun update(currentState: GameState, delta: Double) {
        type.physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        type.graphicsComponent.draw(this, graphics, state)
    }
}
