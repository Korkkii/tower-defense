package game.enemies

import game.AddEntity
import game.GameEntity
import game.GameState
import game.PhysicsComponent
import game.StunEnemyDebuff
import game.Vector
import game.circle

typealias EffectEntityConstructor = (
    origin: Vector,
    maxRadius: Double
) -> GameEntity

class BlastBossPhysicsComponent(
    private val blastRadius: Double,
    private val blastCooldown: Double,
    private val effectEntityConstructor: EffectEntityConstructor
) : PhysicsComponent<Enemy> {
    private val movementComponent = EnemyMovementComponent()
    private var timeUntilNext = blastCooldown

    override fun update(entity: Enemy, currentState: GameState, delta: Double) {
        if (entity.statusEffects.has(StunEnemyDebuff::class)) return

        movementComponent.update(entity, currentState, delta)

        timeUntilNext -= delta
        val stunArea = circle(entity.position, blastRadius)
        val towers = currentState.towers
        val towersNear = towers.filter { it.square.intersects(stunArea.boundsInLocal) }
        if (towersNear.isEmpty() || timeUntilNext > 0.0) return

        val effectEntity = effectEntityConstructor(
            entity.position,
            blastRadius
        )
        GameState.notify(AddEntity(effectEntity))
        timeUntilNext = blastCooldown
    }
}
