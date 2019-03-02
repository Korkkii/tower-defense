package game

interface Event
object PlacingTowerEvent : Event
data class PlaceTowerEvent(val square: BuildAreaSquare) : Event
data class SelectTowerEvent(val tower: Tower) : Event