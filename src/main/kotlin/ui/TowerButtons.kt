package ui

import game.BossDefeated
import game.GameState
import game.PlacingTowerEvent
import game.data.GameData
import game.towers.TowerType
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.BorderWidths
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Text

class TowerButtons : FlowPane() {
    init {
        val towerButtons = GameData.towerTypes.map {
            TowerButton(it, it.name)
        }

        padding = Insets(10.0, 20.0, 10.0, 20.0)
        alignment = Pos.CENTER
        hgap = 10.0
        vgap = 10.0
        children += towerButtons
    }
}

class TowerButton(private val towerType: TowerType, name: String) : VBox() {
    private val icon = Circle(20.0, Color.BLACK)
    private val text = Text(name)

    init {
        // TODO: Make these properly sized
        prefWidth = 100.0
        border = Border(BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii(2.0), BorderWidths(1.0)))
        alignment = Pos.CENTER
        spacing = 2.0
        padding = Insets(4.0)
        GameState.subscribe(BossDefeated::class) { updateButton(towerType) }

        setOnMouseClicked {
            if (towerType.isAvailable()) GameState.notify(PlacingTowerEvent(towerType))
        }

        updateButton(towerType)
    }

    private fun updateButton(towerType: TowerType) {
        val towerAvailable = towerType.isAvailable()
        val newBackground = if (towerAvailable) Color.WHITE else Color.GRAY

        background = Background(BackgroundFill(newBackground, CornerRadii(2.0), Insets.EMPTY))

        val content = getContent(towerType)
        children.clear()
        children += content
    }

    private fun getContent(towerType: TowerType): List<Node> {
        val content = if (!towerType.isAvailable()) {
            val requirementText = getRequirementText(towerType)
            val text = Text(requirementText)
            text.wrappingWidth = prefWidth
            text
        } else icon

        return listOf(content, text)
    }

    private fun getRequirementText(towerType: TowerType) =
        GameData.towerUpgradeRequirements[towerType]?.let { "Requires ${it.joinToString(", ") { boss -> boss.name }}" }
            ?: "No requirements"
}
