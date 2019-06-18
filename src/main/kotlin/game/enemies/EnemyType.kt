package game.enemies

import game.AddEntity
import game.BossStartEvent
import game.GameState
import game.PhysicsComponent
import game.RegenBuff
import game.SpeedBuff
import game.center
import javafx.scene.paint.Color
import kotlin.random.Random

open class EnemyType(
    val enemyPrice: Int,
    val baseHealth: Double,
    val healthPerLevel: Double,
    val radius: Double,
    val color: Color,
    val velocity: Double,
    val onDamage: (Enemy, DamageType) -> Unit = { _, _ -> },
    val onCreate: (Enemy) -> Unit = {},
    val physicsComponentConstructor: () -> PhysicsComponent<Enemy> = { EnemyMovementComponent() }
) {
    companion object {
        /*
        * Element types
        *   X light --> Multiple copies? Can blind towers to give chance to miss?
            X fire --> Can stun towers?
            X wind --> Faster? Burst of speed on hit?
            water --> Divides into multiple enemies?
            X nature --> Heals over time? Burst heal?
            X metal --> Takes less damage from X attack? Or just shit ton of health?
        * */

        val enemy = EnemyType(5, 20.0, 10.0, 4.0, Color.RED, 35.0)
        val fastEnemy = EnemyType(5, 10.0, 3.0, 4.0, Color.DEEPSKYBLUE.brighter(), 60.0)
        val windBoss = BossType(
            20,
            80.0,
            0.0,
            6.0,
            Color.SLATEGRAY,
            35.0,
            "Wind Elemental",
            onDamage = ::onHitAddSpeedBuff
        )
        val metalBoss = BossType(20, 150.0, 0.0, 6.0, Color.DARKGRAY, 35.0, "Metal Elemental")
        val natureBoss = BossType(
            20,
            100.0,
            0.0,
            6.0,
            Color.MEDIUMSEAGREEN,
            35.0,
            "Nature Elemental",
            onCreate = {
                it.statusEffects += RegenBuff(3.0, 3600.0)
            })
        val fireBoss = BossType(
            20,
            100.0,
            0.0,
            7.0,
            Color.FIREBRICK,
            35.0,
            "Fire Elemental",
            physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::StunEffect) }
        )
        val lightBoss = BossType(
            20,
            100.0,
            0.0,
            7.0,
            Color.WHITESMOKE,
            35.0,
            "Light Elemental",
            physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::FlashEffect) }
        )
        val waterBoss = BossType(
            20,
            80.0,
            0.0,
            7.0,
            Color.NAVY.brighter(),
            35.0,
            "Water Elemental",
            onDamage = ::onHitSpawnClone
        )
        val boss = BossType(20, 120.0, 0.0, 6.0, Color.CRIMSON, 35.0, "Boss Man")
        val upgradedBoss =
            BossType(30, 240.0, 0.0, 6.0, Color.CRIMSON.darker(), 35.0, "Boss Man 2")
        private val bosses = listOf(
            boss,
            windBoss,
            metalBoss,
            natureBoss,
            fireBoss,
            lightBoss,
            waterBoss
        )
        val bossLevels = mapOf(boss to upgradedBoss)
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.map { bossLevels[it] }.filterNotNull()
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
}

fun onHitAddSpeedBuff(enemy: Enemy, damageType: DamageType) {
    val noSpeedBuff = !enemy.statusEffects.has(SpeedBuff::class)
    val notOverTime = damageType !is OverTimeDamage
    if (noSpeedBuff && notOverTime)
        enemy.statusEffects += SpeedBuff(2.0, 1.5)
}

fun onHitSpawnClone(enemy: Enemy, damageType: DamageType) {
    val spawnChance = 0.1
    val randomDouble = Random.nextDouble()

    if (randomDouble < spawnChance) {
        val bossHealthPercent = enemy.health / enemy.maxHealth
        val type = enemy.type as BossType
        val animationEntity = SpawnEffectEntity(enemy.position, enemy.target.waypoint.center(), enemy.type.radius) {
            GameState.notify(BossStartEvent(type, bossHealthPercent, enemy.target, it))
        }

        GameState.notify(AddEntity(animationEntity))
    }
}

class BossType(
    enemyPrice: Int,
    baseHealth: Double,
    healthPerLevel: Double,
    radius: Double,
    color: Color,
    velocity: Double,
    val name: String,
    onDamage: (Enemy, DamageType) -> Unit = { _, _ -> },
    onCreate: (Enemy) -> Unit = {},
    physicsComponentConstructor: () -> PhysicsComponent<Enemy> = { EnemyMovementComponent() }
) : EnemyType(
    enemyPrice,
    baseHealth,
    healthPerLevel,
    radius,
    color,
    velocity,
    onDamage,
    onCreate,
    physicsComponentConstructor
)
