package game

import game.enemies.Enemy
import game.enemies.OverTimeDamage
import game.towers.Tower
import kotlin.reflect.KClass

class StatusEffects<T : GameEntity> {
    private val currentEffects = mutableListOf<StatusEffect<T>>()

    fun <U : StatusEffect<T>> has(effect: KClass<U>): Boolean {
        return currentEffects.filterIsInstance(effect.java).isNotEmpty()
    }

    fun <U : StatusEffect<T>> find(type: KClass<U>): U? = currentEffects.find(type)

    operator fun <U : StatusEffect<T>> plusAssign(effect: U) {
        currentEffects += effect
    }

    fun update(entity: T, currentState: GameState, delta: Double) {
        currentEffects.forEach { it.update(entity, currentState, delta) }
        currentEffects.removeAll { it.isOver() }
    }

    override fun toString(): String {
        val effectStrings = currentEffects.joinToString()
        return "${javaClass.canonicalName}($effectStrings)"
    }
}

abstract class StatusEffect<T : GameEntity>(private var duration: Double) {
    fun update(entity: T, currentState: GameState, delta: Double) {
        duration -= delta

        onUpdate(entity, currentState, delta)
    }

    fun isOver() = duration <= 0.0

    open fun onUpdate(entity: T, currentState: GameState, delta: Double) {}

    override fun toString(): String {
        return "${this.javaClass.canonicalName}(duration: $duration)"
    }
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

class BlindDebuff(duration: Double) : StatusEffect<Tower>(duration)
