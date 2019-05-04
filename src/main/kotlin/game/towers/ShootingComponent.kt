package game.towers

import game.Enemy
import game.GameState
import game.center

class ShootingComponent {
    private var firingCooldown = 0.0

    fun update(tower: Tower, currentState: GameState, delta: Double) {
        val enemies = currentState.enemies
        if (enemies.isEmpty()) return

        firingCooldown -= delta
        val canFire = firingCooldown <= 0.0
        val closestEnemy = enemies.reduce { currentClosest, nextEnemy ->
            if (distanceToEnemy(tower, currentClosest) > distanceToEnemy(tower, nextEnemy)) nextEnemy
            else currentClosest
        }

        if (withinRange(tower, closestEnemy) && canFire) {
            currentState.projectiles += tower.projectileConstructor(tower, closestEnemy)
            val secondsUntilNext = 1 / tower.fireRate
            firingCooldown = secondsUntilNext
        }
    }

    private fun distanceToEnemy(tower: Tower, enemy: Enemy): Double = (tower.rangeCircle.center() - enemy.position).length

    private fun withinRange(tower: Tower, enemy: Enemy): Boolean {
        val rangeCircle = tower.rangeCircle
        val collisionBoundary  = rangeCircle.radius + enemy.radius
        val distance = (rangeCircle.center() - enemy.position).length
        return distance < collisionBoundary
    }
}
