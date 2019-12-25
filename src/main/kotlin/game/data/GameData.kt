package game.data

object GameData {
    val towerTypes = listOf(
        basic,
        light,
        fire,
        wind,
        water,
        nature,
        metal,
        blacksmith,
        quickshot,
        critTower,
        steamTower,
        frostTower,
        photosynthesis,
        rust,
        iceberg,
        explosion,
        gold
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
        light to listOf(lightBoss),
        water to listOf(waterBoss),
        fire to listOf(fireBoss),
        wind to listOf(windBoss),
        metal to listOf(metalBoss),
        nature to listOf(natureBoss),
        blacksmith to listOf(fireBoss, metalBoss),
        quickshot to listOf(windBoss, lightBoss),
        critTower to listOf(natureBoss, metalBoss),
        steamTower to listOf(waterBoss, fireBoss),
        frostTower to listOf(waterBoss, windBoss),
        photosynthesis to listOf(natureBoss, lightBoss),
        rust to listOf(metalBoss, windBoss, waterBoss),
        iceberg to listOf(windBoss, lightBoss, waterBoss),
        explosion to listOf(fireBoss, natureBoss, metalBoss),
        gold to listOf(lightBoss, fireBoss, natureBoss)
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
