package ui

import game.BossDefeated
import game.BossStartEvent
import game.enemies.BossType
import game.enemies.EnemyType
import game.GameState
import game.data.GameData
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.layout.FlowPane
import javafx.scene.shape.Circle

class BossButtons : FlowPane() {
    init {
        GameState.subscribe(BossDefeated::class, this::onBossDefeated)

        padding = Insets(10.0, 20.0, 10.0, 20.0)
        alignment = Pos.CENTER
        hgap = 10.0
        vgap = 10.0

        children += EnemyType.getAvailableBosses().map { BossButton(it) }
    }

    private fun onBossDefeated(event: BossDefeated) {
        val type = event.type
        val nodeIndex = children.indexOfFirst { (it as? BossButton)?.type == type }
        if (nodeIndex == -1) return

        val bossSeries = GameData.bossLevels[type.type] ?: return
        val nextUpgradeIndex = bossSeries.indexOfFirst { it == type } + 1
        val nextUpgrade = bossSeries.getOrNull(nextUpgradeIndex)

        if (nextUpgrade != null) children[nodeIndex] = BossButton(nextUpgrade)
        else children.removeAll(children[nodeIndex])
    }
}

class BossButton(val type: BossType) : Button(type.name) {
    private var bossInPlay = false

    init {
        graphic = Circle(20.0, type.color)
        contentDisplay = ContentDisplay.TOP
        prefWidth = 100.0

        setOnMouseClicked {
            if (!bossInPlay) {
                GameState.notify(BossStartEvent(type))
                bossInPlay = true
            }
        }
    }
}
