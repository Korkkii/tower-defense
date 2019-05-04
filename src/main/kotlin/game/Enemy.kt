package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(val gameBoard: GameBoard) : MovingEntity {
    override val movementComponent = EnemyMovementComponent()
    var target: PathSquare
        private set
    val radius = 10.0
    private val maxHealth = 20.0
    var health = maxHealth
    override val velocity = 35.0
    override lateinit var position: Vector
    private var deletable = false

    init {
        val path = gameBoard.path
        val start = path[0]
        target = path[1]

        val centerX = start.x + 0.5 * start.width
        val centerY = start.y + 0.5 * start.height
        position = Vector(centerX, centerY)
    }

    private fun waypointCollisionCircle() = Circle(position.x, position.y, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (waypointCollisionCircle().intersects(target.waypoint.boundsInLocal)) {
            nextTarget()
        }
    }

    private fun nextTarget() {
        val path = gameBoard.path
        val nextIndex = path.indexOf(target) + 1
        val nextTarget = path.getOrNull(nextIndex)
        val hasNextTarget = nextTarget != null

        if (hasNextTarget) {
            nextTarget?.let {
                target = it
            }
        } else {
            deletable = true
            GameState.notify(EnemyReachedEndEvent(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.RED
        graphics.fillCircle(Circle(position.x, position.y, radius))

        val healthBarWidth = 50
        val healthBarHeight = 10
        graphics.fill = Color.RED
        graphics.fillRect(position.x - 0.5 * healthBarWidth, position.y - 2 * healthBarHeight, healthBarWidth.toDouble(), healthBarHeight.toDouble())

        val healthRemainingWidth = health / maxHealth * healthBarWidth
        graphics.fill = Color.GREEN
        graphics.fillRect(position.x - 0.5 * healthBarWidth, position.y - 2 * healthBarHeight, healthRemainingWidth, healthBarHeight.toDouble())
    }

    fun canDelete(): Boolean = deletable
}
