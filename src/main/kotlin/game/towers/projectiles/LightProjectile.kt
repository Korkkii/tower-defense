package game.towers.projectiles

import game.Enemy
import game.GameState
import game.MovementComponent
import game.MovingEntity
import game.Vector
import game.fillCircle
import game.towers.Tower
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class LightProjectile(tower: Tower, override val target: Enemy): Projectile() {
    override val velocity: Double = 400.0
    override val radius = 0.5
    private val damage = 0.5
    override val movementComponent = ProjectileMovementComponent()
    override var position: Vector = tower.square.center

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (hasHit) target.takeDamage(damage)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = if (hasHit) Color.CRIMSON else Color.WHITE
        graphics.fillCircle(Circle(position.x, position.y, radius))
    }
}
