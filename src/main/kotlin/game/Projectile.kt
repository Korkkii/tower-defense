package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Projectile(private val tower: Tower, private val target: Enemy): MovingEntity {
    override val velocity: Double = 100.0
    val radius = 10.0
    private val movementComponent = MovementComponent(this)
    override var position: Vector = tower.square.center
    override var targetPosition: Vector = target.position
    private var boom = false

    override fun update(currentState: GameState, delta: Double) {
        targetPosition = target.position
        movementComponent.update(currentState, delta)
        val collisionBoundary  = radius + target.radius
        val distance = (position - targetPosition).length
        val isHit = distance < collisionBoundary
        boom = isHit
    }

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = if (boom) Color.CRIMSON else Color.WHITE
        graphics.fillCircle(Circle(position.x, position.y, radius))
    }
}