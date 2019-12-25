package game

import game.board.PathSquare
import game.data.enemy
import game.data.fastEnemy
import game.enemies.Enemy
import game.enemies.EnemyType
import kotlin.random.Random.Default.nextInt

private val basicWave = listOf(enemy)
private val fastWave = listOf(fastEnemy)
private val mixed = listOf(enemy, fastEnemy)
private val types = listOf(basicWave, fastWave, mixed)

fun createWave(path: List<PathSquare>, level: Int) = types[nextInt(0, 3)].flatMap { type ->
    val count = nextInt(1, 5)
    (0..count).map { Enemy(path, type, level) }
}
