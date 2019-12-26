package ui

import game.EmptyClick
import game.GameState
import game.PlacingTowerEvent
import game.data.GameData
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

private typealias CommandFunction = () -> Unit

fun handleInput(event: KeyEvent) {
    val command: CommandFunction? = when (event.code) {
        KeyCode.Q -> placeBasicTower
        KeyCode.ESCAPE -> clearSelection
        else -> null
    }

    command?.let { it() }
}

private val placeBasicTower = { GameState.notify(PlacingTowerEvent(GameData.towerTypes[0])) }
private val clearSelection = { GameState.notify(EmptyClick) }
