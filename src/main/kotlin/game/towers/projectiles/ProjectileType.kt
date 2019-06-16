package game.towers.projectiles

import game.Circle
import game.DamageOverTime
import game.Enemy
import game.GameState
import game.GraphicsComponent
import game.NewProjectile
import game.Vector
import game.contains
import kotlin.math.pow

class ProjectileType(
    val radius: Double,
    val velocity: Double,
    val graphicsComponent: GraphicsComponent<Projectile>,
    onHit: (Projectile, Enemy, GameState) -> Unit,
    internal val propertyConstructor: (List<Enemy>) -> ProjectileProperty = { NoProperty }
) {
    val physicsComponent = ProjectilePhysicsComponent(onHit)

    companion object {
        val singleHitProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSingleHit(3.0))
        val singleHit2Projectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSingleHit(5.0))
        val splashProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onSplashHit(3.0, 25.0))
        val lightProjectile = ProjectileType(0.5, 300.0, StraightLineGraphicsComponent(), onSingleHit(2.0))
        val bounceProjectile = ProjectileType(1.3, 200.0, ProjectileGraphicsComponent(), onBounceHit(2.5, 25.0, 3))
        val applyDoTProjectile = ProjectileType(2.0, 100.0, ProjectileGraphicsComponent(), onDoTHit(2.0, 5.0))
        val damagePerCreepProjectile = ProjectileType(
            2.0,
            100.0,
            ProjectileGraphicsComponent(),
            onScalingDamageHit(3.0, 2.0),
            { IncreasedDamageProperty(it.size) })
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
        val bounceProperty = projectile.properties.find(BounceProperty::class)
        val bouncesLeft = bounceProperty?.bouncesLeft ?: initialBounceAmount

        if (bouncesLeft <= 0) return@onHit

        val bouncableEnemies =
            enemiesWithinRange(currentState.enemies, projectile.position, bounceRange).filter { it != target }
        val nextTarget = if (bouncableEnemies.isNotEmpty()) bouncableEnemies.random() else return@onHit
        val properties = ProjectileProperties(BounceProperty(bouncesLeft - 1))
        val bounceProjectile = Projectile(
            projectile,
            nextTarget,
            projectile.type,
            properties
        )

        GameState.notify(NewProjectile(bounceProjectile))
    }

fun onDoTHit(initialDamage: Double, damagePerStack: Double) = { _: Projectile, target: Enemy, _: GameState ->
    target.takeDamage(initialDamage)
    target.statusEffects += DamageOverTime(damagePerStack, 5.0)
}

fun onScalingDamageHit(initialDamage: Double, scaling: Double) =
    { projectile: Projectile, target: Enemy, _: GameState ->
        val damageProperty = projectile.properties.find(IncreasedDamageProperty::class)
        val modifier = damageProperty?.enemyCount ?: 1
        val damage = initialDamage * scaling.pow(modifier)
        target.takeDamage(damage)
    }

private fun enemiesWithinRange(enemies: List<Enemy>, position: Vector, range: Double): List<Enemy> {
    val rangeCircle = Circle(position, range)
    return enemies.filter { rangeCircle.contains(it.position) && !it.canBeDeleted }
}
