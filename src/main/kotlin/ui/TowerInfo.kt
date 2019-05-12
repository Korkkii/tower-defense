package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.Observer
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class TowerInfo : VBox(), Observer {
    private val range = Label("Tower range")

    init {
        GameState.subscribe(GameStateChanged::class.java, this)
        this.background = Background(BackgroundFill(Color.GRAY.brighter(), CornerRadii(2.0), Insets.EMPTY))
        this.padding = Insets(5.0)
        this.children += range
    }

    override fun onNotify(event: Event) {
        val state = event as GameStateChanged
        val text =
        state.selectedTower?.let { "Tower range: ${it.type.range}" } ?: "No tower selected"
        range.text = text
    }
}
