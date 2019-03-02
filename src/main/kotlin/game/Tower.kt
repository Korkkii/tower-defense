package game

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class Tower(val square: BuildAreaSquare): GameEntity {
    private val size = 30.0
    private val range = 75.0
    private val rangeCircle = Circle(square.center, range)
    private val fireRate = 1.0 // Rate per second
    private var firingCooldown = 0.0

    init {
        square.tower = this
    }

    override fun update(currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0

        enemies.forEach {
            if (withinRange(it) && canFire) {
                currentState.projectiles += Projectile(this, it)
                val secondsUntilNext = 1 / fireRate
                firingCooldown = secondsUntilNext
            }
        }
    }

    private fun withinRange(enemy: Enemy): Boolean {
        val collisionBoundary  = rangeCircle.radius + enemy.radius
        val distance = (rangeCircle.center() - enemy.position).length
        return distance < collisionBoundary
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.AQUAMARINE
        graphics.fillOval(square.x + 0.5 * square.width - 0.5 * size,
            square.y + 0.5 * square.height - 0.5 * size,
            size,
            size)
        val stateT = state.state
        if (stateT is SelectedTower && stateT.tower == this) graphics.strokeCircle(rangeCircle)
    }
}