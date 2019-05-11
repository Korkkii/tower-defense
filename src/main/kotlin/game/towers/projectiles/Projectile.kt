package game.towers.projectiles

import game.Enemy
import game.GameState
import game.MovingEntity
import javafx.scene.canvas.GraphicsContext

abstract class Projectile : MovingEntity {
    abstract val target: Enemy
    abstract val radius: Double
    open val graphicsComponent = ProjectileGraphicsComponent()
    var hasHit = false

    fun canDelete(): Boolean = hasHit

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}
