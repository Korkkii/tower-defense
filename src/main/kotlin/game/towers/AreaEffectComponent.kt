package game.towers

import game.AttackSpeedBoost
import game.GameState
import game.PhysicsComponent
import game.enemies.Enemy
import game.towers.projectiles.AttackProperty
import game.towers.projectiles.ShootingTowerProperty
import game.withinRangeOf

typealias OnTick = (enemy: Enemy) -> Unit

class AreaEffectComponent(
    areaEffectDamagePerSecond: Double,
    private val ticksPerSecond: Double,
    private val onTick: OnTick = { _: Enemy -> }
) :
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
            val properties = listOf(towerProperty)
            enemies.withinRangeOf(entity).forEach {
                it.takeDamage(damagePerTick, attackProperties = properties)
                onTick(it)
            }

            val attackSpeedBoost = entity.statusEffects.find(AttackSpeedBoost::class)?.boostPercentage ?: 0.0
            firingCooldown = 1 / (ticksPerSecond * (1 + attackSpeedBoost))
        }
    }
}
