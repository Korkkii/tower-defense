package ui

import game.Game
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Hello World"
        val width = 600.0
        val boardWidth = 400.0
        val height = 400.0
        val group = Group()
        val canvas = Canvas(boardWidth, height)
        val scene = Scene(group)
        val game = Game(boardWidth, height, canvas)

        group.children.add(canvas)
        primaryStage?.scene = scene
        game.start()
        primaryStage?.show()
    }
}