package game

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle


open class BoardSquare(x: Double, y: Double, width: Double, height: Double, color: Color) : Rectangle(x, y, width, height) {
    init {
        this.fill = color
    }
}

open class PathSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.BEIGE)
class BuildAreaSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.GREEN)
class StartSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)