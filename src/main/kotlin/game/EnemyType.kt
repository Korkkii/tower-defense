package game

import javafx.scene.paint.Color

open class EnemyType(
    val enemyPrice: Int,
    val baseHealth: Double,
    val healthPerLevel: Double,
    val radius: Double,
    val color: Color,
    val velocity: Double,
    val actions: EnemyActions = NoopActions
) {
    val movementComponentConstructor = { EnemyMovementComponent() }

    companion object {
        /*
        * Element types
        *   light --> Multiple copies? Can blind towers to give chance to miss?
            fire --> Can stun towers?
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
            7.0,
            Color.SLATEGRAY,
            35.0,
            "Wind Elemental",
            OnHitAction(::onHitAddSpeedBuff)
        )
        val metalBoss = BossType(20, 150.0, 0.0, 6.0, Color.DARKGRAY, 35.0, "Metal Elemental")
        val natureBoss = BossType(20, 100.0, 0.0, 6.0, Color.MEDIUMSEAGREEN, 35.0, "Nature Elemental", OnCreateAction {
            it.statusEffects += RegenBuff(3.0, 3600.0)
        })
        val boss = BossType(20, 120.0, 0.0, 6.0, Color.CRIMSON, 35.0, "Boss Man")
        val upgradedBoss =
            BossType(30, 240.0, 0.0, 6.0, Color.CRIMSON.darker(), 35.0, "Boss Man 2")
        private val bosses = listOf(boss, windBoss, metalBoss, natureBoss)
        val bossLevels = mapOf(boss to upgradedBoss)
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.map { bossLevels[it] }.filterNotNull()
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
}

interface EnemyActions {
    fun onDamage(enemy: Enemy) {}
    fun onCreate(enemy: Enemy) {}
}

object NoopActions : EnemyActions

data class OnHitAction(val onDamageFun : (Enemy) -> Unit) : EnemyActions {
    override fun onDamage(enemy: Enemy): Unit = onDamageFun(enemy)
}

data class OnCreateAction(val onCreateFun : (Enemy) -> Unit) : EnemyActions {
    override fun onCreate(enemy: Enemy): Unit = onCreateFun(enemy)
}

fun onHitAddSpeedBuff(enemy: Enemy) {
    if (enemy.statusEffects.none { it is SpeedBuff })
        enemy.statusEffects += SpeedBuff(2.0, 1.5)
}

class BossType(
    enemyPrice: Int,
    baseHealth: Double,
    healthPerLevel: Double,
    radius: Double,
    color: Color,
    velocity: Double,
    val name: String,
    actions: EnemyActions = NoopActions
) : EnemyType(
    enemyPrice,
    baseHealth,
    healthPerLevel,
    radius,
    color,
    velocity,
    actions
)
