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
        val damageText = DamageText(event.enemy.position, event.damage)
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
        graphics.stroke = Color.RED
        graphics.strokeText("Miss", x, y)
    }
}

class DamageText(position: Vector, private val damage: Double) : GameEntity(position) {
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
        graphics.font = Font.font("Helvetica", FontWeight.THIN, 4.0)
        graphics.stroke = Color.BLACK
        graphics.strokeText("$damage", x, y)
    }
}
