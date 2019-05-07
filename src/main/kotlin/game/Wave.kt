package game

class Wave(val level: Int, gameBoard: GameBoard) : UpdatableEntity {
    private var enemies: List<Enemy>
    private var timeUntilNextEnemy = if (level == 0) 0.0 else 5.0
    private var enemyRate = 1.0 // Enemies per second

    init {
        val enemyCount = kotlin.random.Random.nextInt(1, 5)
        enemies = (0..enemyCount).map { Enemy(gameBoard) }
    }

    private fun isComplete(): Boolean {
        return enemies.isEmpty()
    }

    override fun update(currentState: GameState, delta: Double) {
        timeUntilNextEnemy -= delta

        if (timeUntilNextEnemy <= 0.0 && enemies.isNotEmpty()) {
            val nextEnemy = enemies.first()
            enemies = enemies.drop(1)
            GameState.notify(NewEnemyEvent(nextEnemy))
            timeUntilNextEnemy = 1.0 / enemyRate

            if (isComplete()) GameState.notify(WaveComplete)
        }
    }
}
