package game.enemies

import game.BlindDebuff
import game.DeleteEntity
import game.ExplosionDebuff
import game.GameEntity
import game.GameState
import game.StatusEffect
import game.StunTowerDebuff
import game.Vector
import game.circle
import game.fillCircle
import game.towers.Tower
import game.withOpacity
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

open class BlastEffectEntity protected constructor(
    private val origin: Vector,
    private val maxRadius: Double,
    private val effectColor: Color,
    private val effectedEntitySelector: (currentState: GameState, blastArea: Circle) -> List<GameEntity>,
    private val blastEffect: (entity: GameEntity) -> Unit
) : GameEntity(origin) {
    private var radius = 1.0
    private val animationTime = 0.15 // seconds
    private val growthRate = maxRadius / animationTime

    override fun update(currentState: GameState, delta: Double) {
        radius += growthRate * delta

        if (radius >= maxRadius) {
            val blastArea = circle(origin, maxRadius)
            val entitiesWithinBlastArea = effectedEntitySelector(currentState, blastArea)
            entitiesWithinBlastArea.forEach(blastEffect)
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        graphics.fill = effectColor
        graphics.fillCircle(circle(position, radius))
    }
}

private fun causeStatusEffect(effectConstructor: (duration: Double) -> StatusEffect<Tower>): (GameEntity) -> Unit =
    { entity: GameEntity ->
        val tower = (entity as? Tower)
        tower?.let { it.statusEffects += effectConstructor(3.0) }
    }

private fun getWithinAreaTowers(currentState: GameState, blastArea: Circle) =
    currentState.towers.filter { it.square.intersects(blastArea.boundsInLocal) }

class StunEffect(origin: Vector, maxRadius: Double) :
    BlastEffectEntity(origin, maxRadius, Color.ORANGERED, ::getWithinAreaTowers, causeStatusEffect(::StunTowerDebuff))

class FlashEffect(origin: Vector, maxRadius: Double) :
    BlastEffectEntity(
        origin,
        maxRadius,
        Color.LIGHTYELLOW.withOpacity(0.4),
        ::getWithinAreaTowers,
        causeStatusEffect(::BlindDebuff)
    )

fun getWithinAreaEnemies(currentState: GameState, blastArea: Circle) =
    currentState.enemies.filter { it.collisionCircle().intersects(blastArea.boundsInLocal) }

class ExplosionEffect(origin: Vector, maxRadius: Double, explosionStatus: ExplosionDebuff) :
    BlastEffectEntity(origin, maxRadius, Color.ORANGERED, ::getWithinAreaEnemies, { entity ->
        val enemy = (entity as? Enemy)
        enemy?.let {
            val explosionDamage = explosionStatus.damagePerStack * explosionStatus.stackSize
            it.takeDamage(explosionDamage)
        }
    })
