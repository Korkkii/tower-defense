package game.towers.projectiles

import game.Circle
import game.Enemy
import game.GameState
import game.Vector
import game.contains
import game.towers.Tower

class SplashProjectile(tower: Tower, override val target: Enemy) : Projectile() {
    override val velocity: Double = 100.0
    override val radius = 2.0
    private val damage = 3.0
    private val splashRange = 15.0
    override val movementComponent = ProjectileMovementComponent()
    override var position: Vector = tower.square.center

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (hasHit) {
            val splashedEnemies = enemiesWithinSplashRange(currentState.enemies, position)
            splashedEnemies.forEach { it.takeDamage(damage) }
        }
    }

    private fun enemiesWithinSplashRange(enemies: List<Enemy>, position: Vector): List<Enemy> {
        val splashCircle = Circle(position, splashRange)
        return enemies.filter { splashCircle.contains(it.position) }
    }
}
