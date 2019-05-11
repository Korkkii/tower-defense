package game.towers

import game.GameState
import game.GraphicsComponent
import game.SelectedTower
import game.strokeCircle
import javafx.scene.canvas.GraphicsContext

class TowerGraphicsComponent : GraphicsComponent<Tower> {
    override fun draw(tower: Tower, graphics: GraphicsContext, state: GameState) {
        val square = tower.square
        val size = tower.size
        graphics.fill = tower.color
        graphics.fillOval(
            square.x + 0.5 * square.width - 0.5 * size,
            square.y + 0.5 * square.height - 0.5 * size,
            size,
            size
        )
        val stateT = state.state

        if (stateT is SelectedTower && stateT.tower == tower)
            graphics.strokeCircle(tower.rangeCircle)
    }
}
