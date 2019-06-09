package game.towers.projectiles

import game.Enemy
import game.GameEntity
import game.GameState
import game.Vector
import javafx.scene.canvas.GraphicsContext

class Projectile(position: Vector, val target: Enemy, val type: ProjectileType, val properties: ProjectileProperties = NoProperties) : GameEntity(position) {
    private val graphicsComponent = ProjectileGraphicsComponent()
    var hasHit = false

    fun canDelete(): Boolean = hasHit

    override fun update(currentState: GameState, delta: Double) {
        type.physicsComponent.update(this, currentState, delta)
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphicsComponent.draw(this, graphics, state)
    }
}

interface ProjectileProperties
object NoProperties : ProjectileProperties
data class BounceProperties(val bouncesLeft: Int) : ProjectileProperties
