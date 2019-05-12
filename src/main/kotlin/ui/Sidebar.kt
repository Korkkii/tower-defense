package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.Observer
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

class Sidebar :
    VBox(), Observer {
    private val gameState = GameState.instance
    private val money = Text("Player money ${gameState.playerMoney}")
    private val enemies = Text("Enemies on the run ${gameState.enemies.count()}")
    private val towerButtons = TowerButtons()
    private val towerInfo = TowerInfo()

    init {
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        val children = listOf(money, enemies, towerInfo, towerButtons)
        this.children.addAll(children)
        this.isFillWidth = true
        this.padding = Insets(10.0)

        GameState.subscribe(GameStateChanged::class.java, this)
    }

    override fun onNotify(event: Event) {
        when (event) {
            is GameStateChanged -> {
                money.text = "Player money ${gameState.playerMoney}"
                enemies.text = "Enemies on the run ${gameState.enemies.count()}"
            }
        }
    }
}
