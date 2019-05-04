package game.towers.projectiles

import game.MovingEntity

abstract class Projectile : MovingEntity {
    var hasHit = false
        protected set

    fun canDelete(): Boolean = hasHit
}
