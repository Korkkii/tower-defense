package ui

import game.BossDefeated
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
    init {
        GameState.subscribe(BossDefeated::class.java, this::onBossDefeated)

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

        val nextType = EnemyType.bossLevels[type]
        if (nextType != null) children[nodeIndex] = BossButton(nextType)
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
