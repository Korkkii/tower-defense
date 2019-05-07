package game

class WaveGenerator(private val gameBoard: GameBoard) : Observer {
    init {
        GameState.subscribe(WaveComplete.javaClass, this)
        GameState.notify(NewWave(Wave(0, gameBoard)))
    }

    override fun onNotify(event: Event) {
        val currentLevel = GameState.instance.currentWave?.level ?: 0
        GameState.notify(NewWave(Wave(currentLevel + 1, gameBoard)))
    }
}
