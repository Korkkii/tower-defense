package game

import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Enemy(board: List<List<BoardSquare>>) : Circle(10.0) {
    private lateinit var target: PathSquare

    init {
        val path = generatePath(board)
        val start = path[0]
        this.centerX = start.x + 0.5 * start.width
        this.centerY = start.y + 0.5 * start.height
        this.fill = Color.RED
        println(this.centerX)
        println(this.centerY)
        println(this)
    }

    private fun generatePath(board: List<List<BoardSquare>>): List<PathSquare> {
        val start = board.flatten().find { it is StartSquare } as? StartSquare
            ?: throw Error("Missing start square in the board!")

        return listOf(start, PathSquare(100.0, 100.0, 80.0, 80.0))
    }
}