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
    override val size = 10.0
    override val range = 75.0
    override val fireRate = 1.0
    override val color: Color = Color.AQUAMARINE
    override val shootingComponent = ShootingComponent()
    override val graphicsComponent = TowerGraphicsComponent()

    override val projectileConstructor: (Tower, Enemy) -> Projectile = ::SingleHitProjectile

    override fun update(currentState: GameState, delta: Double) {
        shootingComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}
