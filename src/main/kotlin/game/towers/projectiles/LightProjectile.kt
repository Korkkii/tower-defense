package game.towers.projectiles

import game.Enemy
import game.GameState
import game.Vector
import game.towers.Tower

class LightProjectile(tower: Tower, override val target: Enemy) : Projectile() {
    override val velocity: Double = 400.0
    override val radius = 0.5
    private val damage = 0.5
    override val movementComponent = ProjectileMovementComponent()
    override var position: Vector = tower.square.center

    override fun update(currentState: GameState, delta: Double) {
        movementComponent.update(this, currentState, delta)

        if (hasHit) target.takeDamage(damage)
    }
}
