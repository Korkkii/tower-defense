package game

import javafx.scene.paint.Color

open class EnemyType(
    val enemyPrice: Int,
    val baseHealth: Double,
    val healthPerLevel: Double,
    val radius: Double,
    val color: Color,
    val velocity: Double
) {
    val movementComponentConstructor = { EnemyMovementComponent() }

    companion object {
        val enemy = EnemyType(5, 20.0, 10.0,5.0, Color.RED, 35.0)
        val fastEnemy = EnemyType(5, 10.0, 3.0,5.0, Color.DEEPSKYBLUE.brighter(), 60.0)
        val boss = BossType(20, 120.0, 0.0,7.0, Color.CRIMSON, 35.0, "Boss Man")
        val upgradedBoss =
            BossType(30, 240.0, 0.0,7.0, Color.CRIMSON.darker(), 35.0, "Boss Man 2")
        private val bosses = listOf(boss)
        val bossLevels = mapOf(boss to upgradedBoss)
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.map { bossLevels[it] }.filterNotNull()
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
}

class BossType(
    enemyPrice: Int,
    baseHealth: Double,
    healthPerLevel: Double,
    radius: Double,
    color: Color,
    velocity: Double,
    val name: String
) : EnemyType(
    enemyPrice,
    baseHealth,
    healthPerLevel,
    radius,
    color,
    velocity
)
