package ui

import game.GameState
import game.GameStateChanged
import game.SellTower
import game.UpgradeClicked
import game.data.GameData
import game.towers.TowerType
import game.towers.calculateFireRate
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
    private val sellButton = GameButton("Sell tower") {
        setOnMouseClicked {
            GameState.notify(SellTower)
        }
    }

    init {
        GameState.subscribe(GameStateChanged::class) {
            // TODO: Better status info instead of class names
            val text =
                it.selectedTower?.let { tower ->
                    val range = "Tower range: ${tower.type.range}"
                    val statusEffects = "Tower status: ${tower.statusEffects.snapshot()}"
                    val totalFireRate = calculateFireRate(tower.fireRate, tower)
                    val attackSpeed = "Tower attack speed: ${totalFireRate.toExactString()}"
                    listOf(range, attackSpeed, statusEffects).joinToString("\n")
                }
                    ?: "No tower selected"
            range.text = text

            val missingSellButton = !children.contains(sellButton)

            if (it.selectedTower != null && missingSellButton) {
                children += sellButton
            } else if (it.selectedTower == null && !missingSellButton) {
                children -= sellButton
            }
        }
        background = Background(BackgroundFill(Color.GRAY.brighter(), CornerRadii(2.0), Insets.EMPTY))
        padding = Insets(5.0)

        children += listOf(range, upgrade)
    }
}

private fun Double.toExactString() = this.toBigDecimal().toPlainString()

class Upgrades : FlowPane() {
    init {
        GameState.subscribe(GameStateChanged::class) {
            val selectedTower = it.selectedTower

            val upgradeTypes = selectedTower?.type?.let { type -> GameData.possibleTowerUpgrades[type] } ?: listOf()
            children.clear()
            children += upgradeTypes.map { type ->
                GameButton(type.name) {
                    setOnMouseClicked {
                        if (type.isAvailable()) {
                            GameState.notify(UpgradeClicked(type))
                        }
                    }
                }
            }
        }
    }
}

class GameButton(name: String, block: Button.() -> Unit) : Button(name) {
    init {
        this.apply(block)
    }
}
