package game.towers

import game.BlindDebuff
import game.enemies.Enemy
import game.GameState
import game.NewProjectile
import game.PhysicsComponent
import game.StunDebuff
import game.center
import game.towers.projectiles.BlindedProperty
import game.towers.projectiles.Projectile
import game.towers.projectiles.ProjectileProperties
import game.towers.projectiles.ProjectileType
import kotlin.math.min

typealias OnShootFunction = (Tower, List<Enemy>, ProjectileType, ProjectileProperties) -> Unit

class ShootingComponent (
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
            val properties = ProjectileProperties(projectileProperty, blindProperty)

            onShoot(entity, enemiesWithinRange, projectileType, properties)

            val secondsUntilNext = 1 / entity.fireRate
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

class AcceleratingShootingComponent constructor(
    projectileType: ProjectileType,
    onShoot: (Tower, List<Enemy>, ProjectileType, ProjectileProperties) -> Unit
) : PhysicsComponent<Tower> {
    private var shootingComponent = ShootingComponent(projectileType, onShoot)
    private var timerCooldown = 2.0
    private var acceleratedAttackTimer = timerCooldown

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        shootingComponent.update(entity, currentState, delta)

        acceleratedAttackTimer -= delta
        val didFire = shootingComponent.firingCooldown == (1 / entity.fireRate)
        if (didFire) {
            acceleratedAttackTimer = timerCooldown
            val acceleratedFireRate = min(entity.fireRate * 1.2, 5.0)
            entity.fireRate = acceleratedFireRate
        } else if (acceleratedAttackTimer <= 0.0) {
            entity.fireRate = entity.type.baseFireRate
        }
    }
}
