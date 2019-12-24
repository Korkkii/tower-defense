package game.enemies

import game.AddEntity
import game.BossStartEvent
import game.GameState
import game.PhysicsComponent
import game.RegenBuff
import game.SpeedChange
import game.center
import javafx.scene.paint.Color
import kotlin.random.Random

interface EnemyInfo {
    val enemyPrice: Int
    val baseHealth: Double
    val healthPerLevel: Double
    val radius: Double
    val color: Color
    val velocity: Double
    val onDamage: (Enemy, DamageType) -> Unit
    val onCreate: (Enemy) -> Unit
    val physicsComponentConstructor: () -> PhysicsComponent<Enemy>
}

data class EnemyType(
    override val enemyPrice: Int,
    override val baseHealth: Double,
    override val healthPerLevel: Double,
    override val radius: Double,
    override val color: Color,
    override val velocity: Double,
    override val onDamage: (Enemy, DamageType) -> Unit = { _, _ -> },
    override val onCreate: (Enemy) -> Unit = {},
    override val physicsComponentConstructor: () -> PhysicsComponent<Enemy> = { EnemyMovementComponent() }
) : EnemyInfo {
    companion object {
        val enemy = EnemyType(5, 20.0, 10.0, 4.0, Color.RED, 35.0)
        val fastEnemy = EnemyType(5, 10.0, 3.0, 4.0, Color.DEEPSKYBLUE.brighter(), 60.0)
        val windBoss = BossType(
            "Wind Elemental", EnemyType(
                20,
                80.0,
                0.0,
                6.0,
                Color.SLATEGRAY,
                35.0,
                onDamage = ::onHitAddSpeedBuff
            )
        )
        val metalBoss = BossType("Metal Elemental", EnemyType(20, 150.0, 0.0, 6.0, Color.DARKGRAY, 35.0))
        val natureBoss = BossType(
            "Nature Elemental", EnemyType(
                20,
                100.0,
                0.0,
                6.0,
                Color.MEDIUMSEAGREEN,
                35.0,
                onCreate = {
                    it.statusEffects += RegenBuff(3.0, 3600.0)
                })
        )
        val fireBoss = BossType("Fire Elemental", EnemyType(
            20,
            100.0,
            0.0,
            7.0,
            Color.FIREBRICK,
            35.0,
            physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::StunEffect) }
        ))
        val lightBoss = BossType("Light Elemental", EnemyType(
            20,
            100.0,
            0.0,
            7.0,
            Color.WHITESMOKE,
            35.0,
            physicsComponentConstructor = { BlastBossPhysicsComponent(20.0, 5.0, ::FlashEffect) }
        ))
        val waterBoss = BossType(
            "Water Elemental", EnemyType(
                20,
                80.0,
                0.0,
                7.0,
                Color.NAVY.brighter(),
                35.0,
                onDamage = ::onHitSpawnClone
            )
        )
        private val bosses = listOf(
            windBoss,
            metalBoss,
            natureBoss,
            fireBoss,
            lightBoss,
            waterBoss
        )
        val bossLevels = mapOf<BossType, BossType>()
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.mapNotNull { bossLevels[it] }
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
}

fun onHitAddSpeedBuff(enemy: Enemy, damageType: DamageType) {
    val noSpeedBuff = !enemy.statusEffects.has(SpeedChange::class)
    val notOverTime = damageType !is OverTimeDamage
    if (noSpeedBuff && notOverTime)
        enemy.statusEffects += SpeedChange(2.0, 1.5)
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

data class BossType(
    val name: String,
    private val type: EnemyType
) : EnemyInfo by type
