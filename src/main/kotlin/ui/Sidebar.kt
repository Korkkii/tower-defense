package ui

import game.GameState
import game.GameStateChanged
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

class Sidebar :
    VBox() {
    // TODO: Convert JavaFX to KotlinFX
    private val gameState = GameState.instance
    private val money = Text("Player money ${gameState.playerMoney}")
    private val enemies = Text("Enemies on the run ${gameState.enemies.count()}")
    private val towerButtons = TowerButtons()
    private val towerInfo = TowerInfo()
    private val bossButtons = BossButtons()

    init {
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        val children = listOf(money, enemies, towerInfo, towerButtons, bossButtons)
        this.children.addAll(children)
        this.isFillWidth = true
        this.padding = Insets(10.0)

        GameState.subscribe(GameStateChanged::class.java) {
            money.text = "Player money ${it.money}"
            enemies.text = "Enemies on the run ${it.enemyCount}"
        }
    }
}
