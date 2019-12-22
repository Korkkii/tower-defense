package game.towers.projectiles

import game.GameState
import game.fillCircle
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

fun drawProjectile(entity: Projectile, graphics: GraphicsContext, state: GameState) {
    val (x, y) = entity.position
    graphics.fill = Color.WHITE
    graphics.fillCircle(Circle(x, y, entity.type.radius))
}

fun drawStraightLine(entity: Projectile, graphics: GraphicsContext, state: GameState) {
    graphics.stroke = Color.WHITE
    val origin = entity.position
    val endPoint = entity.target.position
    graphics.strokeLine(origin.x, origin.y, endPoint.x, endPoint.y)
}
