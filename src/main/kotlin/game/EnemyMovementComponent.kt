package game

class EnemyMovementComponent : PhysicsComponent<Enemy> {
    override fun update(entity: Enemy, currentState: GameState, delta: Double) {
        val target = entity.target
        val targetPosition = target.waypoint.center()
        val speedBuff = entity.statusEffects.find(SpeedBuff::class)
        val velocityScaling = speedBuff?.speedScaling ?: 1.0
        val velocity = entity.type.velocity * velocityScaling

        val direction = (targetPosition - entity.position).unitVector()
        entity.position += velocity * direction * delta
    }
}
