package game.towers.projectiles

import game.AddEntity
import game.DeleteEntity
import game.enemies.Enemy
import game.GameEntity
import game.GameState
import game.times
import game.PhysicsComponent
import game.Vector
import game.towers.Tower
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
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
                val originEntity = (entity.originEntity as? Tower)?.square?.center
                originEntity?.let { GameState.notify(AddEntity(MissText(it))) }
            } else {
                onHit(entity, entity.target, currentState)
            }
        }
    }
}

class MissText(position: Vector) : GameEntity(position) {
    private var lifeTime = 2.0
    private var velocity = 1.0

    override fun update(currentState: GameState, delta: Double) {
        lifeTime -= delta
        position -= Vector(0.0, velocity * delta)

        if (lifeTime <= 0.0) {
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        val (x, y) = position
        graphics.font = Font(7.0)
        graphics.stroke = Color.RED
        graphics.strokeText("Miss", x, y)
    }
}
