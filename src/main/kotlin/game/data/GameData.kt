package game.data

import game.towers.TypeEnum

object GameData {
    val towerTypes = listOf(
        basic
        // light,
        // fire,
        // wind,
        // water,
        // nature,
        // metal,
        // blacksmith,
        // quickshot,
        // critTower,
        // steamTower,
        // frostTower,
        // photosynthesis,
        // rust,
        // iceberg,
        // explosion,
        // gold
    )
    val bosses = listOf(
        windBoss,
        metalBoss,
        natureBoss,
        fireBoss,
        lightBoss,
        waterBoss
    )
    val towerUpgradeRequirements = mapOf(
        Pair(TypeEnum.LIGHT, 1) to listOf(lightBoss),
        Pair(TypeEnum.WATER, 1) to listOf(waterBoss),
        Pair(TypeEnum.FIRE, 1) to listOf(fireBoss),
        Pair(TypeEnum.WIND, 1) to listOf(windBoss),
        Pair(TypeEnum.METAL, 1) to listOf(metalBoss),
        Pair(TypeEnum.NATURE, 1) to listOf(natureBoss),
        Pair(TypeEnum.BLACKSMITH, 1) to listOf(fireBoss2, metalBoss2),
        Pair(TypeEnum.QUICKSHOT, 1) to listOf(windBoss2, lightBoss2),
        Pair(TypeEnum.CRIT, 1) to listOf(natureBoss2, metalBoss2),
        Pair(TypeEnum.STEAM, 1) to listOf(waterBoss2, fireBoss2),
        Pair(TypeEnum.FROST, 1) to listOf(waterBoss2, windBoss2),
        Pair(TypeEnum.PHOTOSYNTHESIS, 1) to listOf(natureBoss2, lightBoss2),
        Pair(TypeEnum.RUST, 1) to listOf(metalBoss3, windBoss3, waterBoss3),
        Pair(TypeEnum.ICEBERG, 1) to listOf(windBoss3, lightBoss3, waterBoss3),
        Pair(TypeEnum.EXPLOSION, 1) to listOf(fireBoss3, natureBoss3, metalBoss3),
        Pair(TypeEnum.GOLD, 1) to listOf(lightBoss3, fireBoss3, natureBoss3),
        Pair(TypeEnum.LIGHT, 2) to listOf(lightBoss2),
        Pair(TypeEnum.WATER, 2) to listOf(waterBoss2),
        Pair(TypeEnum.FIRE, 2) to listOf(fireBoss2),
        Pair(TypeEnum.WIND, 2) to listOf(windBoss2),
        Pair(TypeEnum.METAL, 2) to listOf(metalBoss2),
        Pair(TypeEnum.NATURE, 2) to listOf(natureBoss2),
        Pair(TypeEnum.LIGHT, 3) to listOf(lightBoss3),
        Pair(TypeEnum.WATER, 3) to listOf(waterBoss3),
        Pair(TypeEnum.FIRE, 3) to listOf(fireBoss3),
        Pair(TypeEnum.WIND, 3) to listOf(windBoss3),
        Pair(TypeEnum.METAL, 3) to listOf(metalBoss3),
        Pair(TypeEnum.NATURE, 3) to listOf(natureBoss3),
        Pair(TypeEnum.BLACKSMITH, 2) to listOf(fireBoss3, metalBoss3),
        Pair(TypeEnum.QUICKSHOT, 2) to listOf(windBoss3, lightBoss3),
        Pair(TypeEnum.CRIT, 2) to listOf(natureBoss3, metalBoss3),
        Pair(TypeEnum.STEAM, 2) to listOf(waterBoss3, fireBoss3),
        Pair(TypeEnum.FROST, 2) to listOf(waterBoss3, windBoss3),
        Pair(TypeEnum.PHOTOSYNTHESIS, 2) to listOf(natureBoss3, lightBoss3)
    )
    val possibleTowerUpgrades = mapOf(
        basic to listOf(
            basic2, light,
            fire,
            wind,
            water,
            nature,
            metal
        ),
        basic2 to listOf(
            basic3,
            light2,
            fire2,
            wind2,
            water2,
            nature2,
            metal2
        ),
        basic3 to listOf(
            light3,
            fire3,
            wind3,
            water3,
            nature3,
            metal3
        ),
        light to listOf(light2),
        fire to listOf(fire2),
        wind to listOf(wind2),
        water to listOf(water2),
        nature to listOf(nature2),
        metal to listOf(metal2),
        light2 to listOf(photosynthesis, quickshot, light3),
        fire2 to listOf(steamTower, blacksmith, fire3),
        wind2 to listOf(frostTower, quickshot, wind3),
        water2 to listOf(steamTower, frostTower, water3),
        nature2 to listOf(photosynthesis, critTower, nature3),
        metal2 to listOf(blacksmith, critTower, metal3),
        light3 to listOf(photosynthesis2, quickshot2),
        fire3 to listOf(steamTower2, blacksmith2),
        wind3 to listOf(frostTower2, quickshot2),
        water3 to listOf(steamTower2, frostTower2),
        nature3 to listOf(photosynthesis2, critTower2),
        metal3 to listOf(blacksmith2, critTower2),
        blacksmith to listOf(blacksmith2),
        quickshot to listOf(quickshot2),
        critTower to listOf(critTower2),
        steamTower to listOf(steamTower2),
        frostTower to listOf(frostTower2),
        photosynthesis to listOf(photosynthesis2),
        blacksmith2 to listOf(explosion),
        quickshot2 to listOf(iceberg),
        critTower2 to listOf(explosion),
        steamTower2 to listOf(),
        frostTower2 to listOf(iceberg),
        photosynthesis2 to listOf(gold)
    )
}
