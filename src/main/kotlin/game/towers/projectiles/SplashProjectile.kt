package game.towers.projectiles

import game.Circle
import game.Enemy
import game.GameState
import game.MovementComponent
import game.Vector
import game.contains
import game.fillCircle
import game.towers.Tower
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class SplashProjectile(tower: Tower, private val target: Enemy) : Projectile() {
    override val velocity: Double = 200.0
    private val radius = 2.0
    private val damage = 3.0
    private val splashRange = 100.0
    private val movementComponent = MovementComponent(this)
    override var position: Vector = tower.square.center
    override var targetPosition: Vector = target.position

    override fun update(currentState: GameState, delta: Double) {
        if (hasHit) return
        targetPosition = target.position
        movementComponent.update(currentState, delta)
        val collisionBoundary = radius + target.radius
        val distance = (position - targetPosition).length
        val isHit = distance < collisionBoundary
        hasHit = isHit

        if (isHit) {
            val splashedEnemies = enemiesWithinSplashRange(currentState.enemies, position)
            splashedEnemies.forEach { it.health -= damage }
        }
    }

    private fun enemiesWithinSplashRange(enemies: List<Enemy>, position: Vector): List<Enemy> {
        val splashCircle = Circle(position, splashRange)
        return enemies.filter { splashCircle.contains(it.position) }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = if (hasHit) Color.CRIMSON else Color.WHITE
        graphics.fillCircle(Circle(position.x, position.y, radius))
    }
}
