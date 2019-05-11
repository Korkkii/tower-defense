package game.towers

import game.BuildAreaSquare

abstract class AreaEffectTower(square: BuildAreaSquare) : Tower(square) {
    abstract val areaEffectDamage: Double
}
