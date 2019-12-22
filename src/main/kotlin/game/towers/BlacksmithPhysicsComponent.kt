package game.towers

import game.DamageBoost
import game.GameState
import game.PhysicsComponent

class BlacksmithPhysicsComponent : PhysicsComponent<Tower> {
    override fun update(entity: Tower, currentState: GameState, delta: Double) {
        val towersWithoutBuff =
            currentState.towers.filter { it.statusEffects.find(DamageBoost::class) == null }.ifEmpty { null }
        val randomTowerWithoutBuff = towersWithoutBuff?.random()

        randomTowerWithoutBuff?.let { it.statusEffects += DamageBoost(5.0, 0.50) }
    }
}
