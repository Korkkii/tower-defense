package game

import game.towers.Tower
import game.towers.TowerType

interface Event
data class PlacingTowerEvent(val towerType: TowerType) : Event
data class PlaceTowerEvent(val square: BuildAreaSquare) : Event
data class SelectTowerEvent(val tower: Tower) : Event
data class NewEnemyEvent(val enemy: Enemy) : Event
data class EnemyDefeated(val enemy: Enemy) : Event
data class GameStateChanged(val money: Int, val enemyCount: Int, val selectedTower: Tower?) : Event
data class NewWave(val wave: Wave) : Event
object UpgradeClicked : Event
object WaveComplete : Event
object EmptyClick : Event
object GameEnded : Event
