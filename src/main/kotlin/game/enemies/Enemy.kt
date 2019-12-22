package game.enemies

import game.DamageBoost
import game.EnemyDefeated
import game.EnemyTakeDamageEvent
import game.GameEntity
import game.GameState
import game.StatusEffects
import game.Vector
import game.board.PathSquare
import game.fillCircle
import game.find
import game.towers.projectiles.ProjectileProperty
import game.towers.projectiles.ShootingTowerProperty
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(
    private val path: List<PathSquare>,
    val type: EnemyType,
    level: Int,
    healthPercent: Double = 1.0,
    currentTarget: PathSquare? = null,
    currentPosition: Vector? = null
) :
    GameEntity(currentPosition ?: calculateStartingPosition(path)) {
    var target: PathSquare
        private set
    val maxHealth = type.baseHealth + type.healthPerLevel * level
    var health = maxHealth * healthPercent
        private set
    var canBeDeleted = false
        private set
    private val physicsComponent = type.physicsComponentConstructor()
    val statusEffects = StatusEffects<Enemy>()

    init {
        target = currentTarget ?: path[1]
        type.onCreate(this)
    }

    private fun waypointCollisionCircle() = Circle(position.x, position.y, 1.4)

    override fun update(currentState: GameState, delta: Double) {
        physicsComponent.update(this, currentState, delta)

        if (waypointCollisionCircle().intersects(target.waypoint.boundsInLocal)) {
            nextTarget()
        }

        statusEffects.update(this, currentState, delta)
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

    fun takeDamage(
        initialDamage: Double,
        damageType: DamageType = SingleHitDamage,
        projectileProperties: List<ProjectileProperty> = listOf()
    ) {
        val totalDamage = calculateDamage(initialDamage, projectileProperties)
        health -= totalDamage

        type.onDamage(this, damageType)
        GameState.notify(EnemyTakeDamageEvent(this, totalDamage))

        if (health <= 0.0) {
            canBeDeleted = true
            GameState.notify(EnemyDefeated(this))
        }
    }
}

private fun calculateDamage(initialDamage: Double, projectileProperties: List<ProjectileProperty>): Double {
    val towerStatuses = projectileProperties.find(ShootingTowerProperty::class)?.statusEffect
    val modifiers = towerStatuses?.find(DamageBoost::class)?.boostPercentage ?: 0.0
    return initialDamage * (1 + modifiers)
}

private fun calculateStartingPosition(path: List<PathSquare>): Vector {
    val start = path[0]
    val centerX = start.x + 0.5 * start.width
    val centerY = start.y + 0.5 * start.height
    return Vector(centerX, centerY)
}
