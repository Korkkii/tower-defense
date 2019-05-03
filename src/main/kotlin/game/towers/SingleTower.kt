package game.towers

import game.BuildAreaSquare
import game.GameState
import game.SelectedTower
import game.strokeCircle
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class SingleTower(override val square: BuildAreaSquare) : Tower() {
    val cost = 30
    private val size = 30.0
    override val range = 75.0
    override val fireRate = 1.0
    private val shootingComponent = ShootingComponent()

    init {
        square.tower = this
    }

    override fun update(currentState: GameState, delta: Double) {
        shootingComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.AQUAMARINE
        graphics.fillOval(
            square.x + 0.5 * square.width - 0.5 * size,
            square.y + 0.5 * square.height - 0.5 * size,
            size,
            size
        )
        val stateT = state.state
        if (stateT is SelectedTower && stateT.tower == this) graphics.strokeCircle(rangeCircle)
    }
}
