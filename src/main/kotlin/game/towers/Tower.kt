package game.towers

import game.BuildAreaSquare
import game.Circle
import game.Enemy
import game.GameEntity
import game.towers.projectiles.Projectile
import javafx.scene.paint.Color

abstract class Tower(val square: BuildAreaSquare) : GameEntity {
    abstract val cost: Int
    abstract val range: Double
    abstract val fireRate: Double // Rate per second
    abstract val projectileConstructor: ((Tower, Enemy) -> Projectile)
    abstract val size: Double
    abstract val color: Color
    abstract val shootingComponent: ShootingComponent
    abstract val graphicsComponent: TowerGraphicsComponent
    val rangeCircle by lazy { Circle(square.center, range) }

    init {
        square.tower = this
    }

    fun deleteTower() {
        square.tower = null
    }

    companion object {
        val allTowers = listOf(
            (::SingleTower to "Single hit tower"),
            (::SplashTower to "Splash tower"),
            (::LightTower to "Light tower")
        )
    }
}
