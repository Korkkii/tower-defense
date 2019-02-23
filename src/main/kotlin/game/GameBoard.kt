package game

import javafx.scene.Group

class GameBoard(width: Double, height: Double) : Group(), UpdatableEntity {
    val board = listOf(
        listOf(0,2,0,0,0),
        listOf(0,1,0,0,0),
        listOf(0,1,1,1,0),
        listOf(0,0,0,1,0),
        listOf(0,0,0,1,0))

    init {
        val rectangleWidth = width / board[0].size
        val rectangleHeight = height / board.size
        val rectangles = board.map { x, y, cellValue ->
            when(cellValue) {
                0 -> BuildAreaSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                1 -> PathSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                2 -> StartSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
                else -> BuildAreaSquare(x * rectangleWidth, y * rectangleHeight, rectangleWidth, rectangleHeight)
            }
        }
        this.children.addAll(rectangles.flatten())
        val enemy = Enemy(rectangles)
        this.children.add(enemy)
    }

    override fun update() {
//        rectangle.update()
    }
}
