package game

import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle


open class BoardSquare(x: Double, y: Double, width: Double, height: Double, color: Color) : Rectangle(x, y, width, height) {
    init {
        this.fill = color
    }

    override fun toString(): String {
        return "${this.javaClass.name}(${(this.x / this.width).toInt()}, ${(this.y / this.height).toInt()})"
    }
}

open class PathSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.BEIGE) {
    val waypoint: Circle = Circle(x + 0.5 * width, y + 0.5 * height, 5.0)
}
class BuildAreaSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.GREEN)
class StartSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)
class EndSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)