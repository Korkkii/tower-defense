package game

class WaveGenerator(private val gameBoard: GameBoard) : Observer {
    init {
        GameState.subscribe(WaveComplete.javaClass, this)
        GameState.subscribe(BossStartEvent::class.java, this)
        GameState.notify(NewWave(Wave(0, gameBoard)))
    }

    override fun onNotify(event: Event) {
        when (event) {
            is WaveComplete -> {
                val currentLevel = GameState.instance.currentWave?.level ?: 0
                GameState.notify(NewWave(Wave(currentLevel + 1, gameBoard)))
            }
            is BossStartEvent -> {
                val enemy = Enemy(gameBoard.path, event.bossType)
                GameState.notify(NewEnemyEvent(enemy))
            }
        }

    }
}
