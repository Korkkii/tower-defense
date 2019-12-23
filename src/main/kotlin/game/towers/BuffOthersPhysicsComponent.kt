package game.towers

import game.DamageBoost
import game.GameState
import game.PhysicsComponent
import game.StatusEffect

class BuffOthersPhysicsComponent(private val buffCreator: () -> StatusEffect<Tower>): PhysicsComponent<Tower> {
    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        val towersWithoutBuff =
            currentState.towers.filter { it.statusEffects.find(DamageBoost::class) == null }.ifEmpty { null }
        val randomTowerWithoutBuff = towersWithoutBuff?.random()
        val buff = buffCreator()

        randomTowerWithoutBuff?.let { it.statusEffects += buff }
    }
}
