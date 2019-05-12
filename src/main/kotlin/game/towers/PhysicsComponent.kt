package game.towers

import game.GameState

interface PhysicsComponent<T> {
    fun update(entity: T, currentState: GameState, delta: Double)
}
