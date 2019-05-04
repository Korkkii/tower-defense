package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.Observer
import game.PlacingTowerEvent
import game.towers.SingleTower
import game.towers.SplashTower
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
    val lives = Text("Player lives ${GameState.instance.playerLives}")
    val money = Text("Player money ${GameState.instance.playerMoney}")

    init {
        val singleHitTowerButton = Button("Single hit tower")
        val splashTowerButton = Button("Splash tower")
        singleHitTowerButton.setOnMouseClicked { GameState.notify(PlacingTowerEvent(::SingleTower)) }
        splashTowerButton.setOnMouseClicked { GameState.notify(PlacingTowerEvent(::SplashTower)) }
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))

        this.children.addAll(listOf(singleHitTowerButton, splashTowerButton, lives, money))
        GameState.subscribe(GameStateChanged, this)
    }

    override fun onNotify(event: Event) {
        when (event) {
            GameStateChanged -> {
                lives.text = "Player lives ${GameState.instance.playerLives}"
                money.text = "Player money ${GameState.instance.playerMoney}"
            }
        }
    }
}
