package game.towers

import game.AttackSpeedBoost
import game.DamageBoost
import game.GameState
import game.PhysicsComponent
import game.board.BuildAreaSquare
import game.enemies.BossType
import game.towers.projectiles.ProjectileType
import javafx.scene.paint.Color

data class TowerType(
    val name: String,
    val cost: Int,
    val range: Double,
    val baseFireRate: Double,
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
            ShootingComponent.with(ProjectileType.lightProjectile, ::onShootMultiTarget)
        )
        private val fire = TowerType(
            "Fire tower",
            40,
            30.0,
            1.0,
            Color.ORANGERED,
            ShootingComponent.with(ProjectileType.applyDoTProjectile)
        )
        private val wind =
            TowerType("Wind tower", 5, 35.0, 1.0, Color.SILVER, ShootingComponent.with(ProjectileType.bounceProjectile))
        private val nature = TowerType(
            "Nature tower",
            10,
            35.0,
            1.0,
            Color.DARKSEAGREEN,
            physicsComponentConstructor = ShootingComponent.with(ProjectileType.damagePerCreepProjectile)
        )
        private val metal = TowerType(
            "Metal tower",
            10,
            35.0,
            1.5,
            Color.DARKSEAGREEN,
            physicsComponentConstructor = {
                AcceleratingShootingComponent(
                    ProjectileType.singleHitProjectile,
                    ::onShootSingleTarget
                )
            }
        )

        private val blacksmith = TowerType(
            "Blacksmith tower",
            50,
            30.0,
            1.0,
            Color.DARKRED,
            physicsComponentConstructor = { BuffOthersPhysicsComponent { DamageBoost(5.0, 0.30) } }
        )
        private val quickshot = TowerType(
            "Quickshot tower",
            50,
            30.0,
            1.0,
            Color.LIGHTSKYBLUE,
            physicsComponentConstructor = { BuffOthersPhysicsComponent { AttackSpeedBoost(5.0, 0.30) } }
        )
        /*
        * Towers
        * - X Fire
        *   Increasing DoT debuff?
        * - X Water
        *   Splash tower
        * - X Wind
        *   Bounce attack
        * - X Nature
        *   Attacks ground, aftershock on spot / small area of constant damage or slow?
        *   X% more damage per enemy in area
        * - X Metal
        *   Goes through multiple opponents? Multishot? Swap with light and make light multishot?
        *   Throws roadspikes that do damage for X seconds?
        * - X Light
        *   Increasing attack speed / damage? Reset at switch / after CD?
        *   Instant multishot?
        *
        * Tower idea
        *   - Green TD style spells?
        *   - Chance for multishot / extra projectile?
        *   - Random damage tower --> Low and high base, avg a bit above avg
        *   - Pierce through multiple
        *
        * */
        val towerTypes = listOf(
            light,
            fire,
            wind,
            water,
            nature,
            metal,
            blacksmith,
            quickshot
        )

        /*
        * TODO:
        *  - 3 levels for each basic tower
        *  - New towers for
        *   - X Metal + fire - Blacksmith i.e. damage +% for others
        *   - Metal + X
        *   - X Wind + light - Quickshot - attack speed +% for others
        *   - Wind + water - Chance for extra projectile? High pressure water something??
        *   - Fire + X - Shockwave hit? I.e. creates projectile to all enemies within range on hit
        *   - Fire + nature - do more damage on lower health %?
        *   - Light + X - Magic?
        *   - Light + nature - Transport X space backwards? No meaning in circle?
        *   - Nature + light - Life tower? No lives so what instead?
        *   - Nature + metal - Critical strike?
        *   - Water + wind - Frost --> slow
        *   - Water + fire - Steam tower --> AoE damage
        *
        * TODO: triples
        *   - Metal + water + air - Rust -> add damage taken
        *
        * */

        // val requirements = mapOf(light to EnemyType.boss)
        // val requirements = mapOf(
        //     light to lightBoss,
        //     water to waterBoss,
        //     fire to fireBoss,
        //     wind to windBoss,
        //     metal to metalBoss,
        //     nature to natureBoss
        // )
        val requirements = mapOf<TowerType, BossType>()
        val upgrades = mapOf<TowerType, List<TowerType>>()
    }
}
