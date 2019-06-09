package game.towers.projectiles

import game.Enemy
import game.GameState
import game.times
import game.PhysicsComponent

class ProjectilePhysicsComponent(private val onHit: (Projectile, Enemy, GameState) -> Unit) :
    PhysicsComponent<Projectile> {
    override fun update(entity: Projectile, currentState: GameState, delta: Double) {
        if (entity.hasHit) return
        val target = entity.target

        val direction = (target.position - entity.position).unitVector()
        entity.position += entity.type.velocity * direction * delta

        val collisionBoundary = entity.type.radius + target.type.radius
        val distance = (entity.position - target.position).length
        val isHit = distance < collisionBoundary
        entity.hasHit = isHit

        if (isHit) onHit(entity, entity.target, currentState)
    }
}
