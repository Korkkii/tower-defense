package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val path: List<PathSquare>, val type: EnemyType) : GameEntity {
    var target: PathSquare
        private set
    var health = type.maxHealth
        private set
    var position: Vector
    var canBeDeleted = false
        private set

    init {
        val start = path[0]
        target = path[1]

        val centerX = start.x + 0.5 * start.width
        val centerY = start.y + 0.5 * start.height
        position = Vector(centerX, centerY)
    }

    private fun waypointCollisionCircle() = Circle(position.x, position.y, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        type.movementComponent.update(this, currentState, delta)

        if (waypointCollisionCircle().intersects(target.waypoint.boundsInLocal)) {
            nextTarget()
        }
    }

    private fun nextTarget() {
        val nextIndex = path.indexOf(target) + 1
        val nextTarget = path.getOrNull(nextIndex)
        val hasNextTarget = nextTarget != null

        if (hasNextTarget) {
            nextTarget?.let {
                target = it
            }
        } else {
            target = path[0]
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.RED
        graphics.fillCircle(Circle(position.x, position.y, type.radius))

        val healthBarWidth = 20
        val healthBarHeight = 3
        graphics.fill = Color.RED
        graphics.fillRect(position.x - 0.5 * healthBarWidth, position.y - 3 * healthBarHeight, healthBarWidth.toDouble(), healthBarHeight.toDouble())

        val healthRemainingWidth = health / type.maxHealth * healthBarWidth
        graphics.fill = Color.GREEN
        graphics.fillRect(position.x - 0.5 * healthBarWidth, position.y - 3 * healthBarHeight, healthRemainingWidth, healthBarHeight.toDouble())
    }

    fun takeDamage(damage: Double) {
        health -= damage

        if (health <= 0.0) {
            canBeDeleted = true
            GameState.notify(EnemyDefeated(this))
        }
    }
}
