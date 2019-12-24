package game.towers.projectiles

import game.StatusEffect
import game.towers.Tower

interface AttackProperty

object NoProperty : AttackProperty
data class BounceProperty(val bouncesLeft: Int) : AttackProperty
data class IncreasedDamageProperty(val enemyCount: Int) : AttackProperty
data class BlindedProperty(val missChance: Double) : AttackProperty
data class ShootingTowerProperty(val tower: Tower, val statusEffect: List<StatusEffect<Tower>>) : AttackProperty
data class CritProperty(val damageModifier: Double) : AttackProperty
