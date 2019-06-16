package game

import game.board.GameBoard
import game.enemies.Enemy

class Wave(val level: Int, gameBoard: GameBoard) : UpdatableEntity {
    private val enemies: MutableList<Enemy> = createWave(gameBoard.path, level).toMutableList()
    private var timeUntilNextEnemy = if (level == 0) 0.0 else 5.0
    private var enemyRate = 2.0 // Enemies per second

    private fun isComplete(): Boolean {
        return enemies.isEmpty()
    }

    override fun update(currentState: GameState, delta: Double) {
        timeUntilNextEnemy -= delta

        if (timeUntilNextEnemy <= 0.0 && enemies.isNotEmpty()) {
            val nextEnemy = enemies.first()
            enemies.remove(nextEnemy)
            GameState.notify(NewEnemyEvent(nextEnemy))
            timeUntilNextEnemy = 1.0 / enemyRate

            if (isComplete()) GameState.notify(WaveComplete)
        }
    }
}
