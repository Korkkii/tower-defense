package game

import game.towers.Tower
import game.towers.TowerType
import game.towers.projectiles.Projectile
import ui.MouseHandler

class GameState : Observer {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()
    var currentWave: Wave? = null
        private set
    val publisher = Publisher()
    val maxEnemies = 20
    var playerMoney = 50
        private set

    var state: State = Idle
        private set

    init {
        publisher.subscribeToAll(this)
    }

    override fun onNotify(event: Event) {
        when(event) {
            is PlacingTowerEvent -> state = PlacingTower(event.towerType)
            is PlaceTowerEvent -> {
                val currentState = state as? PlacingTower ?: return
                val towerType = currentState.type
                val tower = towerType.create(event.square)

                state = Idle

                // TODO create UI error message for "not enough money"
                // Or maybe prevent trying to place in the first place?
                if (playerMoney < towerType.cost) {
                    tower.deleteTower()
                    return
                }

                playerMoney -= towerType.cost
                towers += tower
                publisher.publish(createStateEvent())
            }
            is SelectTowerEvent -> {
                state = TowerSelected(event.tower)
                publisher.publish(createStateEvent())
            }
            is NewEnemyEvent -> {
                enemies += event.enemy
                publisher.publish(createStateEvent())
                if (enemies.count { !it.canBeDeleted } > maxEnemies) publisher.publish(GameEnded)
            }
            is EnemyDefeated -> {
                playerMoney += event.enemy.enemyPrice
                publisher.publish(createStateEvent(enemyCount = enemies.count { !it.canBeDeleted }))
            }
            is NewWave -> currentWave = event.wave
            is EmptyClick -> {
                state = Idle
                publisher.publish(createStateEvent())
            }
            else -> {}
        }
    }

    private fun createStateEvent(enemyCount: Int, money: Int = playerMoney): GameStateChanged {
        val selectedTower = (state as? TowerSelected)?.tower
        return GameStateChanged(money, enemyCount, selectedTower)
    }
    private fun createStateEvent() = createStateEvent(enemyCount = enemies.count())

    fun mousePosition() = mouseHandler.mousePosition

    companion object {
        val instance = GameState()
        fun notify(event: Event) = instance.publisher.publish(event)
        fun <T : Event> subscribe(event: Class<T>, observer: Observer) = instance.publisher.subscribeToEvent(event, observer)
    }
}

fun MutableList<Enemy>.withinRangeOf(tower: Tower): List<Enemy> {
    val rangeCircle = tower.rangeCircle
    val center = rangeCircle.center()
    return this.filter { enemy ->
        val collisionBoundary = rangeCircle.radius + enemy.radius
        val distance = (center - enemy.position).length
        distance < collisionBoundary
    }
}

sealed class State
object Idle : State()
data class PlacingTower(val type: TowerType) : State()
data class TowerSelected(val tower: Tower) : State()
