package game.towers

import game.BuildAreaSquare
import game.Enemy
import game.GameState
import game.SelectedTower
import game.strokeCircle
import game.towers.projectiles.LightProjectile
import game.towers.projectiles.Projectile
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class LightTower(square: BuildAreaSquare) : Tower(square) {
    override val cost = 40
    private val size = 10.0
    override val range = 50.0
    override val fireRate = 200.0
    private val shootingComponent = ShootingComponent()

    override val projectileConstructor: (Tower, Enemy) -> Projectile = ::LightProjectile

    override fun update(currentState: GameState, delta: Double) {
        shootingComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.ANTIQUEWHITE
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
