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
    properties: List<ProjectileProperty?>
) : GameEntity(
    calculateStartingPosition(originEntity)
) {
    var hasHit = false
    val properties = properties.filterNotNull()

    fun canDelete(): Boolean = hasHit

    override fun update(currentState: GameState, delta: Double) {
        type.physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        type.drawGraphics(this, graphics, state)
    }
}

fun calculateStartingPosition(entity: GameEntity): Vector = (entity as? Tower)?.square?.center ?: entity.position
