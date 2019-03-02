package game

import ui.MouseHandler

class GameState : Observer {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()

    var state: State = Idle
        private set

    override fun onNotify(event: Event) {
        when(event) {
            PlacingTowerEvent -> if (state == Idle) state = PlacingTower
            is PlaceTowerEvent -> if (state == PlacingTower) {
                val tower = Tower(event.square)
                towers += tower
                state = Idle
            }
            else -> {}
        }
    }

    fun mousePosition() = mouseHandler.mousePosition

    companion object {
        val instance = GameState()
        fun notify(event: Event) = instance.onNotify(event)
    }
}

sealed class State
object Idle : State()
object PlacingTower : State()