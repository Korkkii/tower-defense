package game.towers

import game.BuildAreaSquare
import game.Circle
import game.GameEntity
import game.GameState
import javafx.scene.canvas.GraphicsContext

class Tower(val square: BuildAreaSquare, val type: TowerType) : GameEntity {
    val rangeCircle by lazy { Circle(square.center, type.range) }
    var canBeDeleted = false
        private set
    private val physicsComponent = type.physicsComponentConstructor()

    init {
        square.tower = this
    }

    fun deleteTower() {
        if (square.tower == this) square.tower = null
        canBeDeleted = true
    }

    fun upgrade(upgradeType: TowerType): Tower = Tower(square, upgradeType)

    override fun update(currentState: GameState, delta: Double) {
        physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        type.graphicsComponent.draw(this, graphics, state)
    }
}
