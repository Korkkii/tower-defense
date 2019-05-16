package ui

import game.Event
import game.GameState
import game.GameStateChanged
import game.Observer
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

class TowerInfo : VBox(), Observer {
    private val range = Label("Tower range")
    private val upgrade = Upgrades()

    init {
        GameState.subscribe(GameStateChanged::class.java, this)
        this.background = Background(BackgroundFill(Color.GRAY.brighter(), CornerRadii(2.0), Insets.EMPTY))
        this.padding = Insets(5.0)
        this.children += listOf(range, upgrade)
    }

    override fun onNotify(event: Event) {
        val state = event as GameStateChanged
        val text =
            state.selectedTower?.let { "Tower range: ${it.type.range}" } ?: "No tower selected"
        range.text = text
    }
}

class Upgrades : FlowPane(), Observer {
    init {
        GameState.subscribe(GameStateChanged::class.java, this)
    }

    override fun onNotify(event: Event) {
        val state = event as GameStateChanged
        val tower = state.selectedTower

        val upgradeTypes = tower?.type?.let { TowerType.upgrades[it] } ?: listOf()
        this.children.clear()
        this.children += upgradeTypes.map { UpgradeButton(it) }.toMutableList()
    }
}

class UpgradeButton(type: TowerType) : Button(type.name) {
    init {
        this.setOnMouseClicked { GameState.notify(UpgradeClicked(type)) }
    }
}
