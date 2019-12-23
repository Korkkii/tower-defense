package ui

import game.AddEntity
import game.DeleteEntity
import game.GameEntity
import game.GameState
import game.NotEnoughMoney
import game.position
import game.withOpacity
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import kotlin.math.max
import kotlin.math.min

class AlertHandler(private val locationRectangle: Rectangle) {
    init {
        GameState.subscribe(NotEnoughMoney::class) {
            GameState.notify(AddEntity(AlertBox(locationRectangle, "Not enough money")))
        }
    }
}

class AlertBox(private val rectangle: Rectangle, private val text: String) : GameEntity(rectangle.position()) {
    private var lifeTime = 2.5
    private val fadeOutStartTime = 0.5
    private val rectangleBaseColor = Color.KHAKI
    private val textBaseColor = Color.BLACK

    override fun update(currentState: GameState, delta: Double) {
        lifeTime -= delta

        if (lifeTime <= 0.0) {
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        val opacity = clamp(lifeTime / fadeOutStartTime)
        val color = if (lifeTime <= fadeOutStartTime) rectangleBaseColor.withOpacity(opacity) else rectangleBaseColor
        graphics.fill = color.darker()

        graphics.fillRectangle(rectangle)

        graphics.font = Font.font("Helvetica", FontWeight.THIN, 7.0)
        graphics.fill = if (lifeTime <= fadeOutStartTime) textBaseColor.withOpacity(opacity) else textBaseColor
        val textX = rectangle.x + (rectangle.width * 0.1)
        val textY = rectangle.y + (rectangle.height * 0.4)

        graphics.fillText(text, textX, textY, rectangle.width * 0.8)
    }
}

private fun clamp(num: Double): Double = min(max(num, 0.0), 1.0)
