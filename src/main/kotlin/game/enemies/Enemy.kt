package game.enemies

import game.AddEntity
import game.DamageBoost
import game.DamageTakenChange
import game.EnemyDefeated
import game.EnemyTakeDamageEvent
import game.ExplosionDebuff
import game.GameEntity
import game.GameState
import game.StatusEffects
import game.Vector
import game.board.PathSquare
import game.circle
import game.fillCircle
import game.find
import game.towers.projectiles.AttackProperty
import game.towers.projectiles.CritProperty
import game.towers.projectiles.ShootingTowerProperty
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(
    private val path: List<PathSquare>,
    val type: EnemyInfo,
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

    fun collisionCircle() = circle(position, type.radius)

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
        attackProperties: List<AttackProperty> = listOf()
    ) {
        val totalDamage = calculateDamage(initialDamage, attackProperties, statusEffects)
        health -= totalDamage

        type.onDamage(this, damageType)
        val isCrit = attackProperties.find(CritProperty::class) != null
        val damageEvent = EnemyTakeDamageEvent(this, totalDamage, isCrit)
        GameState.notify(damageEvent)

        if (health <= 0.0) {
            canBeDeleted = true
            val explosionDebuff = statusEffects.find(ExplosionDebuff::class)
            explosionDebuff?.let {
                val blastEffect = ExplosionEffect(position, 25.0, it)
                GameState.notify(AddEntity(blastEffect))
            }
            GameState.notify(EnemyDefeated(this))
        }
    }
}

private fun calculateDamage(
    initialDamage: Double,
    attackProperties: List<AttackProperty>,
    statusEffects: StatusEffects<Enemy>
): Double {
    val towerStatuses = attackProperties.find(ShootingTowerProperty::class)?.statusEffect
    val critModifier = attackProperties.find(CritProperty::class)?.damageModifier ?: 1.0
    val towerDamageModifiers = towerStatuses?.find(DamageBoost::class)?.boostPercentage ?: 0.0
    val damageTakenModifier = statusEffects.find(DamageTakenChange::class)?.damageScaling ?: 1.0
    return initialDamage * (1 + towerDamageModifiers) * critModifier * damageTakenModifier
}

private fun calculateStartingPosition(path: List<PathSquare>): Vector {
    val start = path[0]
    val centerX = start.x + 0.5 * start.width
    val centerY = start.y + 0.5 * start.height
    return Vector(centerX, centerY)
}
