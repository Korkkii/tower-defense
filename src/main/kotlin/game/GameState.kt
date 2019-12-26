package game

import game.enemies.BossType
import game.enemies.Enemy
import game.towers.Tower
import game.towers.TowerType
import game.towers.TypeEnum
import game.towers.projectiles.Projectile
import ui.MouseHandler
import kotlin.math.roundToInt
import kotlin.reflect.KClass

class MutableGameState {
    val enemies = mutableListOf<Enemy>()
    val towers = mutableListOf<Tower>()
    val projectiles = mutableListOf<Projectile>()
    val mouseHandler = MouseHandler()
    var currentWave: Wave? = null
        private set
    val publisher = Publisher()
    private val maxEnemies = 20
    var playerMoney = 300
        private set

    var state: State = Idle
        private set
    val defeatedBosses = mutableSetOf<BossType>()
    val miscEntities = mutableListOf<GameEntity>()
    private val additionsList = mutableListOf<GameEntity>()
    private val deletionsList = mutableListOf<GameEntity>()

    init {
        publisher.subscribeToAll(::onNotify)
    }

    private fun onNotify(event: Event) {
        when (event) {
            is PlacingTowerEvent -> state = PlacingTower(event.towerType)
            is PlaceTowerEvent -> {
                val currentState = state as? PlacingTower ?: return
                val towerType = currentState.type
                val tower = towerType.create(event.square)

                state = Idle

                if (playerMoney < towerType.cost) {
                    GameState.notify(NotEnoughMoney)
                    tower.deleteTower()
                    return
                }

                playerMoney -= towerType.cost
                additionsList += tower
                publisher.publish(createStateEvent())
            }
            is SelectTowerEvent -> {
                state = TowerSelected(event.tower)
                publisher.publish(createStateEvent())
            }
            is NewEnemyEvent -> {
                additionsList += event.enemy
            }
            is EnemyDefeated -> {
                val type = event.enemy.type
                val shooter = event.shooter
                val priceModifier = if (shooter?.type?.type == TypeEnum.GOLD) 2 else 1
                val price = priceModifier * type.enemyPrice
                playerMoney += price
                if (type is BossType) {
                    val isWaterBoss = type.type == TypeEnum.WATER
                    val waterBossesStillLeft = enemies.any { it.type.type == TypeEnum.WATER }
                    if (isWaterBoss && waterBossesStillLeft) return

                    defeatedBosses += type

                    publisher.publish(BossDefeated(type))
                }
                publisher.publish(createStateEvent(enemyCount = enemies.count { !it.canBeDeleted }))
            }
            is NewWave -> currentWave = event.wave
            is EmptyClick -> {
                state = Idle
                publisher.publish(createStateEvent())
            }
            is UpgradeClicked -> {
                val currentTower = (state as? TowerSelected)?.tower ?: return
                val clickedType = event.type
                val cost = clickedType.cost - currentTower.type.cost
                if (cost > playerMoney) return

                playerMoney -= cost
                val upgradedTower = currentTower.upgrade(clickedType)

                currentTower.deleteTower()

                towers += upgradedTower

                state = TowerSelected(upgradedTower)
                publisher.publish(createStateEvent())
            }
            is NewProjectile -> {
                additionsList += event.projectile
            }
            is AddEntity -> {
                additionsList += event.entity
            }
            is DeleteEntity -> {
                deletionsList += event.entity
            }
            is SellTower -> {
                val currentTower = (state as? TowerSelected)?.tower ?: return
                val sellPrice = (currentTower.type.cost * 0.7).roundToInt()

                playerMoney += sellPrice
                currentTower.deleteTower()

                state = Idle
                publisher.publish(createStateEvent())
            }
            else -> {
            }
        }
    }

    fun commitUpdates() {
        val shouldSendStateEvent =
            additionsList.filterIsInstance<Enemy>().isNotEmpty() && enemies.any { it.canBeDeleted }
        additionsList.forEach {
            when (it) {
                is Enemy -> enemies += it
                is Tower -> towers += it
                is Projectile -> projectiles += it
                else -> miscEntities += it
            }
        }
        deletionsList.forEach { miscEntities.remove(it) }
        additionsList.clear()
        deletionsList.clear()

        projectiles.removeAll { it.canDelete() }
        enemies.removeAll { it.canBeDeleted }
        towers.removeAll { it.canBeDeleted }

        if (shouldSendStateEvent) publisher.publish(createStateEvent())
        // TODO: Create end screen after game ends 
        // With e.g. how many enemies killed, how many waves survived
        if (enemies.count { !it.canBeDeleted } > maxEnemies) publisher.publish(GameEnded)
    }

    private fun createStateEvent(enemyCount: Int, money: Int = playerMoney): GameStateChanged {
        val selectedTower = (state as? TowerSelected)?.tower
        return GameStateChanged(money, enemyCount, selectedTower)
    }

    private fun createStateEvent() = createStateEvent(enemyCount = enemies.count())

    companion object {
        internal val instance = MutableGameState()
    }
}

data class GameState(
    val playerMoney: Int,
    val enemies: List<Enemy>,
    val towers: List<Tower>,
    val projectiles: List<Projectile>,
    val entities: List<GameEntity>,
    val currentWave: Wave?,
    val defeatedBosses: Set<BossType>,
    val state: State
) {
    fun commit() {
        mutableState.commitUpdates()
    }

    val mouseHandler = mutableState.mouseHandler

    fun mousePosition() = mutableState.mouseHandler.mousePosition

    companion object {
        private val mutableState = MutableGameState.instance
        val instance: GameState
            get() {
                return GameState(
                    mutableState.playerMoney,
                    mutableState.enemies,
                    mutableState.towers,
                    mutableState.projectiles,
                    mutableState.miscEntities,
                    mutableState.currentWave,
                    mutableState.defeatedBosses,
                    mutableState.state
                )
            }

        fun notify(event: Event) = mutableState.publisher.publish(event)
        fun <T : Event> subscribe(event: KClass<T>, callback: (T) -> Unit) =
            mutableState.publisher.subscribeToEvent(event, callback)
    }
}

fun List<Enemy>.withinRangeOf(tower: Tower): List<Enemy> {
    val rangeCircle = tower.rangeCircle
    val center = rangeCircle.center()
    return this.filter { enemy ->
        val collisionBoundary = rangeCircle.radius + enemy.type.radius
        val distance = (center - enemy.position).length
        distance < collisionBoundary
    }
}

sealed class State
object Idle : State()
data class PlacingTower(val type: TowerType) : State()
data class TowerSelected(val tower: Tower) : State()
