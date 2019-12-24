package game.enemies

import game.BlindDebuff
import game.circle
import game.DeleteEntity
import game.GameEntity
import game.GameState
import game.StatusEffect
import game.StunTowerDebuff
import game.Vector
import game.fillCircle
import game.towers.Tower
import game.withOpacity
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

open class BlastEffectEntity protected constructor(
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
        graphics.fillCircle(circle(position, radius))
    }
}

class StunEffect(towersNear: List<Tower>, maxRadius: Double, position: Vector) :
    BlastEffectEntity(towersNear, maxRadius, ::StunTowerDebuff, Color.ORANGERED, position)

class FlashEffect(towersNear: List<Tower>, maxRadius: Double, position: Vector) :
    BlastEffectEntity(towersNear, maxRadius, ::BlindDebuff, Color.LIGHTYELLOW.withOpacity(0.4), position)
