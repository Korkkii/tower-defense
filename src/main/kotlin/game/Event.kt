package game

interface Event
object PlacingTowerEvent : Event
data class PlaceTowerEvent(val square: BuildAreaSquare) : Event
data class SelectTowerEvent(val tower: Tower) : Event
data class EnemyReachedEndEvent(val enemy: Enemy) : Event