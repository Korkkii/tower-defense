package game.towers.projectiles

import game.GameState
import game.MovementComponent
import game.times

class ProjectileMovementComponent : MovementComponent<Projectile> {
    override fun update(entity: Projectile, gameState: GameState, delta: Double) {
        if (entity.hasHit) return
        val target = entity.target

        val direction = (target.position - entity.position).unitVector()
        entity.position += entity.velocity * direction * delta

        val collisionBoundary = entity.radius + target.type.radius
        val distance = (entity.position - target.position).length
        val isHit = distance < collisionBoundary
        entity.hasHit = isHit
    }
}
