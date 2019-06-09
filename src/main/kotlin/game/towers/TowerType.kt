package game.towers

import game.BossType
import game.BuildAreaSquare
import game.EnemyType
import game.GameState
import game.PhysicsComponent
import game.towers.projectiles.ProjectileType
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
        // private val singleHit = TowerType(
        //     "Single hit tower",
        //     10,
        //     35.0,
        //     1.0,
        //     Color.AQUAMARINE,
        //     ShootingComponent.with(ProjectileType.singleHitProjectile)
        // )
        // private val singleHit2 = TowerType(
        //     "Single hit tower",
        //     20,
        //     45.0,
        //     1.2,
        //     Color.AQUAMARINE,
        //     ShootingComponent.with(ProjectileType.singleHit2Projectile)
        // )
        private val water = TowerType(
            "Water tower",
            30,
            35.0,
            1.0,
            Color.NAVY,
            ShootingComponent.with(ProjectileType.splashProjectile)
        )
        private val light = TowerType(
            "Light tower",
            40,
            35.0,
            1.0,
            Color.ANTIQUEWHITE,
            ShootingComponent.withMultiShot(ProjectileType.lightProjectile)
        )
        private val fire = TowerType(
            "Fire tower",
            40,
            30.0,
            1.0,
            Color.ORANGERED,
            ShootingComponent.with(ProjectileType.applyDoTProjectile)
            // AreaEffectComponent.with(0.1)
        )
        private val wind =
            TowerType("Wind tower", 5, 35.0, 1.0, Color.SILVER, ShootingComponent.with(ProjectileType.bounceProjectile))
        /*
        * Towers
        * - X Fire
        *   Increasing DoT debuff?
        * - X Water
        *   Splash tower
        * - X Wind
        *   Bounce attack
        * - Nature
        *   Attacks ground, aftershock on spot / small area of constant damage or slow?
        * - Metal
        *   Goes through multiple opponents? Multishot? Swap with light and make light multishot?
        *   Throws roadspikes that do damage for X seconds?
        * - X Light
        *   Increasing attack speed / damage? Reset at switch / after CD?
        *   Instant multishot?
        * */
        val towerTypes = listOf(
            light,
            fire,
            wind,
            water
        )

        // val requirements = mapOf(light to EnemyType.boss)
        val requirements = mapOf<TowerType, BossType>()
        val upgrades = mapOf<TowerType, List<TowerType>>()
    }
}
