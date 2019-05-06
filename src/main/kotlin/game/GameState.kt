package game

import game.towers.Tower
import game.towers.projectiles.Projectile
import ui.MouseHandler

class GameState : Observer {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()
    var currentWave: Wave? = null
    val publisher = Publisher()
    val maxEnemies = 20
    var playerMoney = 50

    var state: State = Idle
        private set

    init {
        publisher.subscribeToAll(this)
    }

    override fun onNotify(event: Event) {
        when(event) {
            is PlacingTowerEvent<*> -> state = PlacingTower(event.towerConstructor)
            is PlaceTowerEvent -> {
                val currentState = state as? PlacingTower<*> ?: return

                val tower = currentState.constructor(event.square)

                state = Idle

                // TODO create UI error message for "not enough money"
                // Or maybe prevent trying to place in the first place?
                if (playerMoney < tower.cost) {
                    tower.deleteTower()
                    return
                }

                playerMoney -= tower.cost
                towers += tower
                publisher.publish(createStateEvent())
            }
            is SelectTowerEvent -> state = SelectedTower(event.tower)
            is NewEnemyEvent -> {
                enemies += event.enemy
                publisher.publish(createStateEvent())
                if (enemies.count { !it.canBeDeleted } > maxEnemies) publisher.publish(GameEnded)
            }
            is EnemyDefeated -> {
                playerMoney += event.enemy.enemyPrice
                publisher.publish(createStateEvent(enemyCount = enemies.count { !it.canBeDeleted }))
            }
            else -> {}
        }
    }

    private fun createStateEvent(enemyCount: Int, money: Int = playerMoney) = GameStateChanged(money, enemyCount)
    private fun createStateEvent() = createStateEvent(enemyCount = enemies.count())

    fun mousePosition() = mouseHandler.mousePosition

    companion object {
        val instance = GameState()
        fun notify(event: Event) = instance.publisher.publish(event)
        fun <T : Event> subscribe(event: Class<T>, observer: Observer) = instance.publisher.subscribeToEvent(event, observer)
    }
}

sealed class State
object Idle : State()
data class PlacingTower<T : Tower>(val constructor: (BuildAreaSquare) -> T) : State()
data class SelectedTower(val tower: Tower) : State()
