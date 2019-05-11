package game.towers

import game.BuildAreaSquare
import game.Enemy
import game.GameState
import game.towers.projectiles.Projectile
import game.towers.projectiles.SingleHitProjectile
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class FireTower(square: BuildAreaSquare) : AreaEffectTower(square) {
    override val cost: Int = 10
    override val range: Double = 30.0
    override val fireRate: Double = 1.0
    override val size = 10.0
    override val color: Color = Color.ORANGERED
    override val shootingComponent = AreaEffectComponent()
    override val graphicsComponent = TowerGraphicsComponent()
    override val areaEffectDamage = 0.1

    override fun update(currentState: GameState, delta: Double) {
        shootingComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}
