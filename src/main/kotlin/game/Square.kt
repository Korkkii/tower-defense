package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle


open class BoardSquare(x: Double, y: Double, width: Double, height: Double, val color: Color) : Rectangle(x, y, width, height), GameEntity {
    override fun toString(): String {
        return "${this.javaClass.name}(${(this.x / this.width).toInt()}, ${(this.y / this.height).toInt()})"
    }

    override fun update(currentState: GameState, delta: Double) {}

    override fun draw(graphics: GraphicsContext) {
        graphics.fill = color
        graphics.fillRect(x, y, width, height)
    }
}

open class PathSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.BEIGE) {
    val waypoint: Circle = Circle(x + 0.5 * width, y + 0.5 * height, 1.4)
}
class BuildAreaSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.GREEN) {
    val center = Vector(x + 0.5 * width, y + 0.5 * height)
}
class StartSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)
class EndSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)