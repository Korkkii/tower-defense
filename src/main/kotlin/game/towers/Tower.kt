package game.towers

import game.board.BuildAreaSquare
import game.circle
import game.GameEntity
import game.GameState
import game.StatusEffects
import javafx.scene.canvas.GraphicsContext

class Tower(val square: BuildAreaSquare, val type: TowerType) : GameEntity(square.center) {
    val rangeCircle by lazy { circle(square.center, type.range) }
    var fireRate = type.baseFireRate
    var canBeDeleted = false
        private set
    private val physicsComponent = type.physicsComponentConstructor()
    val statusEffects = StatusEffects<Tower>()

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
        statusEffects.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        type.graphicsComponent.draw(this, graphics, state)
    }
}
