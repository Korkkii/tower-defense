package ui

import game.GameState
import game.PlacingTowerEvent
import game.towers.TowerType
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.layout.FlowPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class TowerButtons : FlowPane() {
    init {
        val towerButtons = TowerType.towerTypes.map {
            TowerButton(it, it.name)
        }

        this.padding = Insets(10.0, 20.0, 10.0, 20.0)
        this.alignment = Pos.CENTER
        this.hgap = 10.0
        this.vgap = 10.0
        this.children += towerButtons
    }
}

class TowerButton(towerType: TowerType, name: String) : Button(name) {
    init {
        this.graphic = Circle(20.0, Color.BLACK)
        this.contentDisplay = ContentDisplay.TOP
        this.prefWidth = 100.0
        this.setOnMouseClicked { GameState.notify(PlacingTowerEvent(towerType)) }
    }
}
