package game.towers

import game.GameState
import game.NewProjectile
import game.center
import game.enemies.Enemy
import game.towers.projectiles.Projectile
import game.towers.projectiles.ProjectileProperties
import game.towers.projectiles.ProjectileType

fun onShootSingleTarget(
    entity: Tower,
    enemiesWithinRange: List<Enemy>,
    projectileType: ProjectileType,
    properties: ProjectileProperties
) {
    val closestEnemy = getClosestEnemy(entity, enemiesWithinRange)
    GameState.notify(NewProjectile(Projectile(entity, closestEnemy, projectileType, properties)))
}

private fun getClosestEnemy(entity: Tower, enemiesWithinRange: List<Enemy>): Enemy {
    return enemiesWithinRange.reduce { currentClosest, nextEnemy ->
        if (distanceToEnemy(entity, currentClosest) > distanceToEnemy(entity, nextEnemy)) nextEnemy
        else currentClosest
    }
}

private fun distanceToEnemy(tower: Tower, enemy: Enemy): Double =
    (tower.rangeCircle.center() - enemy.position).length

fun onShootMultiTarget(
    entity: Tower,
    enemiesWithinRange: List<Enemy>,
    projectileType: ProjectileType,
    properties: ProjectileProperties
) {
    enemiesWithinRange.forEach {
        val projectile = Projectile(
            entity,
            it,
            projectileType,
            properties
        )

        GameState.notify(NewProjectile(projectile))
    }
}
