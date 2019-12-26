package game.data

import game.RegenBuff
import game.enemies.BlastBossPhysicsComponent
import game.enemies.BossType
import game.enemies.EnemyType
import game.enemies.FlashEffect
import game.enemies.StunEffect
import game.enemies.enemyType
import game.enemies.enemyTypeBase
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

fun bossBase() = enemyTypeBase {
    healthPerLevel = 0.0
}

val windBossBase = bossBase().apply {
    healthPerLevel = 0.0
    radius = 6.0
    color = Color.SLATEGRAY
    velocity = 35.0
    onDamage = ::onHitAddSpeedBuff
}
val windBoss = BossType(
    "Wind Elemental", windBossBase.with {
        enemyPrice = 20
        baseHealth = 80.0
    }
)
val windBoss2 = BossType(
    "Wind Elemental",  windBossBase.with {
        enemyPrice = 40
        baseHealth = 300.0
    }
)
val windBoss3 = BossType(
    "Wind Elemental",  windBossBase.with {
        enemyPrice = 80
        baseHealth = 800.0
    }
)

val metalBossBase = bossBase().apply {
    radius = 6.0
    color = Color.DARKGRAY
    velocity = 35.0
}
val metalBoss = BossType("Metal Elemental", metalBossBase.with {
    enemyPrice = 20
    baseHealth = 150.0
})
val metalBoss2 = BossType("Metal Elemental", metalBossBase.with {
    enemyPrice = 40
    baseHealth = 600.0
})
val metalBoss3 = BossType("Metal Elemental", metalBossBase.with {
    enemyPrice = 80
    baseHealth = 1600.0
})

val natureBossBase = bossBase().apply {
    radius = 6.0
    color = Color.MEDIUMSEAGREEN
    velocity = 35.0
}
val natureBoss = BossType(
    "Nature Elemental", natureBossBase.with {
        enemyPrice = 20
        baseHealth = 100.0
        onCreate = {
            it.statusEffects += RegenBuff(3.0, 3600.0)
        }
    }
)
val natureBoss2 = BossType(
    "Nature Elemental", natureBossBase.with {
        enemyPrice = 40
        baseHealth = 400.0
        onCreate = {
            it.statusEffects += RegenBuff(7.0, 3600.0)
        }
    }
)
val natureBoss3 = BossType(
    "Nature Elemental", natureBossBase.with {
        enemyPrice = 80
        baseHealth = 1000.0
        onCreate = {
            it.statusEffects += RegenBuff(15.0, 3600.0)
        }
    }
)

val fireBossBase = bossBase().apply {
    radius = 7.0
    color = Color.FIREBRICK
    velocity = 35.0
    physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::StunEffect) }
}
val fireBoss = BossType("Fire Elemental", fireBossBase.with {
    enemyPrice = 20
    baseHealth = 100.0
})
val fireBoss2 = BossType("Fire Elemental", fireBossBase.with {
    enemyPrice = 40
    baseHealth = 400.0
})
val fireBoss3 = BossType("Fire Elemental", fireBossBase.with {
    enemyPrice = 80
    baseHealth = 1000.0
})

val lightBossBase = bossBase().apply {
    radius = 7.0
    color = Color.WHITESMOKE
    velocity = 35.0
    physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::FlashEffect) }
}
val lightBoss = BossType("Light Elemental", lightBossBase.with {
    enemyPrice = 20
    baseHealth = 100.0
})
val lightBoss2 = BossType("Light Elemental", lightBossBase.with {
    enemyPrice = 40
    baseHealth = 400.0
})
val lightBoss3 = BossType("Light Elemental", lightBossBase.with {
    enemyPrice = 80
    baseHealth = 1000.0
})

val waterBossBase = bossBase().apply {
    radius = 7.0
    color = Color.NAVY.brighter()
    velocity = 35.0
    onDamage = ::onHitSpawnClone
}
val waterBoss = BossType(
    "Water Elemental", waterBossBase.with {
        enemyPrice = 20
        baseHealth = 80.0
    }
)
val waterBoss2 = BossType(
    "Water Elemental", waterBossBase.with {
        enemyPrice = 40
        baseHealth = 300.0
    }
)
val waterBoss3 = BossType(
    "Water Elemental", waterBossBase.with {
        enemyPrice = 80
        baseHealth = 800.0
    }
)
