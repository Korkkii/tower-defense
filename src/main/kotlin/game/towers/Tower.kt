package game.towers

import game.BuildAreaSquare
import game.Circle
import game.Enemy
import game.GameEntity
import game.GameState
import game.Projectile
import game.SelectedTower
import game.center
import game.strokeCircle
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

interface Tower : GameEntity {
    val square: BuildAreaSquare
}
