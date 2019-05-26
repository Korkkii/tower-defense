package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.UpgradeClicked
import game.towers.TowerType
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class TowerInfo : VBox() {
    private val range = Label("Tower range")
    private val upgrade = Upgrades()

    init {
        GameState.subscribe(GameStateChanged::class.java) {
            val text =
                it.selectedTower?.let { tower -> "Tower range: ${tower.type.range}" } ?: "No tower selected"
            range.text = text
        }
        background = Background(BackgroundFill(Color.GRAY.brighter(), CornerRadii(2.0), Insets.EMPTY))
        padding = Insets(5.0)
        children += listOf(range, upgrade)
    }
}

class Upgrades : FlowPane() {
    init {
        GameState.subscribe(GameStateChanged::class.java) {
            val selectedTower = it.selectedTower

            val upgradeTypes = selectedTower?.type?.let { type -> TowerType.upgrades[type] } ?: listOf()
            children.clear()
            children += upgradeTypes.map { type -> UpgradeButton(type) }
        }
    }
}

class UpgradeButton(type: TowerType) : Button(type.name) {
    init {
        setOnMouseClicked { GameState.notify(UpgradeClicked(type)) }
    }
}
