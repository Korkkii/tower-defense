package game

import javafx.scene.paint.Color
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.title = "Hello World"
        val group = GameBoard()

        val scene = Scene(group, 600.0, 400.0, Color.BLUE)

        primaryStage?.scene = scene
        val gameLoop = GameLoop(group)
        gameLoop.start()
        primaryStage?.show()
    }
}

class GameBoard : Group(), UpdatableEntity {
    val rectangle = MovingRectangle(200.0, 100.0, 50.0, 50.0)

    init {
        this.children.add(rectangle)
    }

    override fun update() {
        rectangle.update()
    }
}

class MovingRectangle(x: Double, y: Double, width: Double, height: Double) : Rectangle(x, y, width, height), UpdatableEntity {
    override fun update() {
        this.x += 2
        this.y += 1
    }
}