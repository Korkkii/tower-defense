package game

import game.towers.Tower
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

abstract class PathSquare(x: Double, y: Double, width: Double, height: Double) : BoardSquare(x, y, width, height, Color.BEIGE) {
    val waypoint: Circle = Circle(x + 0.5 * width, y + 0.5 * height, 1.4)

    abstract val nextDirection: Direction
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

        val mousePosition = graphics.transform.inverseTransform(state.mousePosition())

        if (state.state is PlacingTower && withinArea(mousePosition)) {
            graphics.fillRect(x, y, width, height)
            if (tower != null) {
                graphics.font = Font(20.0)
                graphics.fill = Color.RED
                graphics.fillText("X", x + 0.1 * width, y + 0.9 * height)
            }
        }
    }
}
class StartSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height) {
    override val nextDirection: Direction = Right
}
class RightSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height) {
    override val nextDirection: Direction = Right
}
class LeftSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height) {
    override val nextDirection: Direction = Left
}
class UpSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height) {
    override val nextDirection: Direction = Up
}
class DownSquare(x: Double, y: Double, width: Double, height: Double) : PathSquare(x, y, width, height) {
    override val nextDirection: Direction = Down
}
