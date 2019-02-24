package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Projectile(private val tower: Tower, private val target: Enemy): MovingEntity {
    override val velocity: Double = 200.0
    private val radius = 2.0
    private val damage = 3.0
    private val movementComponent = MovementComponent(this)
    override var position: Vector = tower.square.center
    override var targetPosition: Vector = target.position
    private var hasHit = false

    override fun update(currentState: GameState, delta: Double) {
        if (hasHit) return
        targetPosition = target.position
        movementComponent.update(currentState, delta)
        val collisionBoundary  = radius + target.radius
        val distance = (position - targetPosition).length
        val isHit = distance < collisionBoundary
        hasHit = isHit

        if (isHit) {
            target.health -= damage
        }
    }

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = if (hasHit) Color.CRIMSON else Color.WHITE
        graphics.fillCircle(Circle(position.x, position.y, radius))
    }

    fun canDelete(): Boolean = hasHit
}