package game

import game.towers.Tower

interface Event
data class PlacingTowerEvent<T : Tower>(val towerConstructor: (square: BuildAreaSquare) -> T) : Event
data class PlaceTowerEvent(val square: BuildAreaSquare) : Event
data class SelectTowerEvent(val tower: Tower) : Event
data class EnemyReachedEndEvent(val enemy: Enemy) : Event
data class NewEnemyEvent(val enemy: Enemy) : Event
data class EnemyDefeated(val enemy: Enemy) : Event
object GameStateChanged : Event
object GameEnded : Event
