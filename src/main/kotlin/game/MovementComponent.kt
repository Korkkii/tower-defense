package game

interface MovementComponent<T : MovingEntity> {
    fun update(entity: T, gameState: GameState, delta: Double)
}
