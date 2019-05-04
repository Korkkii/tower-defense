package game.towers

import game.BuildAreaSquare
import game.Circle
import game.Enemy
import game.GameEntity
import game.towers.projectiles.Projectile

abstract class Tower(val square: BuildAreaSquare) : GameEntity {
    abstract val cost: Int
    abstract val range: Double
    abstract val fireRate: Double // Rate per second
    abstract val projectileConstructor: ((Tower, Enemy) -> Projectile)
    val rangeCircle by lazy { Circle(square.center, range) }

    init {
        square.tower = this
    }

    fun deleteTower() {
        square.tower = null
    }
}
