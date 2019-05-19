package game.towers.projectiles

import game.GameState
import game.MovementComponent
import game.times

class ProjectileMovementComponent : MovementComponent<Projectile> {
    override fun update(projectile: Projectile, gameState: GameState, delta: Double) {
        if (projectile.hasHit) return
        val target = projectile.target

        val direction = (target.position - projectile.position).unitVector()
        projectile.position += projectile.velocity * direction * delta

        val collisionBoundary = projectile.radius + target.type.radius
        val distance = (projectile.position - target.position).length
        val isHit = distance < collisionBoundary
        projectile.hasHit = isHit
    }
}
