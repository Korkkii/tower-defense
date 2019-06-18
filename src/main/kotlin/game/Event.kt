package game

import game.board.BuildAreaSquare
import game.board.PathSquare
import game.enemies.BossType
import game.enemies.Enemy
import game.towers.Tower
import game.towers.TowerType
import game.towers.projectiles.Projectile

interface Event
data class PlacingTowerEvent(val towerType: TowerType) : Event
data class BossStartEvent(
    val bossType: BossType,
    val healthPercent: Double = 1.0,
    val currentTarget: PathSquare? = null,
    val currentPosition: Vector? = null
) : Event

data class PlaceTowerEvent(val square: BuildAreaSquare) : Event
data class SelectTowerEvent(val tower: Tower) : Event
data class NewEnemyEvent(val enemy: Enemy) : Event
data class EnemyDefeated(val enemy: Enemy) : Event
data class GameStateChanged(val money: Int, val enemyCount: Int, val selectedTower: Tower?) : Event
data class NewWave(val wave: Wave) : Event
data class UpgradeClicked(val type: TowerType) : Event
data class BossDefeated(val type: BossType) : Event
data class NewProjectile(val projectile: Projectile) : Event
data class DeleteEntity(val entity: GameEntity) : Event
data class AddEntity(val entity: GameEntity) : Event
object WaveComplete : Event
object EmptyClick : Event
object GameEnded : Event
