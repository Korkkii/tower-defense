package game.towers.projectiles

import game.DamageOverTime
import game.GameState
import game.NewProjectile
import game.Vector
import game.circle
import game.contains
import game.enemies.Enemy
import game.find
import javafx.scene.canvas.GraphicsContext
import kotlin.math.pow
import kotlin.random.Random

typealias GraphicsFunction = (Projectile, GraphicsContext, GameState) -> Unit

class ProjectileType(
    val radius: Double,
    val velocity: Double,
    val drawGraphics: GraphicsFunction,
    onHit: (Projectile, Enemy, GameState) -> Unit,
    internal val propertyConstructor: (List<Enemy>) -> AttackProperty = { NoProperty }
) {
    val physicsComponent = ProjectilePhysicsComponent(onHit)

    companion object {
        val singleHitProjectile = ProjectileType(2.0, 100.0, ::drawProjectile, onSingleHit(3.0))
        val splashProjectile = ProjectileType(2.0, 100.0, ::drawProjectile, onSplashHit(3.0, 25.0))
        val lightProjectile = ProjectileType(0.5, 300.0, ::drawStraightLine, onSingleHit(2.0))
        val bounceProjectile = ProjectileType(1.3, 200.0, ::drawProjectile, onBounceHit(2.5, 25.0, 3))
        val applyDoTProjectile = ProjectileType(2.0, 100.0, ::drawProjectile, onDoTHit(2.0, 5.0, 8.0))
        val damagePerCreepProjectile = ProjectileType(
            2.0,
            100.0,
            ::drawProjectile,
            onScalingDamageHit(3.0, 2.0),
            { IncreasedDamageProperty(it.size) })
        val critProjectile = ProjectileType(
            2.0,
            100.0,
            ::drawProjectile,
            onCritProjectileHit(3.0, 0.2)
        )
        val missingHpProjectile = ProjectileType(
            2.0,
            100.0,
            ::drawProjectile,
            onEnemyMissingHpScaledHit(3.0, 2.0)
        )
    }
}

fun onSingleHit(damage: Double) = { projectile: Projectile, target: Enemy, _: GameState ->
    target.takeDamage(damage, attackProperties = projectile.properties)
}

fun onSplashHit(damage: Double, splashRange: Double) = { projectile: Projectile, _: Enemy, currentState: GameState ->
    val splashedEnemies = enemiesWithinRange(currentState.enemies, projectile.position, splashRange)
    splashedEnemies.forEach { it.takeDamage(damage, attackProperties = projectile.properties) }
}

fun onBounceHit(damage: Double, bounceRange: Double, initialBounceAmount: Int) =
    onHit@{ projectile: Projectile, target: Enemy, currentState: GameState ->
        target.takeDamage(damage, attackProperties = projectile.properties)
        val bounceProperty = projectile.properties.find(BounceProperty::class)
        val bouncesLeft = bounceProperty?.bouncesLeft ?: initialBounceAmount

        if (bouncesLeft <= 0) return@onHit

        val bouncableEnemies =
            enemiesWithinRange(currentState.enemies, projectile.position, bounceRange).filter { it != target }
        val nextTarget = if (bouncableEnemies.isNotEmpty()) bouncableEnemies.random() else return@onHit
        val originalShooterTowerProperty = projectile.properties.find(ShootingTowerProperty::class)
        val properties = listOf(BounceProperty(bouncesLeft - 1), originalShooterTowerProperty)
        val bounceProjectile = Projectile(
            projectile,
            nextTarget,
            projectile.type,
            properties
        )

        GameState.notify(NewProjectile(bounceProjectile))
    }

fun onDoTHit(initialDamage: Double, damagePerSecond: Double, ticksPerSecond: Double) =
    { projectile: Projectile, target: Enemy, _: GameState ->
        target.takeDamage(initialDamage, attackProperties = projectile.properties)
        target.statusEffects += DamageOverTime(damagePerSecond, 5.0, ticksPerSecond)
    }

fun onScalingDamageHit(initialDamage: Double, scaling: Double) =
    { projectile: Projectile, target: Enemy, _: GameState ->
        val damageProperty = projectile.properties.find(IncreasedDamageProperty::class)
        val modifier = damageProperty?.enemyCount ?: 1
        val damage = initialDamage * scaling.pow(modifier)
        target.takeDamage(damage, attackProperties = projectile.properties)
    }

fun onCritProjectileHit(baseDamage: Double, critChance: Double) =
    { projectile: Projectile, target: Enemy, _: GameState ->
        val isCrit = Random.Default.nextDouble() < critChance
        val critMultiplier = if (isCrit) CritProperty(2.0) else null
        val properties = (projectile.properties + critMultiplier).filterNotNull()
        target.takeDamage(baseDamage, attackProperties = properties)
    }

fun onEnemyMissingHpScaledHit(baseDamage: Double, maximumScaling: Double) =
    { projectile: Projectile, target: Enemy, _: GameState ->
        val scalingModifier = target.health / target.maxHealth * maximumScaling
        val totalDamage = scalingModifier * baseDamage
        target.takeDamage(totalDamage, attackProperties = projectile.properties)
    }

private fun enemiesWithinRange(enemies: List<Enemy>, position: Vector, range: Double): List<Enemy> {
    val rangeCircle = circle(position, range)
    return enemies.filter { rangeCircle.contains(it.position) && !it.canBeDeleted }
}
