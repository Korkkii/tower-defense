package game

data class EnemyType(
    val enemyPrice: Int,
    val maxHealth: Double,
    val movementComponent: EnemyMovementComponent
) {
    val radius = 5.0
    val velocity = 35.0

    companion object {
        val enemy = EnemyType(5, 20.0, EnemyMovementComponent())
    }
}
