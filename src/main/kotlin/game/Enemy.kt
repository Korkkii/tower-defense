package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val gameBoard: GameBoard) : Circle(10.0), GameEntity {
    private var target: PathSquare
    private val velocity = 35

    init {
        val path = gameBoard.path
        val start = path[0]
        target = path[1]
        centerX = start.x + 0.5 * start.width
        centerY = start.y + 0.5 * start.height
    }

    private fun waypointCollisionCircle() = Circle(centerX, centerY, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        val waypoint = target.waypoint
        val direction = (waypoint.center() - this.center()).unitVector()
        centerX += velocity * direction.x * delta
        centerY += velocity * direction.y * delta

        if (waypointCollisionCircle().intersects(waypoint.boundsInLocal)) {
            nextTarget()
        }
    }

    private fun nextTarget() {
        val path = gameBoard.path
        val nextIndex = path.indexOf(target) + 1
        val nextTarget = path.getOrNull(nextIndex)

        nextTarget?.let { target = it }
    }

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = Color.RED
        graphics.fillCircle(this)
    }
}