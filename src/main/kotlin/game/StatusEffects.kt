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
        val foundExisting: StatusEffect<T>? = currentEffects.find(effect::class)

        if (foundExisting is StackableStatus<T>) {
            foundExisting.stackSize += 1
        } else {
            foundExisting?.let { currentEffects -= foundExisting }
            currentEffects += effect
        }
    }

    fun update(entity: T, currentState: GameState, delta: Double) {
        val updatableStatuses = currentEffects.filterIsInstance<DurationStatus<T>>()
        updatableStatuses.forEach { it.update(entity, currentState, delta) }
        updatableStatuses.filter { it.isOver() }.forEach { currentEffects.remove(it) }
    }

    fun snapshot() = currentEffects.toList()

    override fun toString(): String {
        val effectStrings = currentEffects.joinToString()
        return "${javaClass.canonicalName}($effectStrings)"
    }
}

interface StatusEffect<T : GameEntity>

// TODO: Convert to use type object and composition instead of inheritance
// TODO: Convert StatusEffects class into extension functions instead of wrapper class
open class DurationStatus<T : GameEntity>(private var duration: Double): StatusEffect<T> {
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

open class StackableStatus<T : GameEntity>: StatusEffect<T> {
    var stackSize = 1

    override fun toString(): String {
        return "${this.javaClass.canonicalName}(stackSize: $stackSize)"
    }
}

class DamageOverTime(private val damagePerSecond: Double, duration: Double, private val ticksPerSecond: Double) :
    DurationStatus<Enemy>(duration) {
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

class SpeedChange(val speedScaling: Double, duration: Double) : DurationStatus<Enemy>(duration)

class RegenBuff(private val healthPerSecond: Double, duration: Double) : DurationStatus<Enemy>(duration) {
    override fun onUpdate(entity: Enemy, currentState: GameState, delta: Double) {
        val regenAmount = healthPerSecond * delta
        entity.takeDamage(-regenAmount)
    }
}

class DamageTakenChange(val damageScaling: Double, duration: Double): DurationStatus<Enemy>(duration)

class StunEnemyDebuff(duration: Double) : DurationStatus<Enemy>(duration)

class ExplosionDebuff(val damagePerStack: Double) : StackableStatus<Enemy>()

class StunTowerDebuff(duration: Double) : DurationStatus<Tower>(duration)

class BlindDebuff(duration: Double) : DurationStatus<Tower>(duration)

class DamageBoost(duration: Double, val boostPercentage: Double) : DurationStatus<Tower>(duration)

class AttackSpeedBoost(duration: Double, val boostPercentage: Double) : DurationStatus<Tower>(duration)
