package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font


open class BoardSquare(x: Double, y: Double, width: Double, height: Double, val color: Color) : Rectangle(x, y, width, height), GameEntity {
    override fun toString(): String {
        return "${this.javaClass.name}(${(this.x / this.width).toInt()}, ${(this.y / this.height).toInt()})"
    }

    override fun update(currentState: GameState, delta: Double) {}

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = color
        graphics.fillRect(x, y, width, height)
    }
}

open class PathSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.BEIGE) {
    val waypoint: Circle = Circle(x + 0.5 * width, y + 0.5 * height, 1.4)
}
class BuildAreaSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.GREEN) {
    val center = Vector(x + 0.5 * width, y + 0.5 * height)
    var tower: Tower? = null

    fun withinArea(position: Vector): Boolean {
        return this.contains(position.x, position.y)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        super.draw(graphics, state)
        graphics.fill = color.brighter().brighter()
        if (state.state is PlacingTower && withinArea(state.mousePosition())) {
            graphics.fillRect(x, y, width, height)
            if (tower != null) {
                graphics.font = Font(90.0)
                graphics.fill = Color.RED
                graphics.fillText("X", x + 0.1 * width, y + 0.9 * height)
            }
        }
    }
}
class StartSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)
class EndSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height)