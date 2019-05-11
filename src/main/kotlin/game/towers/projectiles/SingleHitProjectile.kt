package game.towers.projectiles

import game.Enemy
import game.GameState
import game.Vector
import game.towers.Tower

class SingleHitProjectile(tower: Tower, override val target: Enemy) : Projectile() {
    override val velocity: Double = 100.0
    override val radius = 2.0
    private val damage = 3.0
    override val movementComponent = ProjectileMovementComponent()
    override var position: Vector = tower.square.center

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (hasHit) target.takeDamage(damage)
    }
}
