package game.towers.projectiles

import game.Circle
import game.DamageOverTime
import game.Enemy
import game.GameState
import game.GraphicsComponent
import game.NewProjectile
import game.Vector
import game.contains

class ProjectileType(
    val radius: Double,
    val velocity: Double,
    val graphicsComponent: GraphicsComponent<Projectile>,
    onHit: (Projectile, Enemy, GameState) -> Unit
) {
    val physicsComponent = ProjectilePhysicsComponent(onHit)

    companion object {
        val singleHitProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSingleHit(3.0))
        val singleHit2Projectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSingleHit(5.0))
        val splashProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSplashHit(3.0, 25.0))
        val lightProjectile = ProjectileType(0.5, 300.0, StraightLineGraphicsComponent(), onSingleHit(2.0))
        val bounceProjectile = ProjectileType(1.3, 200.0, ProjectileGraphicsComponent(), onBounceHit(2.5, 25.0, 3))
        val applyDoTProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onDoTHit(2.0, 5.0))
    }
}

fun onSingleHit(damage: Double) = { _: Projectile, target: Enemy, _: GameState ->
    target.takeDamage(damage)
}

fun onSplashHit(damage: Double, splashRange: Double) = { projectile: Projectile, _: Enemy, currentState: GameState ->
    val splashedEnemies = enemiesWithinRange(currentState.enemies, projectile.position, splashRange)
    splashedEnemies.forEach { it.takeDamage(damage) }
}

fun onBounceHit(damage: Double, bounceRange: Double, initialBounceAmount: Int) =
    onHit@{ projectile: Projectile, target: Enemy, currentState: GameState ->
        target.takeDamage(damage)

        val bouncesLeft = (projectile.properties as? BounceProperties)?.bouncesLeft ?: initialBounceAmount

        if (bouncesLeft <= 0) return@onHit

        val bouncableEnemies =
            enemiesWithinRange(currentState.enemies, projectile.position, bounceRange).filter { it != target }
        val nextTarget = if (bouncableEnemies.isNotEmpty()) bouncableEnemies.random() else return@onHit
        GameState.notify(
            NewProjectile(
                Projectile(
                    projectile.position,
                    nextTarget,
                    projectile.type,
                    BounceProperties(bouncesLeft - 1)
                )
            )
        )
    }

fun onDoTHit(initialDamage: Double, damagePerStack: Double) = { _: Projectile, target: Enemy, _: GameState ->
    target.takeDamage(initialDamage)
    target.statusEffects += DamageOverTime(damagePerStack, 5.0)
}

private fun enemiesWithinRange(enemies: List<Enemy>, position: Vector, range: Double): List<Enemy> {
    val rangeCircle = Circle(position, range)
    return enemies.filter { rangeCircle.contains(it.position) && !it.canBeDeleted }
}
