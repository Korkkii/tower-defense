package game.towers

import game.Enemy
import game.GameState
import game.NewProjectile
import game.PhysicsComponent
import game.StunDebuff
import game.center
import game.towers.projectiles.Projectile
import game.towers.projectiles.ProjectileProperties
import game.towers.projectiles.ProjectileType
import kotlin.math.min

open class ShootingComponent protected constructor(
    private val projectileType: ProjectileType,
    private val onShoot: (Tower, List<Enemy>, ProjectileType, ProjectileProperties) -> Unit
) : PhysicsComponent<Tower> {
    protected var firingCooldown = 0.0

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        firingCooldown -= delta

        if (entity.statusEffects.currentEffects.any { it is StunDebuff }) return

        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        val canFire = firingCooldown <= 0.0
        val enemiesWithinRange = enemies.filter { withinRange(entity, it) }

        if (enemiesWithinRange.isNotEmpty() && canFire) {
            val properties = projectileType.propertiesConstructor(enemiesWithinRange)
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
            onShoot: (Tower, List<Enemy>, ProjectileType, ProjectileProperties) -> Unit = ::onShootSingleTarget
        ): () -> PhysicsComponent<Tower> =
            { ShootingComponent(type, onShoot) }
    }
}

class AcceleratingShootingComponent constructor(
    projectileType: ProjectileType,
    onShoot: (Tower, List<Enemy>, ProjectileType, ProjectileProperties) -> Unit
) : ShootingComponent(projectileType, onShoot) {
    private var timerCooldown = 2.0
    private var acceleratedAttackTimer = timerCooldown

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        super.update(entity, currentState, delta)

        acceleratedAttackTimer -= delta
        val didFire = firingCooldown == (1 / entity.fireRate)
        if (didFire) {
            acceleratedAttackTimer = timerCooldown
            val acceleratedFireRate = min(entity.fireRate * 1.2, 5.0)
            entity.fireRate = acceleratedFireRate
        } else if (acceleratedAttackTimer <= 0.0) {
            entity.fireRate = entity.type.baseFireRate
        }
    }
}

fun onShootSingleTarget(
    entity: Tower,
    enemiesWithinRange: List<Enemy>,
    projectileType: ProjectileType,
    properties: ProjectileProperties
) {
    val closestEnemy = getClosestEnemy(entity, enemiesWithinRange)
    GameState.notify(NewProjectile(Projectile(entity.square.center, closestEnemy, projectileType, properties)))
}

private fun getClosestEnemy(entity: Tower, enemiesWithinRange: List<Enemy>): Enemy {
    return enemiesWithinRange.reduce { currentClosest, nextEnemy ->
        if (distanceToEnemy(entity, currentClosest) > distanceToEnemy(entity, nextEnemy)) nextEnemy
        else currentClosest
    }
}

fun onShootMultiTarget(
    entity: Tower,
    enemiesWithinRange: List<Enemy>,
    projectileType: ProjectileType,
    properties: ProjectileProperties
) {
    enemiesWithinRange.forEach {
        GameState.notify(
            NewProjectile(
                Projectile(
                    entity.square.center,
                    it,
                    projectileType,
                    properties
                )
            )
        )
    }
}

private fun distanceToEnemy(tower: Tower, enemy: Enemy): Double =
    (tower.rangeCircle.center() - enemy.position).length
