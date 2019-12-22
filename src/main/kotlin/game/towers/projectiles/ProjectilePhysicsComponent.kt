package game.towers.projectiles

import game.GameState
import game.PhysicsComponent
import game.TowerMissedEvent
import game.enemies.Enemy
import game.find
import game.times
import game.towers.Tower
import kotlin.random.Random

class ProjectilePhysicsComponent(private val onHit: (Projectile, Enemy, GameState) -> Unit) :
    PhysicsComponent<Projectile> {
    override fun update(entity: Projectile, currentState: GameState, delta: Double) {
        if (entity.hasHit) return
        val target = entity.target

        val direction = (target.position - entity.position).unitVector()
        entity.position += entity.type.velocity * direction * delta

        val collisionBoundary = entity.type.radius + target.type.radius
        val distance = (entity.position - target.position).length
        val isHit = distance < collisionBoundary
        entity.hasHit = isHit

        if (isHit) {
            val blindedProp = entity.properties.find(BlindedProperty::class)
            val missChance = blindedProp?.missChance ?: 0.0
            val hitChance = Random.nextDouble()

            if (hitChance < missChance) {
                val originEntity = (entity.originEntity as? Tower)
                originEntity?.let { GameState.notify(TowerMissedEvent(it)) }
            } else {
                onHit(entity, entity.target, currentState)
            }
        }
    }
}
