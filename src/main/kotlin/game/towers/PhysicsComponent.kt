package game.towers

import game.GameEntity
import game.GameState

interface PhysicsComponent<T : GameEntity> {
    fun update(entity: T, currentState: GameState, delta: Double)
}
