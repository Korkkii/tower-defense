package game

interface Event
object PlacingTowerEvent : Event
data class PlaceTowerEvent(val square: BuildAreaSquare) : Event