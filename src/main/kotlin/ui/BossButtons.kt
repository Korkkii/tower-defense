package ui

import game.BossStartEvent
import game.BossType
import game.EnemyType
import game.GameState
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.layout.FlowPane
import javafx.scene.shape.Circle

class BossButtons : FlowPane() {
    private val buttons = EnemyType.bosses.map { BossButton(it, it.name) }

    init {
        padding = Insets(10.0, 20.0, 10.0, 20.0)
        alignment = Pos.CENTER
        hgap = 10.0
        vgap = 10.0

        children += buttons
    }
}

class BossButton(type: BossType, name: String) : Button(name) {
    init {
        this.graphic = Circle(20.0, type.color)
        this.contentDisplay = ContentDisplay.TOP
        this.prefWidth = 100.0
        this.setOnMouseClicked { GameState.notify(BossStartEvent(type)) }
    }
}
