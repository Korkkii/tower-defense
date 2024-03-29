package game

import game.board.GameBoard
import game.enemies.Enemy

class WaveGenerator(private val gameBoard: GameBoard) {
    init {
        GameState.subscribe(WaveComplete::class) {
            val currentLevel = GameState.instance.currentWave?.level ?: 0
            GameState.notify(NewWave(Wave(currentLevel + 1, gameBoard)))
        }
        GameState.subscribe(BossStartEvent::class) {
            val currentLevel = GameState.instance.currentWave?.level ?: 0
            val enemy =
                Enemy(gameBoard.path, it.bossType, currentLevel, it.healthPercent, it.currentTarget, it.currentPosition)
            GameState.notify(NewEnemyEvent(enemy))
        }
        GameState.notify(NewWave(Wave(0, gameBoard)))
    }
}
