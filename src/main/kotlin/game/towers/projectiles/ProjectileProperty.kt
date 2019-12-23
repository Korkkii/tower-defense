package game.towers.projectiles

import game.StatusEffect
import game.towers.Tower

interface ProjectileProperty

object NoProperty : ProjectileProperty
data class BounceProperty(val bouncesLeft: Int) : ProjectileProperty
data class IncreasedDamageProperty(val enemyCount: Int) : ProjectileProperty
data class BlindedProperty(val missChance: Double) : ProjectileProperty
data class ShootingTowerProperty(val statusEffect: List<StatusEffect<Tower>>) : ProjectileProperty
data class CritProperty(val damageModifier: Double) : ProjectileProperty
