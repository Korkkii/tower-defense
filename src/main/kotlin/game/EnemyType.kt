package game

import javafx.scene.paint.Color

open class EnemyType(
    val enemyPrice: Int,
    val maxHealth: Double,
    val movementComponent: EnemyMovementComponent,
    val radius: Double,
    val color: Color
) {
    val velocity = 35.0

    companion object {
        val enemy = EnemyType(5, 20.0, EnemyMovementComponent(), 5.0, Color.RED)
        val boss = BossType(20, 120.0, EnemyMovementComponent(), 7.0, Color.CRIMSON, "Boss Man")
        val upgradedBoss = BossType(30, 240.0, EnemyMovementComponent(), 7.0, Color.CRIMSON.darker(), "Boss Man 2")
        private val bosses = listOf(boss)
        private val bossLevels = mapOf(boss to upgradedBoss)
        fun getAvailableBosses(): List<BossType> {
            val defeated = GameState.instance.defeatedBosses
            val upgradesOfDefeated = defeated.map { bossLevels[it] }.filterNotNull()
            return bosses + upgradesOfDefeated - GameState.instance.defeatedBosses
        }
    }
}

class BossType(
    enemyPrice: Int,
    maxHealth: Double,
    movementComponent: EnemyMovementComponent,
    radius: Double,
    color: Color,
    val name: String
) : EnemyType(
    enemyPrice,
    maxHealth,
    movementComponent,
    radius,
    color
)
