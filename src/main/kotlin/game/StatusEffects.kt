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
        val foundExisting = currentEffects.find(effect::class)
        foundExisting?.let { currentEffects -= foundExisting }
        currentEffects += effect
    }

    fun update(entity: T, currentState: GameState, delta: Double) {
        currentEffects.forEach { it.update(entity, currentState, delta) }
        currentEffects.removeAll { it.isOver() }
    }

    fun snapshot() = currentEffects.toList()

    override fun toString(): String {
        val effectStrings = currentEffects.joinToString()
        return "${javaClass.canonicalName}($effectStrings)"
    }
}

// TODO: Convert to use type object and composition instead of inheritance
// TODO: Convert StatusEffects class into extension functions instead of wrapper class
open class StatusEffect<T : GameEntity>(private var duration: Double) {
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

class DamageOverTime(private val damagePerSecond: Double, duration: Double, private val ticksPerSecond: Double) :
    StatusEffect<Enemy>(duration) {
    private var cooldown = 1 / ticksPerSecond
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        cooldown -= delta
        if (cooldown <= 0.0) {
            val damage = damagePerSecond / ticksPerSecond
            entity.takeDamage(damage, OverTimeDamage)
            cooldown = 1 / ticksPerSecond
        }
    }
}

class SpeedChange(val speedScaling: Double, duration: Double) : StatusEffect<Enemy>(duration)

class RegenBuff(private val healthPerSecond: Double, duration: Double) : StatusEffect<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val regenAmount = healthPerSecond * delta
        entity.takeDamage(-regenAmount)
    }
}

class DamageTakenChange(val damageScaling: Double, duration: Double): StatusEffect<Enemy>(duration)

class StunDebuff(duration: Double) : StatusEffect<Tower>(duration)

class BlindDebuff(duration: Double) : StatusEffect<Tower>(duration)

class DamageBoost(duration: Double, val boostPercentage: Double) : StatusEffect<Tower>(duration)

class AttackSpeedBoost(duration: Double, val boostPercentage: Double) : StatusEffect<Tower>(duration)
