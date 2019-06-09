package game

interface PhysicsComponent<T> {
    fun update(entity: T, currentState: GameState, delta: Double)
}
