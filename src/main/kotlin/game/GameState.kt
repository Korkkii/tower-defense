package game

import ui.MouseHandler

class GameState : Observer {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()
    var currentWave: Wave? = null
    val publisher = Publisher()
    var playerMoney = 50

    var playerLives = 3
        private set
    var state: State = Idle
        private set

    override fun onNotify(event: Event) {
        when(event) {
            PlacingTowerEvent -> state = PlacingTower
            is PlaceTowerEvent -> if (state == PlacingTower) {
                state = Idle
                val tower = Tower(event.square)

                // TODO create UI error message for "not enough money"
                // Or maybe prevent trying to place in the first place?
                if (playerMoney < tower.cost) return

                playerMoney -= tower.cost
                towers += tower
                publisher.publish(GameStateChanged)
            }
            is SelectTowerEvent -> state = SelectedTower(event.tower)
            is EnemyReachedEndEvent -> {
                playerLives -= 1
                val stateEvent = if (playerLives >= 1) GameStateChanged else GameEnded
                publisher.publish(stateEvent)
            }
            is NewEnemyEvent -> enemies += event.enemy
            else -> {}
        }
    }

    fun mousePosition() = mouseHandler.mousePosition

    companion object {
        val instance = GameState()
        fun notify(event: Event) = instance.onNotify(event)
        fun subscribe(event: Event, observer: Observer) = instance.publisher.subscribeToEvent(event, observer)
    }
}

sealed class State
object Idle : State()
object PlacingTower : State()
data class SelectedTower(val tower: Tower) : State()
