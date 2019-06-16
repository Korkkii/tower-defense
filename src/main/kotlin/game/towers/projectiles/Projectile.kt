package game.towers.projectiles

import game.enemies.Enemy
import game.GameEntity
import game.GameState
import game.Vector
import game.find
import game.towers.Tower
import javafx.scene.canvas.GraphicsContext
import kotlin.reflect.KClass

class Projectile(
    val originEntity: GameEntity,
    val target: Enemy,
    val type: ProjectileType,
    val properties: ProjectileProperties = NoProperties
) : GameEntity(
    calculateStartingPosition(originEntity)
) {
    var hasHit = false

    fun canDelete(): Boolean = hasHit

    override fun update(currentState: GameState, delta: Double) {
        type.physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        type.graphicsComponent.draw(this, graphics, state)
    }
}

fun calculateStartingPosition(entity: GameEntity): Vector = (entity as? Tower)?.square?.center ?: entity.position

interface ProjectileProperty
open class ProjectileProperties(vararg properties: ProjectileProperty?) {
    private val all = properties.filterNotNull()
    fun <U : ProjectileProperty> find(type: KClass<U>): U? = all.find(type)
}
object NoProperties : ProjectileProperties()
object NoProperty : ProjectileProperty
data class BounceProperty(val bouncesLeft: Int) : ProjectileProperty
data class IncreasedDamageProperty(val enemyCount: Int) : ProjectileProperty
data class BlindedProperty(val missChance: Double) : ProjectileProperty
