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
        private val boss = BossType(20, 120.0, EnemyMovementComponent(), 7.0, Color.CRIMSON, "Boss Man")
        val bosses = listOf(boss)
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
