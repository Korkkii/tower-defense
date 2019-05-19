package game

interface MovementComponent<T : GameEntity> {
    fun update(entity: T, gameState: GameState, delta: Double)
}
