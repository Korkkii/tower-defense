package game

import game.towers.Tower
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class FireBossPhysicsComponent : PhysicsComponent<Enemy> {
    private val movementComponent = EnemyMovementComponent()
    private val stunCooldown = 5.0
    private var timeUntilNext = stunCooldown
    private val stunCircleRadius = 15.0

    override fun update(entity: Enemy, currentState: GameState, delta: Double) {
        movementComponent.update(entity, currentState, delta)

        timeUntilNext -= delta
        val stunArea = Circle(entity.position, stunCircleRadius)
        val towers = currentState.towers
        val towersNear = towers.filter { it.square.intersects(stunArea.boundsInLocal) }
        if (towersNear.isEmpty() || timeUntilNext > 0.0) return

        GameState.notify(AddEntity(StunEffect(towersNear, stunCircleRadius, entity.position)))
        timeUntilNext = stunCooldown
    }
}

class StunEffect(private val towersNear: List<Tower>, private val maxRadius: Double, position: Vector) : GameEntity(position) {
    private var radius = 1.0
    private val animationTime = 0.2 // seconds
    private val growthRate = maxRadius / animationTime

    override fun update(currentState: GameState, delta: Double) {
        radius += growthRate * delta

        if (radius >= maxRadius) {
            towersNear.forEach { it.statusEffects.currentEffects += StunDebuff(2.0) }
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = Color.ORANGERED
        graphics.fillCircle(Circle(position, radius))
    }
}

