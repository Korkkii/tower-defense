package game.towers

import game.BuildAreaSquare
import game.EnemyType
import game.GameState
import game.towers.projectiles.LightProjectile
import game.towers.projectiles.SingleHitProjectile
import game.towers.projectiles.SplashProjectile
import javafx.scene.paint.Color

data class TowerType(
    val name: String,
    val cost: Int,
    val range: Double,
    val fireRate: Double,
    val color: Color,
    val physicsComponentConstructor: () -> PhysicsComponent<Tower>
) {
    val size = 10.0
    val graphicsComponent = TowerGraphicsComponent()

    fun create(square: BuildAreaSquare) = Tower(square, this)

    fun isAvailable(): Boolean {
        val requirement = requirements[this] ?: return true
        return requirement in GameState.instance.defeatedBosses
    }

    companion object {
        private val singleHit = TowerType(
            "Single hit tower",
            10,
            75.0,
            1.0,
            Color.AQUAMARINE,
            ShootingComponent.with(::SingleHitProjectile)
        )
        private val singleHit2 = TowerType(
            "Single hit tower",
            20,
            90.0,
            1.2,
            Color.AQUAMARINE,
            ShootingComponent.with(::SingleHitProjectile)
        )
        private val splash = TowerType(
            "Splash tower",
            30,
            75.0,
            1.0,
            Color.ROSYBROWN,
            ShootingComponent.with(::SplashProjectile)
        )
        private val light = TowerType(
            "Light tower",
            40,
            50.0,
            200.0,
            Color.ANTIQUEWHITE,
            ShootingComponent.with(::LightProjectile)
        )
        private val fire = TowerType(
            "Fire tower",
            40,
            30.0,
            1.0,
            Color.ORANGERED,
            AreaEffectComponent.with(0.1)
        )
        val towerTypes = listOf(
            singleHit, light, fire
        )

        val requirements = mapOf(light to EnemyType.boss)

        val upgrades = mapOf(singleHit to listOf(singleHit2, splash))
    }
}
