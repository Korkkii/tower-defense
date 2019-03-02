package ui

import game.*
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class Sidebar :
    VBox() {
    init {
        val button = Button("Hello")
        button.setOnMouseClicked { GameState.notify(PlacingTowerEvent) }
        this.background = Background(BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
        this.children.addAll(listOf(button))
    }
}