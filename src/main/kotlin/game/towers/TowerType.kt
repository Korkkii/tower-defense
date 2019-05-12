package game.towers

import game.BuildAreaSquare
import game.towers.projectiles.LightProjectile
import game.towers.projectiles.SingleHitProjectile
import game.towers.projectiles.SplashProjectile
import javafx.scene.paint.Color

open class TowerType(
    val name: String,
    val cost: Int,
    val range: Double,
    val fireRate: Double,
    val color: Color,
    val physicsComponent: PhysicsComponent<Tower>
) {
    val size = 10.0
    val graphicsComponent = TowerGraphicsComponent()
    fun create(square: BuildAreaSquare) = Tower(square, this)

    companion object {
        private val singleHit = TowerType(
            "Single hit tower",
            30,
            75.0,
            1.0,
            Color.AQUAMARINE,
            ShootingComponent(::SingleHitProjectile)
        )
        private val splash = TowerType(
            "Splash tower",
            30,
            75.0,
            1.0,
            Color.ROSYBROWN,
            ShootingComponent(::SplashProjectile)
        )
        private val light = TowerType(
            "Light tower",
            40,
            50.0,
            200.0,
            Color.ANTIQUEWHITE,
            ShootingComponent(::LightProjectile)
        )
        private val fire = TowerType(
            "Fire tower",
            40,
            30.0,
            1.0,
            Color.ORANGERED,
            AreaEffectComponent(0.1)
        )
        val towerTypes = listOf(
            singleHit, splash, light, fire
        )
    }
}
