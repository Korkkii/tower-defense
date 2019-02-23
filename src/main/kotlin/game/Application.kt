package game

import javafx.scene.paint.Color
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Hello World"
        val width = 600.0
        val boardWidth = 400.0
        val height = 400.0
        val group = GameBoard(boardWidth, height)

        val scene = Scene(group, width, height, Color.GRAY)

        primaryStage?.scene = scene
        val gameLoop = GameLoop(group)
        gameLoop.start()
        primaryStage?.show()
    }
}