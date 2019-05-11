package game.towers

import game.BuildAreaSquare
import game.Enemy
import game.GameState
import game.towers.projectiles.LightProjectile
import game.towers.projectiles.Projectile
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class LightTower(square: BuildAreaSquare) : Tower(square) {
    override val cost = 40
    override val size = 10.0
    override val range = 50.0
    override val fireRate = 200.0
    override val color: Color = Color.ANTIQUEWHITE
    override val shootingComponent = ShootingComponent()
    override val graphicsComponent = TowerGraphicsComponent()

    override val projectileConstructor: (Tower, Enemy) -> Projectile = ::LightProjectile

    override fun update(currentState: GameState, delta: Double) {
        shootingComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}
