package game

import ui.MouseHandler

class GameState : Observer {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()
    var currentWave: Wave? = null

    var playerLives = 3
        private set
    var state: State = Idle
        private set

    override fun onNotify(event: Event) {
        when(event) {
            PlacingTowerEvent -> state = PlacingTower
            is PlaceTowerEvent -> if (state == PlacingTower) {
                val tower = Tower(event.square)
                towers += tower
                state = Idle
            }
            is SelectTowerEvent -> state = SelectedTower(event.tower)
            is EnemyReachedEndEvent -> playerLives -= 1
            is NewEnemyEvent -> enemies += event.enemy
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
data class SelectedTower(val tower: Tower) : State()