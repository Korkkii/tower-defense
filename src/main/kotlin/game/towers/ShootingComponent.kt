package game.towers

import game.AttackSpeedBoost
import game.BlindDebuff
import game.GameState
import game.PhysicsComponent
import game.StunDebuff
import game.center
import game.enemies.Enemy
import game.towers.projectiles.BlindedProperty
import game.towers.projectiles.ProjectileProperty
import game.towers.projectiles.ProjectileType
import game.towers.projectiles.ShootingTowerProperty
import kotlin.math.min

typealias OnShootFunction = (Tower, List<Enemy>, ProjectileType, List<ProjectileProperty?>) -> Unit

class ShootingComponent(
    private val projectileType: ProjectileType,
    private val onShoot: OnShootFunction
) : PhysicsComponent<Tower> {
    var firingCooldown = 0.0
        private set

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        firingCooldown -= delta

        if (entity.statusEffects.has(StunDebuff::class)) return

        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        val canFire = firingCooldown <= 0.0
        val enemiesWithinRange = enemies.filter { withinRange(entity, it) }

        if (enemiesWithinRange.isNotEmpty() && canFire) {
            val projectileProperty = projectileType.propertyConstructor(enemiesWithinRange)
            val blindProperty = if (entity.statusEffects.has(BlindDebuff::class)) BlindedProperty(0.3) else null
            val towerProperty = ShootingTowerProperty(entity.statusEffects.snapshot())
            val properties = listOf(projectileProperty, blindProperty, towerProperty)

            onShoot(entity, enemiesWithinRange, projectileType, properties)

            val secondsUntilNext = 1 / calculateFireRate(entity.fireRate, entity)
            firingCooldown = secondsUntilNext
        }
    }

    private fun withinRange(tower: Tower, enemy: Enemy): Boolean {
        val rangeCircle = tower.rangeCircle
        val collisionBoundary = rangeCircle.radius + enemy.type.radius
        val distance = (rangeCircle.center() - enemy.position).length
        return distance < collisionBoundary
    }

    companion object {
        fun with(
            type: ProjectileType,
            onShoot: OnShootFunction = ::onShootSingleTarget
        ): () -> PhysicsComponent<Tower> =
            { ShootingComponent(type, onShoot) }
    }
}

fun calculateFireRate(fireRate: Double, entity: Tower): Double {
    val attackSpeedModifier = entity.statusEffects.find(AttackSpeedBoost::class)?.boostPercentage ?: 0.0
    return fireRate + attackSpeedModifier
}

class AcceleratingShootingComponent constructor(
    projectileType: ProjectileType,
    private val onShoot: OnShootFunction
) : PhysicsComponent<Tower> {
    private val shootingComponent = ShootingComponent(projectileType, ::acceleratingOnShoot)
    private val timerCooldown = 2.0
    private var acceleratedAttackResetTimer = timerCooldown
    private val maxAcceleratedFireRate = 5.0

    // TODO: Convert to use attack speed boost status effect instead of modifying fire rate var? ---> less mutable
    private fun acceleratingOnShoot(
        entity: Tower,
        enemiesWithinRange: List<Enemy>,
        projectileType: ProjectileType,
        properties: List<ProjectileProperty?>
    ) {
        onShoot(entity, enemiesWithinRange, projectileType, properties)

        acceleratedAttackResetTimer = timerCooldown
        val maxFireRate = calculateFireRate(maxAcceleratedFireRate, entity)
        val acceleratedFireRate = min(entity.fireRate * 1.2, maxFireRate)
        entity.fireRate = acceleratedFireRate
    }

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        shootingComponent.update(entity, currentState, delta)

        acceleratedAttackResetTimer -= delta

        if (acceleratedAttackResetTimer <= 0.0) {
            val resettedFireRate = calculateFireRate(entity.type.baseFireRate, entity)
            entity.fireRate = resettedFireRate
        }
    }
}
