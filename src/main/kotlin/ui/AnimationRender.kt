package ui

import game.AddEntity
import game.DeleteEntity
import game.EnemyTakeDamageEvent
import game.GameEntity
import game.GameState
import game.TowerMissedEvent
import game.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

object AnimationRender {
    init {
        GameState.subscribe(TowerMissedEvent::class, ::createMissText)
        GameState.subscribe(EnemyTakeDamageEvent::class, ::createDamageText)
    }

    private fun createMissText(event: TowerMissedEvent) {
        val missText = MissText(event.tower.square.center)
        GameState.notify(AddEntity(missText))
    }

    private fun createDamageText(event: EnemyTakeDamageEvent) {
        val damageText = DamageText(event)
        GameState.notify(AddEntity(damageText))
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
        graphics.fill = Color.RED
        graphics.fillText("Miss", x, y)
    }
}

class DamageText(event: EnemyTakeDamageEvent) : GameEntity(event.enemy.position) {
    private val damage = event.damage
    private val fontWeight = if (event.isCrit) FontWeight.BOLD else FontWeight.THIN
    private val color = if (event.isCrit) Color.RED else Color.BLACK
    private val size = if (event.isCrit) 5.0 else 4.0
    private var lifeTime = 1.0
    private var velocity = 2.0

    override fun update(currentState: GameState, delta: Double) {
        lifeTime -= delta
        position -= Vector(0.0, velocity * delta)

        if (lifeTime <= 0.0) {
            GameState.notify(DeleteEntity(this))
        }
    }

    override fun draw(graphics: GraphicsContext, state: GameState) {
        val (x, y) = position
        graphics.font = Font.font("Helvetica", fontWeight, size)
        graphics.fill = color
        graphics.fillText("%.1f".format(damage), x, y)
    }
}
