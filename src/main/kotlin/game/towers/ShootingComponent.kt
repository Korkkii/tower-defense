package game.towers

import game.Enemy
import game.GameState
import game.center

class ShootingComponent : PhysicsComponent<ProjectileTower> {
    private var firingCooldown = 0.0

    override fun update(entity: ProjectileTower, currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0
        val closestEnemy = enemies.reduce { currentClosest, nextEnemy ->
            if (distanceToEnemy(entity, currentClosest) > distanceToEnemy(entity, nextEnemy)) nextEnemy
            else currentClosest
        }

        if (withinRange(entity, closestEnemy) && canFire) {
            currentState.projectiles += entity.projectileConstructor(entity, closestEnemy)
            val secondsUntilNext = 1 / entity.fireRate
            firingCooldown = secondsUntilNext
        }
    }

    private fun distanceToEnemy(tower: Tower, enemy: Enemy): Double =
        (tower.rangeCircle.center() - enemy.position).length

    private fun withinRange(tower: Tower, enemy: Enemy): Boolean {
        val rangeCircle = tower.rangeCircle
        val collisionBoundary = rangeCircle.radius + enemy.radius
        val distance = (rangeCircle.center() - enemy.position).length
        return distance < collisionBoundary
    }
}
