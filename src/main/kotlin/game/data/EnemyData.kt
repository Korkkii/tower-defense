package game.data

import game.RegenBuff
import game.enemies.BlastBossPhysicsComponent
import game.enemies.BossType
import game.enemies.EnemyType
import game.enemies.FlashEffect
import game.enemies.StunEffect
import game.enemies.enemyType
import game.enemies.onHitAddSpeedBuff
import game.enemies.onHitSpawnClone
import javafx.scene.paint.Color

val enemy = enemyType {
    enemyPrice = 5
    baseHealth = 20.0
    healthPerLevel = 10.0
    radius = 4.0
    color = Color.RED
    velocity = 35.0
}
val fastEnemy = enemyType {
    enemyPrice = 5
    baseHealth = 10.0
    healthPerLevel = 3.0
    radius = 4.0
    color = Color.DEEPSKYBLUE.brighter()
    velocity = 60.0
}
val windBoss = BossType(
    "Wind Elemental", enemyType {
        enemyPrice = 20
        baseHealth = 80.0
        healthPerLevel = 0.0
        radius = 6.0
        color = Color.SLATEGRAY
        velocity = 35.0
        onDamage = ::onHitAddSpeedBuff
    }
)
val metalBoss = BossType("Metal Elemental", EnemyType(20, 150.0, 0.0, 6.0, Color.DARKGRAY, 35.0))
val natureBoss = BossType(
    "Nature Elemental", enemyType {
        enemyPrice = 20
        baseHealth = 100.0
        healthPerLevel = 0.0
        radius = 6.0
        color = Color.MEDIUMSEAGREEN
        velocity = 35.0
        onCreate = {
            it.statusEffects += RegenBuff(3.0, 3600.0)
        }
    }
)
val fireBoss = BossType("Fire Elemental", enemyType {
    enemyPrice = 20
    baseHealth = 100.0
    healthPerLevel = 0.0
    radius = 7.0
    color = Color.FIREBRICK
    velocity = 35.0
    physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::StunEffect) }
})
val lightBoss = BossType("Light Elemental", enemyType {
    enemyPrice = 20
    baseHealth = 100.0
    healthPerLevel = 0.0
    radius = 7.0
    color = Color.WHITESMOKE
    velocity = 35.0
    physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::FlashEffect) }
})
val waterBoss = BossType(
    "Water Elemental", enemyType {
        enemyPrice = 20
        baseHealth = 80.0
        healthPerLevel = 0.0
        radius = 7.0
        color = Color.NAVY.brighter()
        velocity = 35.0
        onDamage = ::onHitSpawnClone
    }
)
