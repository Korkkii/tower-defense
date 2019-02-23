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

class GameBoard(width: Double, height: Double) : Group(), UpdatableEntity {
    val board = listOf(
        listOf(0,1,0,0,0),
        listOf(0,1,0,0,0),
        listOf(0,1,1,1,0),
        listOf(0,0,0,1,0),
        listOf(0,0,0,1,0))

    init {
        val rectangleWidth = width / board[0].size
        val rectangleHeight = height / board.size
        val rectangles = board.flatMap { x, y, cellValue ->
            val color = if (cellValue == 1) Color.BLUE else Color.RED
            MovingRectangle(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight, color) as Rectangle }
        this.children.addAll(rectangles)
    }

    override fun update() {
//        rectangle.update()
    }
}

fun <T> List<List<Int>>.flatMap(mappingFunction: (x: Int, y: Int, cellValue: Int) -> T ): List<T> {
    return this.mapIndexed { columnIndex, cells ->
        cells.mapIndexed { rowIndex, cellValue ->
            mappingFunction(rowIndex, columnIndex, cellValue) } }.flatten()
}

class MovingRectangle(x: Double, y: Double, width: Double, height: Double, color: Color) : Rectangle(x, y, width, height), UpdatableEntity {
    init {
        this.fill = color
    }
    override fun update() {
        this.x += 2
        this.y += 1
    }
}