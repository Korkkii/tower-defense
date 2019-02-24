package game

class MovementComponent(private val entity: MovingEntity) {
    fun update(gameState: GameState, delta: Double) {
        val direction = (entity.targetPosition - entity.position).unitVector()
        entity.position += entity.velocity * direction * delta
    }
}