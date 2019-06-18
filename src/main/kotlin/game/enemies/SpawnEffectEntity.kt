package game.enemies

import game.Circle
import game.DeleteEntity
import game.GameEntity
import game.GameState
import game.Vector
import game.fillCircle
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.PI
import kotlin.math.sin

class SpawnEffectEntity(position: Vector, endPosition: Vector, val endRadius: Double, val onEnd: (Vector) -> Unit) : GameEntity(position) {
    private val animationTime = 1.0
    private val direction = (endPosition - position).unitVector()
    private val speed = (endPosition - position).length / animationTime
    private var time = 0.0

    override fun update(currentState: GameState, delta: Double) {
        time += delta
        position += direction * speed * delta

        if (time >= animationTime) {
            onEnd(position)
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.NAVY.brighter()
        val radius = endRadius * sin(time * PI) + 0.4 * endRadius
        graphics.fillCircle(Circle(position, radius))
    }
}
