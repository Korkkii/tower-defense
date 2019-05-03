package game.towers

import game.BuildAreaSquare
import game.Circle
import game.GameEntity

abstract class Tower : GameEntity {
    abstract val square: BuildAreaSquare
    abstract val range: Double
    abstract val fireRate: Double // Rate per second
    val rangeCircle by lazy { Circle(square.center, range) }
}
