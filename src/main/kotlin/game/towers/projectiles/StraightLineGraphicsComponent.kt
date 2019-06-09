package game.towers.projectiles

import game.GameState
import game.GraphicsComponent
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class StraightLineGraphicsComponent : GraphicsComponent<Projectile> {
    override fun draw(entity: Projectile, graphics: GraphicsContext, state: GameState) {
        graphics.stroke = Color.WHITE
        val origin = entity.position
        val endPoint = entity.target.position
        graphics.strokeLine(origin.x, origin.y, endPoint.x, endPoint.y)
    }
}
