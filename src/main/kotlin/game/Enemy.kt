package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val gameBoard: GameBoard) : MovingEntity {
    private val movementComponent = MovementComponent(this)
    private var target: PathSquare
    override lateinit var targetPosition: Vector
    val radius = 10.0
    var health = 10.0
    override val velocity = 35.0
    override lateinit var position: Vector

    init {
        val path = gameBoard.path
        val start = path[0]
        target = path[1]
        targetPosition = target.waypoint.center()
        val centerX = start.x + 0.5 * start.width
        val centerY = start.y + 0.5 * start.height
        position = Vector(centerX, centerY)
    }

    private fun waypointCollisionCircle() = Circle(position.x, position.y, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(currentState, delta)

        if (waypointCollisionCircle().intersects(target.waypoint.boundsInLocal)) {
            nextTarget()
        }
    }

    private fun nextTarget() {
        val path = gameBoard.path
        val nextIndex = path.indexOf(target) + 1
        val nextTarget = path.getOrNull(nextIndex)

        nextTarget?.let {
            target = it
            targetPosition = target.waypoint.center()
        }
    }

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = Color.RED
        graphics.fillCircle(Circle(position.x, position.y, radius))
    }
}