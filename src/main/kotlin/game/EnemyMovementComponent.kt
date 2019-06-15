package game

class EnemyMovementComponent : MovementComponent<Enemy> {
    override fun update(entity: Enemy, gameState: GameState, delta: Double) {
        val target = entity.target
        val targetPosition = target.waypoint.center()
        val speedBuff = entity.statusEffects.find { it is SpeedBuff } as? SpeedBuff
        val velocityScaling = speedBuff?.speedScaling ?: 1.0
        val velocity = entity.type.velocity * velocityScaling

        val direction = (targetPosition - entity.position).unitVector()
        entity.position += velocity * direction * delta
    }
}
