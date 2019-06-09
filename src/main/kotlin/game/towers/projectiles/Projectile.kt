package game.towers.projectiles

import game.Enemy
import game.GameEntity
import game.GameState
import game.Vector
import game.towers.Tower
import javafx.scene.canvas.GraphicsContext

class Projectile(val tower: Tower, val target: Enemy, val type: ProjectileType) : GameEntity {
    private val graphicsComponent = ProjectileGraphicsComponent()
    var hasHit = false
    var position: Vector = tower.square.center

    fun canDelete(): Boolean = hasHit

    override fun update(currentState: GameState, delta: Double) {
        type.physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}
