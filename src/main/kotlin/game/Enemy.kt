package game

import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val gameBoard: GameBoard) : Circle(10.0), UpdatableEntity {
    private lateinit var target: PathSquare
    private val velocity = 0.5

    init {
        val path = gameBoard.path
        val start = path[0]
        target = path[1]
        this.centerX = start.x + 0.5 * start.width
        this.centerY = start.y + 0.5 * start.height
        this.fill = Color.RED
    }

    fun waypointCollisionCircle() = Circle(centerX, centerY, 1.4)

    override fun update() {
        val waypoint = target.waypoint
        val direction = (waypoint.center() - this.center()).unitVector()
        centerX += velocity * direction.x
        centerY += velocity * direction.y

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
}