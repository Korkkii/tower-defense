package game.towers

import game.BuildAreaSquare
import game.Enemy
import game.towers.projectiles.Projectile

abstract class ProjectileTower(square: BuildAreaSquare) : Tower(square) {
    abstract val projectileConstructor: ((Tower, Enemy) -> Projectile)
}
