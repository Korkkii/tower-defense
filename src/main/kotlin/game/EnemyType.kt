package game

import javafx.scene.paint.Color

open class EnemyType(
    val enemyPrice: Int,
    val baseHealth: Double,
    val healthPerLevel: Double,
    val radius: Double,
    val color: Color,
    val velocity: Double,
    val onDamage: (Enemy) -> Unit = {}
) {
    val movementComponentConstructor = { EnemyMovementComponent() }

    companion object {
        /*
        * Element types
        *   light --> Multiple copies? Can blind towers to give chance to miss?
            fire --> Can stun towers?
            X wind --> Faster? Burst of speed on hit?
            water --> Divides into multiple enemies?
            nature --> Heals over time? Burst heal?
            metal --> Takes less damage from X attack?
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
            ::onHitAddSpeedBuff)
        val boss = BossType(20, 120.0, 0.0, 6.0, Color.CRIMSON, 35.0, "Boss Man")
        val upgradedBoss =
            BossType(30, 240.0, 0.0, 6.0, Color.CRIMSON.darker(), 35.0, "Boss Man 2")
        private val bosses = listOf(boss, windBoss)
        val bossLevels = mapOf(boss to upgradedBoss)
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.map { bossLevels[it] }.filterNotNull()
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
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
    onDamage: (Enemy) -> Unit = {}
) : EnemyType(
    enemyPrice,
    baseHealth,
    healthPerLevel,
    radius,
    color,
    velocity,
    onDamage
)
