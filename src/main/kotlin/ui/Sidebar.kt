package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.Observer
import game.PlacingTowerEvent
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

    init {
        val button = Button("Place tower")
        button.setOnMouseClicked { GameState.notify(PlacingTowerEvent) }
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))

        this.children.addAll(listOf(button, lives))
        GameState.subscribe(this)
    }

    override fun onNotify(event: Event) {
        when (event) {
            GameStateChanged -> lives.text = "Player lives ${GameState.instance.playerLives}"
        }
    }
}
