package game.towers.projectiles

import game.GameState
import game.GraphicsComponent
import game.fillCircle
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class ProjectileGraphicsComponent : GraphicsComponent<Projectile> {
    override fun draw(entity: Projectile, graphics: GraphicsContext, state: GameState) {
        val (x, y) = entity.position
        graphics.fill = Color.WHITE
        graphics.fillCircle(Circle(x, y, entity.radius))
    }
}
