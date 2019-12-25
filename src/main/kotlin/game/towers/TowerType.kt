package game.towers

import game.GameState
import game.PhysicsComponent
import game.board.BuildAreaSquare
import game.data.GameData
import game.towers.projectiles.ProjectileType
import game.towers.projectiles.ProjectileTypeBuilder
import javafx.scene.paint.Color
import kotlin.properties.Delegates

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
        val requirements = GameData.towerUpgradeRequirements[this] ?: return true
        return requirements.all { it in GameState.instance.defeatedBosses }
    }

    companion object {
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
        // val towerTypes = listOf(
        //     basic,
        //     light,
        //     fire,
        //     wind,
        //     water,
        //     nature,
        //     metal,
        //     blacksmith,
        //     quickshot,
        //     critTower,
        //     steamTower,
        //     frostTower,
        //     photosynthesis,
        //     rust, // TODO: Prefer targets without debuff?
        //     iceberg,
        //     explosion,
        //     gold // TODO: Prefer lowest HP targets?
        // )

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
        *   - X Nature + metal - Critical strike?e
        *   - X Water + wind - Frost --> slow
        *   - X Water + fire - Steam tower --> AoE damage
        *
        * TODO: triples
        *   - X Metal + water + wind - Rust -> add damage taken
        *   - X Wind + Light + Water - Iceberg -> Stun
        *   - X Fire + nature + metal - Explosion -> gather stacks to target which explode on target's death
        *   - Light + Fire + Nature - Gold -> Extra gold from kills
        *
        *
        *
        * - Gold tower
        * - Sniper?
        *   - Shoots as long as a target is dead, regardless of range
        * - Gain damage buff per target killed in range
        * - Explosion tower, gather stacks to target which explode on target's death
        * - Longer range hit, larger damage
        * - Quake - on hit causes shockwave which hits neighbours
        *
        * */
    }
}

fun towerType(block: TowerTypeBuilder.() -> Unit) = TowerTypeBuilder().apply(block).build()
fun towerTypeBase(block: TowerTypeBuilder.() -> Unit) = TowerTypeBuilder().apply(block)
fun towerPhysicsBase(block: TowerPhysicsComponentBuilder.() -> Unit) = TowerPhysicsComponentBuilder().apply(block)

class TowerTypeBuilder {
    lateinit var name: String
    var cost by Delegates.notNull<Int>()
    var range by Delegates.notNull<Double>()
    var baseFireRate by Delegates.notNull<Double>()
    lateinit var color: Color
    var physics by Delegates.notNull<() -> PhysicsComponent<Tower>>()

    fun physics(block: TowerPhysicsComponentBuilder.() -> Unit) {
        physics = TowerPhysicsComponentBuilder().apply(block).build()
    }

    fun build(): TowerType = TowerType(name, cost, range, baseFireRate, color, physics)

    fun with(block: TowerTypeBuilder.() -> Unit) = this.apply(block).build()
}

class TowerPhysicsComponentBuilder {
    var projectile: ProjectileType? = null
    var onShootFunction: OnShootFunction = ::onShootSingleTarget
    var physicsComponentConstructor: (ProjectileType, OnShootFunction) -> PhysicsComponent<Tower> = ::ShootingComponent

    fun projectile(block: ProjectileTypeBuilder.() -> Unit) {
        projectile = ProjectileTypeBuilder().apply(block).build()
    }

    fun build(): () -> PhysicsComponent<Tower> {
        val hasProjectile = projectile != null
        val resultComponentLambda = projectile?.let { { physicsComponentConstructor(it, onShootFunction)} }

        return resultComponentLambda ?: throw IllegalStateException("Missing physics component for tower")
    }

    fun with(block: TowerPhysicsComponentBuilder.() -> Unit) = this.apply(block).build()
}
