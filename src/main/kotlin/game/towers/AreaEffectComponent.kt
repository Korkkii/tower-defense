package game.towers

import game.AttackSpeedBoost
import game.GameState
import game.PhysicsComponent
import game.towers.projectiles.ShootingTowerProperty
import game.withinRangeOf

class AreaEffectComponent(areaEffectDamagePerSecond: Double, private val ticksPerSecond: Double) :
    PhysicsComponent<Tower> {
    private var firingCooldown = 1 / ticksPerSecond
    private val damagePerTick = areaEffectDamagePerSecond / ticksPerSecond

    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0

        if (canFire) {
            val towerProperty = ShootingTowerProperty(entity.statusEffects.snapshot())
            enemies.withinRangeOf(entity)
                .forEach { it.takeDamage(damagePerTick, projectileProperties = listOf(towerProperty)) }
            val attackSpeedBoost = entity.statusEffects.find(AttackSpeedBoost::class)?.boostPercentage ?: 0.0
            firingCooldown = 1 / (ticksPerSecond * (1 + attackSpeedBoost))
        }
    }
}
