package ui

import game.Event
import game.GameEnded
import game.GameState
import game.GameStateChanged
import game.Observer
import game.PlacingTowerEvent
import game.towers.SingleTower
import game.towers.SplashTower
import game.towers.Tower
import javafx.geometry.Insets
import javafx.scene.control.Button
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

    init {
        val towerButtons = Tower.allTowers.map {
            val (constructor, name) = it
            val button = Button(name)
            button.setOnMouseClicked { GameState.notify(PlacingTowerEvent(constructor)) }
            button
        }
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        val children = towerButtons + money + enemies
        this.children.addAll(children)

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
