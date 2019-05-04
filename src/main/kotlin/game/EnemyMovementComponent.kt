package game

class EnemyMovementComponent : MovementComponent<Enemy> {
    override fun update(entity: Enemy, gameState: GameState, delta: Double) {
        val target = entity.target
        val targetPosition = target.waypoint.center()

        val direction = (targetPosition - entity.position).unitVector()
        entity.position += entity.velocity * direction * delta
    }
}
