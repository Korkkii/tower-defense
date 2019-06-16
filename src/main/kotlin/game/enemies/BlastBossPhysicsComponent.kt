package game.enemies

import game.AddEntity
import game.BlindDebuff
import game.Circle
import game.DeleteEntity
import game.GameEntity
import game.GameState
import game.PhysicsComponent
import game.StatusEffect
import game.StunDebuff
import game.Vector
import game.fillCircle
import game.towers.Tower
import game.withOpacity
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

typealias EffectEntityConstructor = (List<Tower>,
    maxRadius: Double,
    position: Vector
) -> GameEntity

class BlastBossPhysicsComponent(
    private val blastRadius: Double,
    private val blastCooldown: Double,
    private val effectEntityConstructor: EffectEntityConstructor
) : PhysicsComponent<Enemy> {
    private val movementComponent = EnemyMovementComponent()
    private var timeUntilNext = blastCooldown

    override fun update(entity: Enemy, currentState: GameState, delta: Double) {
        movementComponent.update(entity, currentState, delta)

        timeUntilNext -= delta
        val stunArea = Circle(entity.position, blastRadius)
        val towers = currentState.towers
        val towersNear = towers.filter { it.square.intersects(stunArea.boundsInLocal) }
        if (towersNear.isEmpty() || timeUntilNext > 0.0) return

        val effectEntity = effectEntityConstructor(
            towersNear,
            blastRadius,
            entity.position
        )
        GameState.notify(AddEntity(effectEntity))
        timeUntilNext = blastCooldown
    }
}

open class EffectEntity protected constructor(
    private val towersNear: List<Tower>,
    private val maxRadius: Double,
    private val effectConstructor: (Double) -> StatusEffect<Tower>,
    private val effectColor: Color,
    position: Vector
) : GameEntity(position) {
    private var radius = 1.0
    private val animationTime = 0.2 // seconds
    private val growthRate = maxRadius / animationTime

    override fun update(currentState: GameState, delta: Double) {
        radius += growthRate * delta

        if (radius >= maxRadius) {
            towersNear.forEach { it.statusEffects += effectConstructor(3.0) }
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = effectColor
        graphics.fillCircle(Circle(position, radius))
    }
}

class StunEffect(towersNear: List<Tower>, maxRadius: Double, position: Vector) :
    EffectEntity(towersNear, maxRadius, ::StunDebuff, Color.ORANGERED, position)

class FlashEffect(towersNear: List<Tower>, maxRadius: Double, position: Vector) :
    EffectEntity(towersNear, maxRadius, ::BlindDebuff, Color.LIGHTYELLOW.withOpacity(0.4), position)
