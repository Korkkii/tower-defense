package game.towers.projectiles

import game.Enemy
import game.MovingEntity

abstract class Projectile : MovingEntity {
    abstract val target: Enemy
    abstract val radius: Double
    var hasHit = false

    fun canDelete(): Boolean = hasHit
}
