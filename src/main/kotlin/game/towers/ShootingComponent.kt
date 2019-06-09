package game.towers

import game.Enemy
import game.GameState
import game.NewProjectile
import game.PhysicsComponent
import game.center
import game.towers.projectiles.Projectile
import game.towers.projectiles.ProjectileType

class ShootingComponent private constructor(val projectileType: ProjectileType, private val onShoot: (Tower, List<Enemy>, ProjectileType) -> Unit) : PhysicsComponent<Tower> {
    private var firingCooldown = 0.0

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0
        val enemiesWithinRange = enemies.filter { withinRange(entity, it) }

        if (enemiesWithinRange.isNotEmpty() && canFire) {
            onShoot(entity, enemiesWithinRange, projectileType)
            val secondsUntilNext = 1 / entity.type.fireRate
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
        fun with(type: ProjectileType): () -> PhysicsComponent<Tower> = { ShootingComponent(type, ::onShootSingleTarget) }
        fun withMultiShot(type: ProjectileType): () -> PhysicsComponent<Tower> = { ShootingComponent(type, ::onShootMultiTarget) }
    }
}

fun onShootSingleTarget(entity: Tower, enemiesWithinRange: List<Enemy>, projectileType: ProjectileType) {
    val closestEnemy = enemiesWithinRange.reduce { currentClosest, nextEnemy ->
        if (distanceToEnemy(entity, currentClosest) > distanceToEnemy(entity, nextEnemy)) nextEnemy
        else currentClosest
    }
    GameState.notify(NewProjectile(Projectile(entity.square.center, closestEnemy, projectileType)))
}

fun onShootMultiTarget(entity: Tower, enemiesWithinRange: List<Enemy>, projectileType: ProjectileType) {
    enemiesWithinRange.forEach { GameState.notify(NewProjectile(Projectile(entity.square.center, it, projectileType))) }
}

private fun distanceToEnemy(tower: Tower, enemy: Enemy): Double =
    (tower.rangeCircle.center() - enemy.position).length
