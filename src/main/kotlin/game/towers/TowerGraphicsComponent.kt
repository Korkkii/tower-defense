package game.towers

import game.GameState
import game.GraphicsComponent
import game.TowerSelected
import game.strokeCircle
import javafx.scene.canvas.GraphicsContext

class TowerGraphicsComponent : GraphicsComponent<Tower> {
    override fun draw(entity: Tower, graphics: GraphicsContext, state: GameState) {
        val square = entity.square
        val type = entity.type
        val size = type.size
        graphics.fill = type.color
        graphics.fillOval(
            square.x + 0.5 * square.width - 0.5 * size,
            square.y + 0.5 * square.height - 0.5 * size,
            size,
            size
        )
        val stateT = state.state

        if (stateT is TowerSelected && stateT.tower == entity)
            graphics.strokeCircle(entity.rangeCircle)
    }
}
