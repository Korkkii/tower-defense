package ui

import game.Game
import game.GameState
import game.PlacingTowerEvent
import game.data.GameData
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Hello World"
        val width = 1200.0
        val boardWidth = 900.0
        val height = 900.0

        val canvas = Canvas(boardWidth, height)
        val game = Game(boardWidth, height, canvas)
        val sidebar = Sidebar()

        val children = arrayOf(canvas, sidebar)
        val group = HBox(*children)
        HBox.setHgrow(sidebar, Priority.ALWAYS)
        group.prefWidth = width

        val scene = Scene(group)
        scene.setOnKeyReleased(::handleInput)

        primaryStage?.scene = scene
        game.start()
        primaryStage?.show()
    }
}
