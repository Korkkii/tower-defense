package game.towers

import game.BuildAreaSquare
import game.Enemy
import game.GameState
import game.SelectedTower
import game.strokeCircle
import game.towers.projectiles.Projectile
import game.towers.projectiles.SingleHitProjectile
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class SingleTower(square: BuildAreaSquare) : Tower(square) {
    override val cost = 30
    private val size = 10.0
    override val range = 75.0
    override val fireRate = 1.0
    private val shootingComponent = ShootingComponent()

    override val projectileConstructor: (Tower, Enemy) -> Projectile = ::SingleHitProjectile

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
