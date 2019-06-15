package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

fun calculateStartingPosition(path: List<PathSquare>): Vector {
    val start = path[0]
    val centerX = start.x + 0.5 * start.width
    val centerY = start.y + 0.5 * start.height
    return Vector(centerX, centerY)
}

class Enemy(private val path: List<PathSquare>, val type: EnemyType, level: Int) :
    GameEntity(calculateStartingPosition(path)) {
    var target: PathSquare
        private set
    val maxHealth = type.baseHealth + type.healthPerLevel * level
    var health = maxHealth
        private set
    var canBeDeleted = false
        private set
    private val movementComponent = type.movementComponentConstructor()
    val statusEffects = mutableListOf<StatusEffect<Enemy>>()

    init {
        target = path[1]
        type.actions.onCreate(this)
    }

    private fun waypointCollisionCircle() = Circle(position.x, position.y, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (waypointCollisionCircle().intersects(target.waypoint.boundsInLocal)) {
            nextTarget()
        }

        statusEffects.forEach { it.update(this, currentState, delta) }
        statusEffects.removeAll { it.isOver() }
    }

    private fun nextTarget() {
        val nextIndex = path.indexOf(target) + 1
        val nextTarget = path.getOrNull(nextIndex) ?: path[0]

        target = nextTarget
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = type.color
        graphics.fillCircle(Circle(position.x, position.y, type.radius))

        val healthBarWidth = 20
        val healthBarHeight = 3
        graphics.fill = Color.RED
        graphics.fillRect(
            position.x - 0.5 * healthBarWidth,
            position.y - 3 * healthBarHeight,
            healthBarWidth.toDouble(),
            healthBarHeight.toDouble()
        )

        val healthRemainingWidth = health / maxHealth * healthBarWidth
        graphics.fill = Color.GREEN
        graphics.fillRect(
            position.x - 0.5 * healthBarWidth,
            position.y - 3 * healthBarHeight,
            healthRemainingWidth,
            healthBarHeight.toDouble()
        )
    }

    fun takeDamage(damage: Double) {
        health -= damage
        type.actions.onDamage(this)

        if (health <= 0.0) {
            canBeDeleted = true
            GameState.notify(EnemyDefeated(this))
        }
    }
}

abstract class StatusEffect<T : GameEntity>(private var duration: Double) {
    fun update(entity: T, currentState: GameState, delta: Double) {
        duration -= delta

        onUpdate(entity, currentState, delta)
    }

    fun isOver() = duration <= 0.0

    open fun onUpdate(entity: T, currentState: GameState, delta: Double) {}
}
class DamageOverTime(private val damagePerSecond: Double, duration: Double) : StatusEffect<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val damage = damagePerSecond * delta
        entity.takeDamage(damage)
    }
}

class SpeedBuff(val speedScaling: Double, duration: Double) : StatusEffect<Enemy>(duration)

class RegenBuff(private val healthPerSecond: Double, duration: Double) : StatusEffect<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val regenAmount = healthPerSecond * delta
        entity.takeDamage(-regenAmount)
    }
}
