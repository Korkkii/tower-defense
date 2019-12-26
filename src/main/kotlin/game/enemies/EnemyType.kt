package game.enemies

import game.AddEntity
import game.BossStartEvent
import game.GameState
import game.PhysicsComponent
import game.SpeedChange
import game.center
import game.data.GameData
import game.towers.TypeEnum
import javafx.scene.paint.Color
import kotlin.properties.Delegates
import kotlin.random.Random

interface EnemyInfo {
    val enemyPrice: Int
    val baseHealth: Double
    val healthPerLevel: Double
    val radius: Double
    val color: Color
    val velocity: Double
    val type: TypeEnum
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
    override val type: TypeEnum,
    override val onDamage: (Enemy, DamageType) -> Unit = { _, _ -> },
    override val onCreate: (Enemy) -> Unit = {},
    override val physicsComponentConstructor: () -> PhysicsComponent<Enemy> = { EnemyMovementComponent() }
) : EnemyInfo {
    companion object {
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.mapNotNull { boss ->
                val type = boss.type
                val bossSeries = GameData.bossLevels[type]
                val defeatedBossesInSeries = defeated.filter { it.type == type }
                bossSeries?.let { (it.toList() - defeatedBossesInSeries).first() }
            }
            return GameData.bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
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

    if (damageType != OverTimeDamage && randomDouble < spawnChance) {
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
    private val typeInfo: EnemyType
) : EnemyInfo by typeInfo

fun enemyType(block: EnemyTypeBuilder.() -> Unit) = EnemyTypeBuilder().apply(block).build()
fun enemyTypeBase(block: EnemyTypeBuilder.() -> Unit) = EnemyTypeBuilder().apply(block)

class EnemyTypeBuilder {
    var enemyPrice by Delegates.notNull<Int>()
    var baseHealth by Delegates.notNull<Double>()
    var healthPerLevel by Delegates.notNull<Double>()
    var radius by Delegates.notNull<Double>()
    lateinit var color: Color
    var velocity by Delegates.notNull<Double>()
    lateinit var type: TypeEnum
    var onDamage: (Enemy, DamageType) -> Unit = { _, _ -> }
    var onCreate: (Enemy) -> Unit = {}
    var physicsComponentConstructor: () -> PhysicsComponent<Enemy> = { EnemyMovementComponent() }

    fun build() = EnemyType(
        enemyPrice,
        baseHealth,
        healthPerLevel,
        radius,
        color,
        velocity,
        type,
        onDamage,
        onCreate,
        physicsComponentConstructor
    )

    fun with(block: EnemyTypeBuilder.() -> Unit) = this.apply(block).build()
}
