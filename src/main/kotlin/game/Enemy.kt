package game

import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val gameBoard: GameBoard) : Circle(10.0), UpdatableEntity {
    private lateinit var target: PathSquare
    private val velocity = 0.01

    init {
        val path = gameBoard.path
        val start = path[0]
        target = path[1]
        this.centerX = start.x + 0.5 * start.width
        this.centerY = start.y + 0.5 * start.height
        this.fill = Color.RED
    }

    override fun update() {
        val waypoint = target.waypoint
        val xdiff = (waypoint.centerX - this.centerX)
        val ydiff = (waypoint.centerY - this.centerY)
        this.centerX += velocity * xdiff
        this.centerY += velocity * ydiff

        if (this.intersects(waypoint.boundsInLocal)) {
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