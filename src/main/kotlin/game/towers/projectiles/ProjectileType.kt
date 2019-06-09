package game.towers.projectiles

import game.Circle
import game.Enemy
import game.GameState
import game.Vector
import game.contains

class ProjectileType(
    val radius: Double,
    val velocity: Double,
    onHit: (Projectile, Enemy, GameState) -> Unit
) {
    val physicsComponent = ProjectilePhysicsComponent(onHit)

    companion object {
        val singleHitProjectile = ProjectileType(2.0, 100.0, onSingleHit(3.0))
        val singleHit2Projectile = ProjectileType(2.0, 100.0, onSingleHit(5.0))
        val splashProjectile = ProjectileType(2.0, 100.0, onSplashHit(3.0, 15.0))
        val lightProjectile = ProjectileType(0.5, 400.0, onSingleHit(0.5))
    }
}

fun onSingleHit(damage: Double) = { _: Projectile, target: Enemy, _: GameState ->
    target.takeDamage(damage)
}

fun onSplashHit(damage: Double, splashRange: Double) = { projectile: Projectile, target: Enemy, currentState: GameState ->
    val splashedEnemies = enemiesWithinSplashRange(currentState.enemies, projectile.position, splashRange)
    splashedEnemies.forEach { it.takeDamage(damage) }
}

private fun enemiesWithinSplashRange(enemies: List<Enemy>, position: Vector, splashRange: Double): List<Enemy> {
    val splashCircle = Circle(position, splashRange)
    return enemies.filter { splashCircle.contains(it.position) }
}
