package game.data

import game.AttackSpeedBoost
import game.DamageBoost
import game.DamageOverTime
import game.DamageTakenChange
import game.ExplosionDebuff
import game.SpeedChange
import game.StunEnemyDebuff
import game.towers.AcceleratingShootingComponent
import game.towers.AreaEffectComponent
import game.towers.BuffOthersPhysicsComponent
import game.towers.TypeEnum
import game.towers.onShootMultiTarget
import game.towers.projectiles.IncreasedDamageProperty
import game.towers.projectiles.drawStraightLine
import game.towers.projectiles.onBounceHit
import game.towers.projectiles.onCritProjectileHit
import game.towers.projectiles.onDebuffHit
import game.towers.projectiles.onEnemyMissingHpScaledHit
import game.towers.projectiles.onScalingDamageHit
import game.towers.projectiles.onSingleHit
import game.towers.projectiles.onSplashHit
import game.towers.projectiles.projectileBase
import game.towers.towerPhysicsBase
import game.towers.towerType
import game.towers.towerTypeBase
import javafx.scene.paint.Color

val baseProjectile = projectileBase {
    radius = 2.0
    velocity = 100.0
}

val basicBase = towerTypeBase {
    name = "Basic tower"
    color = Color.WHITE
    enum = TypeEnum.BASIC
}
val basic = basicBase.with {
    cost = 10
    range = 20.0
    baseFireRate = 1.0
    physics { projectile = baseProjectile.with { onHit = onSingleHit(3.0) } }
    level = 1
}
val basic2 = basicBase.with {
    cost = 30
    range = 25.0
    baseFireRate = 1.3
    physics { projectile = baseProjectile.with { onHit = onSingleHit(4.0) } }
    level = 2
}
val basic3 = basicBase.with {
    cost = 100
    range = 35.0
    baseFireRate = 2.0
    physics { projectile = baseProjectile.with { onHit = onSingleHit(6.0) } }
    level = 3
}

val waterBase = towerTypeBase {
    name = "Water tower"
    color = Color.NAVY
    enum = TypeEnum.WATER
}
val water = waterBase.with {
    cost = 30
    range = 35.0
    baseFireRate = 1.0
    physics { projectile = baseProjectile.with { onHit = onSplashHit(6.0, 25.0) } }
    level = 1
}
val water2 = waterBase.with {
    cost = 60
    range = 35.0
    baseFireRate = 1.2
    physics { projectile = baseProjectile.with { onHit = onSplashHit(10.0, 40.0) } }
    level = 2
}
val water3 = waterBase.with {
    cost = 120
    range = 35.0
    baseFireRate = 1.5
    physics { projectile = baseProjectile.with { onHit = onSplashHit(20.0, 60.0) } }
    level = 3
}

val lightBase = towerTypeBase {
    name = "Light tower"
    color = Color.ANTIQUEWHITE
    enum = TypeEnum.LIGHT
}
val lightProjectile = projectileBase {
    radius = 0.5
    velocity = 300.0
    drawGraphics = ::drawStraightLine
}
val multishotComponent = towerPhysicsBase {
    onShootFunction = ::onShootMultiTarget
}
val light = lightBase.with {
    cost = 40
    range = 35.0
    baseFireRate = 1.0
    physics = multishotComponent.with {
        projectile = lightProjectile.with {
            onHit = onSingleHit(2.0)
        }
    }
    level = 1
}
val light2 = lightBase.with {
    cost = 80
    range = 35.0
    baseFireRate = 1.75
    physics = multishotComponent.with {
        projectile = lightProjectile.with {
            onHit = onSingleHit(2.5)
        }
    }
    level = 2
}
val light3 = lightBase.with {
    cost = 160
    range = 35.0
    baseFireRate = 2.4
    physics = multishotComponent.with {
        projectile = lightProjectile.with {
            onHit = onSingleHit(3.0)
        }
    }
    level = 3
}

val fireBase = towerTypeBase {
    name = "Fire tower"
    color = Color.ORANGERED
    enum = TypeEnum.FIRE
}
val fire = fireBase.with {
    cost = 40
    range = 30.0
    baseFireRate = 1.0
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(2.0) {
                DamageOverTime(5.0, 5.0, 8.0)
            }
        }
    }
    level = 1
}
val fire2 = fireBase.with {
    cost = 80
    range = 30.0
    baseFireRate = 1.0
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(2.5) {
                DamageOverTime(7.0, 6.0, 8.0)
            }
        }
    }
    level = 2
}
val fire3 = fireBase.with {
    cost = 160
    range = 30.0
    baseFireRate = 1.0
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(3.0) {
                DamageOverTime(12.0, 7.0, 8.0)
            }
        }
    }
    level = 3
}

val windBase = towerTypeBase {
    name = "Wind tower"
    color = Color.SILVER
    enum = TypeEnum.WIND
}
val wind =
    windBase.with {
        cost = 30
        range = 35.0
        baseFireRate = 1.0
        physics {
            projectile = baseProjectile.with {
                onHit = onBounceHit(2.5, 25.0, 3)
            }
        }
        level = 1
    }
val wind2 =
    windBase.with {
        cost = 80
        range = 35.0
        baseFireRate = 1.2
        physics {
            projectile= baseProjectile.with {
                onHit = onBounceHit(3.2, 25.0, 5)
            }
        }
        level = 2
    }
val wind3 =
    windBase.with {
        cost = 150
        range = 70.0
        baseFireRate = 1.4
        physics {
            projectile = baseProjectile.with{
                onHit = onBounceHit(5.0, 30.0, 10)
            }
        }
        level = 3
    }

val natureBase = towerTypeBase {
    name = "Nature tower"
    color = Color.DARKSEAGREEN
    enum = TypeEnum.NATURE
}
val nature = natureBase.with {
    cost = 30
    range = 35.0
    baseFireRate = 1.0
    physics {
        projectile = baseProjectile.with {
            onHit = onScalingDamageHit(3.0, 2.0)
            propertyConstructor = { IncreasedDamageProperty(it.size) }
        }
    }
    level = 1
}
val nature2 = natureBase.with {
    cost = 60
    range = 35.0
    baseFireRate = 1.25
    physics {
        projectile = baseProjectile.with {
            onHit = onScalingDamageHit(5.0, 2.3)
            propertyConstructor = { IncreasedDamageProperty(it.size) }
        }
    }
    level = 2
}
val nature3 = natureBase.with {
    cost = 120
    range = 35.0
    baseFireRate = 1.5
    physics {
        projectile = baseProjectile.with {
            onHit = onScalingDamageHit(7.0, 2.7)
            propertyConstructor = { IncreasedDamageProperty(it.size) }
        }
    }
    level = 3
}

val metalPhysics = towerPhysicsBase {
    // TODO: Max attack speed cap per tower level
    physicsComponentConstructor = ::AcceleratingShootingComponent
}
val metalBase = towerTypeBase {
    name = "Metal tower"
    color = Color.DARKSEAGREEN
    enum = TypeEnum.METAL
}
val metal = metalBase.with {
    cost = 20
    range = 35.0
    baseFireRate = 1.5
    physics = metalPhysics.with {
        projectile = baseProjectile.with { onHit = onSingleHit(3.0) }
    }
    level = 1
}
val metal2 = metalBase.with {
    cost = 50
    range = 40.0
    baseFireRate = 1.8
    physics = metalPhysics.with {
        projectile = baseProjectile.with { onHit = onSingleHit(4.0) }
    }
    level = 2
}
val metal3 = metalBase.with {
    cost = 100
    range = 45.0
    baseFireRate = 2.4
    physics = metalPhysics.with {
        projectile = baseProjectile.with { onHit = onSingleHit(6.0) }
    }
    level = 3
}

val blacksmithBase = towerTypeBase {
    name = "Blacksmith tower"
    color = Color.DARKRED
    enum = TypeEnum.BLACKSMITH
}
val blacksmith = blacksmithBase.with {
    cost = 100
    range = 30.0
    baseFireRate = 1.0
    physics = { BuffOthersPhysicsComponent { DamageBoost(5.0, 0.50) } }
    level = 1
}
val blacksmith2 = blacksmithBase.with {
    cost = 250
    range = 35.0
    baseFireRate = 1.0
    physics = { BuffOthersPhysicsComponent { DamageBoost(5.0, 1.00) } }
    level = 2
}

val quickshotBase = towerTypeBase {
    name = "Quickshot tower"
    color = Color.LIGHTSKYBLUE
    enum = TypeEnum.QUICKSHOT
}
val quickshot = quickshotBase.with {
    cost = 100
    range = 30.0
    baseFireRate = 1.0
    physics = { BuffOthersPhysicsComponent { AttackSpeedBoost(5.0, 0.50) } }
    level = 1
}
val quickshot2 = quickshotBase.with {
    cost = 250
    range = 35.0
    baseFireRate = 1.0
    physics = { BuffOthersPhysicsComponent { AttackSpeedBoost(5.0, 1.00) } }
    level = 2
}

val critTowerBase = towerTypeBase {
    name = "Critical strike tower"
    color = Color.DARKORANGE
    enum = TypeEnum.CRIT
}
val critTower = critTowerBase.with {
    cost = 90
    range = 30.0
    baseFireRate = 1.5
    physics {
        projectile = baseProjectile.with {
            onHit = onCritProjectileHit(3.0, 0.2, 2.0)
        }
    }
    level = 1
}
val critTower2 = critTowerBase.with {
    cost = 200
    range = 30.0
    baseFireRate = 2.0
    physics {
        projectile = baseProjectile.with {
            onHit = onCritProjectileHit(5.0, 0.3, 3.0)
        }
    }
    level = 2
}

val steamTowerBase = towerTypeBase {
    name = "Steam tower"
    color = Color.WHITESMOKE
    enum = TypeEnum.STEAM
}
val steamTower = steamTowerBase.with {
    cost = 100
    range = 30.0
    baseFireRate = 10.0
    physics = { AreaEffectComponent(3.0, 6.0) }
    level = 1
}
val steamTower2 = steamTowerBase.with {
    cost = 220
    range = 35.0
    baseFireRate = 10.0
    physics = { AreaEffectComponent(9.0, 6.0) }
    level = 2
}

val frostTowerBase = towerTypeBase {
    name = "Frost tower"
    color = Color.LIGHTSTEELBLUE
    enum = TypeEnum.FROST
}
val frostTower = frostTowerBase.with {
    cost = 100
    range = 30.0
    baseFireRate = 10.0
    physics = {
        AreaEffectComponent(1.0, 6.0) { enemy ->
            enemy.statusEffects += SpeedChange(0.7, 2.0)
        }
    }
    level = 1
}
val frostTower2 = frostTowerBase.with {
    cost = 250
    range = 30.0
    baseFireRate = 10.0
    physics = {
        AreaEffectComponent(1.0, 6.0) { enemy ->
            enemy.statusEffects += SpeedChange(0.4, 2.0)
        }
    }
    level = 2
}

val photosynthesisBase = towerTypeBase {
    name = "Photosynthesis tower"
    color = Color.FORESTGREEN
    enum = TypeEnum.PHOTOSYNTHESIS
}
val photosynthesis = photosynthesisBase.with {
    cost = 100
    range = 30.0
    baseFireRate = 1.0
    physics {
        projectile = baseProjectile.with {
            onHit = onEnemyMissingHpScaledHit(3.0, 2.0)
        }
    }
    level = 1
}
val photosynthesis2 = photosynthesisBase.with {
    cost = 250
    range = 40.0
    baseFireRate = 1.3
    physics {
        projectile = baseProjectile.with {
            onHit = onEnemyMissingHpScaledHit(6.0, 3.0)
        }
    }
    level = 2
}

val rust = towerType {
    name = "Rust tower"
    cost = 350
    range = 40.0
    baseFireRate = 1.0
    color = Color.FIREBRICK
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(1.0) {
                DamageTakenChange(2.0, 2.0)
            }
        }
    }
    enum = TypeEnum.RUST
    level = 1
}

val iceberg = towerType {
    name = "Iceberg tower"
    cost = 350
    range = 30.0
    baseFireRate = 1.0
    color = Color.BLUEVIOLET
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(1.0) { StunEnemyDebuff(1.0) }
        }
    }
    enum = TypeEnum.ICEBERG
    level = 1
}

val explosion = towerType {
    name = "Explosion tower"
    cost = 350
    range = 35.0
    baseFireRate = 1.0
    color = Color.BLUEVIOLET
    physics {
        projectile = baseProjectile.with {
            onHit = onDebuffHit(2.0) { ExplosionDebuff(3.0) }
        }
    }
    enum = TypeEnum.EXPLOSION
    level = 1
}

val gold = towerType {
    name = "Gold tower"
    cost = 300
    range = 40.0
    baseFireRate = 0.33
    color = Color.BLUEVIOLET
    physics {
        projectile = baseProjectile.with {
            onHit = onSingleHit(12.0)
        }
    }
    enum = TypeEnum.GOLD
    level = 1
}
