package game

import game.towers.Tower

class StatusEffects<T : GameEntity> {
    val currentEffects = mutableListOf<StatusEffect<T>>()

    fun update(entity: T, currentState: GameState, delta: Double) {
        currentEffects.forEach { it.update(entity, currentState, delta) }
        currentEffects.removeAll { it.isOver() }
    }
}

abstract class StatusEffect<T : GameEntity>(private var duration: Double) {
    fun update(entity: T, currentState: GameState, delta: Double) {
        duration -= delta

        onUpdate(entity, currentState, delta)
    }

    fun isOver() = duration <= 0.0

    open fun onUpdate(entity: T, currentState: GameState, delta: Double) {}
}
class DamageOverTime(private val damagePerSecond: Double, duration: Double) : StatusEffect<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val damage = damagePerSecond * delta
        entity.takeDamage(damage, OverTimeDamage)
    }
}

class SpeedBuff(val speedScaling: Double, duration: Double) : StatusEffect<Enemy>(duration)

class RegenBuff(private val healthPerSecond: Double, duration: Double) : StatusEffect<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val regenAmount = healthPerSecond * delta
        entity.takeDamage(-regenAmount)
    }
}

class StunDebuff(duration: Double) : StatusEffect<Tower>(duration)
