package game.towers

import game.GameState
import game.withinRangeOf

class AreaEffectComponent : PhysicsComponent<AreaEffectTower> {
    private var firingCooldown = 0.0

    override fun update(entity: AreaEffectTower, currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0

        if (canFire) {
            enemies.withinRangeOf(entity).forEach { it.takeDamage(entity.areaEffectDamage) }
        }
    }
}
